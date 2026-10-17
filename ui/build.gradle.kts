plugins {
    id("bettertravel.kmp-library")
    alias(libs.plugins.kotlin.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    android {
        androidResources {
            enable = true
        }
    }
    sourceSets {
        commonMain.dependencies {
            api(project(":common"))
            api(project(":domain"))
            api(project(":presentation"))
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.decompose.compose)
        }
    }
}

compose {
    resources {
        packageOfResClass = "com.bettertravel.ui.resources"
        generateResClass = always
    }
}

dependencies {
    androidRuntimeClasspath(compose.uiTooling)
}
