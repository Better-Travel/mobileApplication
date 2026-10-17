plugins {
    id("bettertravel.kmp-library")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":common"))
            api(project(":domain"))
            api(project(":localization"))
            api(libs.decompose)
            implementation(libs.kotlinx.serialization.json)
        }
        commonTest.dependencies {
            implementation(project(":domainMock"))
            implementation(libs.reaktive.testing)
        }
    }
}
