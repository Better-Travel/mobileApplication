plugins {
    id("com.android.library")
}

kotlin {
    jvmToolchain(17)
}

android {
    namespace = "com.bettertravel.android.application"
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
    implementation(project(":application"))
    implementation(project(":database"))
    implementation(project(":android:platform"))
}
