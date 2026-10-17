pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "BetterTravel"

include(":common")
include(":domain")
include(":domainMock")
include(":networking")
include(":database")
include(":localization")
include(":localization:generator")
include(":presentation")
include(":ui")
include(":application")
include(":android:app")
include(":android:application")
include(":android:platform")
include(":android:presentation")
include(":mockServer")
include(":uiTest")
