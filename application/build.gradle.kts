import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import java.util.Properties

plugins {
    id("bettertravel.kmp-library")
    alias(libs.plugins.kotlin.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
}

val generateBuildSecrets by tasks.registering {
    val outputDir = layout.buildDirectory.dir("generated/secrets/kotlin")
    val propertiesFile = rootProject.file("local.properties")
    val environmentKey = providers.environmentVariable("CARTO_API_KEY").orElse("")
    val environmentSpeechKey = providers.environmentVariable("GOOGLE_TTS_API_KEY").orElse("")
    inputs.property("environmentKey", environmentKey)
    inputs.property("environmentSpeechKey", environmentSpeechKey)
    if (propertiesFile.exists()) {
        inputs.file(propertiesFile)
    }
    outputs.dir(outputDir)
    doLast {
        val properties = Properties()
        if (propertiesFile.exists()) {
            propertiesFile.inputStream().use { stream -> properties.load(stream) }
        }

        fun escaped(value: String): String {
            return value
                .trim()
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("$", "\\$")
        }
        val cartoKey = escaped(properties.getProperty("carto.apiKey") ?: environmentKey.get())
        val speechKey = escaped(properties.getProperty("google.tts.apiKey") ?: environmentSpeechKey.get())
        val file = outputDir.get().file("com/bettertravel/application/BuildSecrets.kt").asFile
        file.parentFile.mkdirs()
        file.writeText(
            "package com.bettertravel.application\n\n" +
                "internal object BuildSecrets {\n" +
                "    const val CARTO_API_KEY: String = \"$cartoKey\"\n" +
                "    const val GOOGLE_TTS_API_KEY: String = \"$speechKey\"\n" +
                "}\n",
        )
    }
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
        commonMain {
            kotlin.srcDir(generateBuildSecrets)
        }
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
