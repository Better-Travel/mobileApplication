import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("bettertravel.kmp-library")
    alias(libs.plugins.kotlin.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    targets.withType<KotlinNativeTarget>().configureEach {
        binaries.framework {
            baseName = "Application"
            isStatic = true
            linkerOpts("-lsqlite3")
            export(project(":common"))
            export(project(":domain"))
            export(project(":localization"))
            export(project(":presentation"))
            export(project(":ui"))
            export(libs.decompose)
        }
    }
    sourceSets {
        commonMain.dependencies {
            api(project(":common"))
            api(project(":domain"))
            api(project(":networking"))
            api(project(":database"))
            api(project(":localization"))
            api(project(":presentation"))
            api(project(":ui"))
            api(libs.decompose)
            implementation(compose.runtime)
            implementation(compose.ui)
        }
    }
}
