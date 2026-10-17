plugins {
    id("com.android.application")
    alias(libs.plugins.kotlin.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    jvmToolchain(17)
}

android {
    namespace = "com.bettertravel.android.app"
    compileSdk = 37
    defaultConfig {
        applicationId = "com.bettertravel"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(project(":application"))
    implementation(project(":ui"))
    implementation(project(":android:application"))
    implementation(project(":android:presentation"))
    implementation(compose.runtime)
    implementation(libs.androidx.activity.compose)
    debugImplementation(compose.uiTooling)
}
