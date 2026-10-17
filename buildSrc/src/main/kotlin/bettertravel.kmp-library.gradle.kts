import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.kotlin.multiplatform.library")
}

val moduleSuffix = project.path.removePrefix(":").replace(":", ".").replace("-", "")

extensions.configure<KotlinMultiplatformExtension> {
    jvmToolchain(17)
    android {
        namespace = "com.bettertravel.$moduleSuffix"
        compileSdk = 37
        minSdk = 26
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
        withHostTest { }
    }
    iosArm64()
    iosSimulatorArm64()
    sourceSets.commonTest.dependencies {
        implementation(kotlin("test"))
    }
}
