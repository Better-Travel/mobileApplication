plugins {
    id("bettertravel.kmp-library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":common"))
            api(project(":domain"))
        }
    }
}
