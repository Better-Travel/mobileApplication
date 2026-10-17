plugins {
    id("com.android.library")
}

kotlin {
    jvmToolchain(17)
}

android {
    namespace = "com.bettertravel.android.presentation"
    compileSdk = 37
    defaultConfig {
        minSdk = 26
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    api(project(":common"))
    api(project(":presentation"))
    api(libs.decompose)
    api(libs.androidx.activity.compose)
}
