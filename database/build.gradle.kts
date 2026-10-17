plugins {
    id("bettertravel.kmp-library")
    alias(libs.plugins.sqldelight)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":common"))
            api(project(":domain"))
            implementation(libs.sqldelight.runtime)
            implementation(libs.reaktive.coroutines.interop)
        }
        androidMain.dependencies {
            implementation(libs.sqldelight.android.driver)
        }
        iosMain.dependencies {
            implementation(libs.sqldelight.native.driver)
        }
    }
}

sqldelight {
    databases {
        create("BetterTravelDatabase") {
            packageName.set("com.bettertravel.database")
        }
    }
}
