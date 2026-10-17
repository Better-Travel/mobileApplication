import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.gradle.api.artifacts.ProjectDependency

plugins {
    base
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.compose.compiler) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.sqldelight) apply false
    alias(libs.plugins.ktlint) apply false
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    extensions.configure<KtlintExtension> {
        version.set(
            rootProject.extensions
                .getByType<VersionCatalogsExtension>()
                .named("libs")
                .findVersion("ktlint")
                .get()
                .requiredVersion,
        )
        filter {
            exclude("**/build/**")
            exclude("**/StringRepository.kt")
        }
    }
}

val sharedModules = listOf(
    ":common",
    ":domain",
    ":networking",
    ":database",
    ":localization",
    ":presentation",
    ":ui",
    ":application",
)

val allowedDependencies = mapOf(
    ":common" to emptySet(),
    ":domain" to setOf(":common"),
    ":domainMock" to setOf(":common", ":domain"),
    ":networking" to setOf(":common", ":domain"),
    ":database" to setOf(":common", ":domain"),
    ":localization" to setOf(":common"),
    ":presentation" to setOf(":common", ":domain", ":localization"),
    ":ui" to setOf(":common", ":domain", ":presentation"),
    ":application" to setOf(
        ":common",
        ":domain",
        ":networking",
        ":database",
        ":localization",
        ":presentation",
        ":ui",
    ),
    ":android:platform" to setOf(":common", ":database", ":domain"),
    ":android:presentation" to setOf(":common", ":presentation"),
    ":android:application" to setOf(":application", ":database", ":android:platform"),
    ":android:app" to setOf(
        ":application",
        ":ui",
        ":android:application",
        ":android:platform",
        ":android:presentation",
    ),
)

val checkModuleDependencies = tasks.register("checkModuleDependencies") {
    group = "verification"
    description = "Fails when a module depends on a module it is not allowed to depend on"
    doLast {
        val violations = mutableListOf<String>()
        allowedDependencies.forEach { (path, allowed) ->
            val module = project(path)
            module.configurations.filterNot { it.name.startsWith("swiftPM") }.forEach { configuration ->
                configuration.dependencies.withType(ProjectDependency::class.java).forEach { dependency ->
                    val target = dependency.path
                    val isTestConfiguration = configuration.name.contains("test", ignoreCase = true)
                    val isAllowedMock = target == ":domainMock" && isTestConfiguration
                    if (target != path && target !in allowed && !isAllowedMock) {
                        violations += "$path (${configuration.name}) -> $target"
                    }
                }
            }
        }
        check(violations.isEmpty()) {
            "Forbidden module dependencies:\n" + violations.joinToString("\n")
        }
    }
}

tasks.register("sharedUnitTests") {
    group = "verification"
    description = "Runs the unit tests of all shared modules"
    dependsOn(checkModuleDependencies)
    dependsOn(sharedModules.map { "$it:testAndroidHostTest" })
}

tasks.named("check") {
    dependsOn(checkModuleDependencies)
}
