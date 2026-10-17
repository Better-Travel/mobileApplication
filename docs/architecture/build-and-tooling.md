# Build and tooling

## Versions

Defined in `gradle/libs.versions.toml`; Gradle wrapper 9.8.1; JDK 17 toolchain.
Kotlin 2.4.21, Compose Multiplatform 1.12.2, AGP 9.4.1, Decompose 3.5.0, Reaktive 2.4.0, Ktor 3.6.0,
SQLDelight 2.4.1, Kodein 7.33.0, kotlinx.serialization 1.11.0.

AGP 9 is required because current AndroidX and OkHttp artifacts need `compileSdk` 37. Consequently:

- Shared modules use the `com.android.kotlin.multiplatform.library` plugin through the convention plugin
  `buildSrc/.../bettertravel.kmp-library.gradle.kts` (targets, namespace, `compileSdk`, host tests).
- Android-only modules apply `com.android.library` / `com.android.application` without the Kotlin Android plugin
  (Kotlin support is built in).
- Shared unit tests are Android host tests (`testAndroidHostTest`).

## Commands

| Command | Purpose |
|---|---|
| `./gradlew sharedUnitTests` | Dependency check plus unit tests of all shared modules; run after non-trivial changes |
| `./gradlew build` | Full build including ktlint |
| `./gradlew checkModuleDependencies` | Enforce the module dependency rules |
| `./gradlew ktlintCheck` / `ktlintFormat` | Style |
| `./gradlew :localization:generator:generateKotlinFromXml` | Regenerate `StringRepository.kt` |
| `./gradlew :mockServer:run` | Start the local server on port 8080 |
| `./gradlew :application:linkDebugFrameworkIosSimulatorArm64` | Build the iOS framework |

Android emulators reach the host at `10.0.2.2`, iOS simulators at `localhost`. Both are set in `PlatformConfig`.
Cleartext HTTP is enabled on Android and local networking is allowed on iOS for development only.

## Localization

`localization/strings/en/strings.xml` is the default and `fr/strings.xml` the translation. The generator in
`localization:generator` parses both and writes `StringRepository` and `GeneratedStringRepository`
(language chosen at runtime from `PlatformConfig.languageCode`, falling back to English). The generated file is committed,
so regenerate it after editing any `strings.xml`.

## Style

ktlint with `ktlint_official` (`.editorconfig`): 4-space indent, explicit imports, one argument per line when wrapping,
no `!!`, no expression-body functions, no comments except TODOs or surprising workarounds. Generated sources under
`build/` are excluded.

## Testing conventions

- Methods: `testThat_{condition}_{expectedBehavior}`; classes: `{ClassUnderTest}Tests`.
- Shared tests live in `commonTest`.
- Hand-written mocks live in `domainMock` and are used from tests only.

## iOS

`iosApp/iosApp.xcodeproj` has a "Compile Kotlin Framework" build phase running
`./gradlew :application:embedAndSignAppleFrameworkForXcode`. The Swift code only embeds
`MainViewControllerKt.MainViewController()` in a SwiftUI view.
