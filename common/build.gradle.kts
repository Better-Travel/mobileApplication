plugins {
    id("bettertravel.kmp-library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.reaktive)
            api(libs.kodein)
            api(libs.kotlinx.coroutines.core)
        }
        commonTest.dependencies {
            implementation(libs.reaktive.testing)
        }
    }
}
