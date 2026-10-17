plugins {
    id("org.jetbrains.kotlin.jvm")
}

kotlin {
    jvmToolchain(17)
}

val stringsDir = rootProject.layout.projectDirectory.dir("localization/strings")
val outputFile =
    rootProject.layout.projectDirectory.file(
        "localization/src/commonMain/kotlin/com/bettertravel/localization/StringRepository.kt",
    )

tasks.register<JavaExec>("generateKotlinFromXml") {
    group = "localization"
    description = "Generates StringRepository.kt from the strings.xml files"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("com.bettertravel.localization.generator.MainKt")
    args(stringsDir.asFile.absolutePath, outputFile.asFile.absolutePath)
    inputs.dir(stringsDir)
    outputs.file(outputFile)
}
