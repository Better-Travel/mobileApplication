# BetterTravel

AI walking-tour companion ("Nimbus") for Android and iOS. Kotlin Multiplatform + Compose Multiplatform.
Package root: `com.bettertravel`. Design reference PDFs: `~/Downloads/BetterTravel`.

Architecture notes: `docs/architecture/`.

## Stack
Decompose (navigation/lifecycle), Reaktive (Observable, BehaviorSubject, PublishSubject, BehaviorObservable),
Ktor client, SQLDelight, Kodein via `Deps`, kotlinx.serialization, Gradle Kotlin DSL + `gradle/libs.versions.toml`, JDK 17.

## Modules
| Module | Purpose | May depend on |
|---|---|---|
| `common` | Utilities, `Deps` service locator, rx helpers (`ActivityIndicator`, `shareReplayLatestUntil`), `PlatformConfig` expect/actual | nothing |
| `domain` | Models and repository/service interfaces with verb-style names | common |
| `domainMock` | Hand-written `...Mock` implementations, test-only | common, domain |
| `networking` | Ktor layer, `HttpBased...` implementations | common, domain |
| `database` | SQLDelight persistence implementing domain interfaces | common, domain |
| `localization`, `localization:generator` | EN/FR `strings.xml` (in `localization/strings/`) generated into `StringRepository` | common |
| `presentation` | ViewModels (Decompose + Reaktive), navigation. No Compose, Ktor or SQLDelight | common, domain, localization |
| `ui` | Compose Multiplatform screens | common, domain, presentation |
| `application` | Umbrella module, composition root (`DependencyInitializer`), iOS framework `Application` | all shared modules |
| `android:app` | Android entry point | application, ui, android:* |
| `android:application` | `Application` class, DI bootstrap | application, database, android:platform |
| `android:platform` | Android-specific bindings (e.g. DB driver) | common, database |
| `android:presentation` | Android glue for the root component | common, presentation |
| `mockServer` | Local Ktor server (`GET /routes` on :8080) | none |
| `uiTest` | Appium end-to-end skeleton | none |
| `iosApp` | Xcode project hosting the Compose UI | application framework |

## Dependency rules
Dependencies point inward to `domain`. `domain` knows nothing about Ktor, SQLDelight, Compose or Decompose.
`presentation` never depends on `ui`, `networking` or `database`. `ui` never depends on `networking` or `database`.
`domainMock` is only allowed in test configurations. `./gradlew checkModuleDependencies` enforces this
(allowed edges are listed in the root `build.gradle.kts`).

## Architecture
Reactive MVVM on Clean Architecture. For every screen:
- `XxxViewModel : ViewModel` in `presentation.<feature>`: state as `Observable`/`BehaviorObservable`, actions as functions,
  user-visible strings as properties, nested `data class Parameters(context: ViewModelContext, ...)`.
- `XxxViewModelImpl(parameters) : ScreenViewModel(context), XxxViewModel`: private subjects exposed read-only,
  collaborators via `Deps.instance()`, `trackActivity` and `shareReplayLatestUntil(closed)` for loading and lifecycle.
- A composable in `ui` that depends only on the interface. No business logic in composables.
- Navigation: Decompose child stack in `DefaultRootComponent`.
- ViewModels never reference the view.

## Naming and style
- Packages lowercase by feature (`domain.routes`, `networking.routes`).
- Domain interfaces: verb/gerund or `...Repository`; networking impls: `HttpBased` prefix; mocks: `Mock` suffix; VM impls: `Impl` suffix.
- Tests: methods `testThat_{condition}_{expectedBehavior}`, classes `{ClassUnderTest}Tests`.
- 4-space indent, explicit imports, no `!!`, no expression-body functions, no comments except TODOs or surprising workarounds.
- Every UI composable has a `@Preview`; UI uses `BetterTravelTheme` tokens and `designsystem` components (see `docs/architecture/design-system.md`).
- ktlint (`ktlint_official`) via `./gradlew ktlintCheck` / `ktlintFormat`.
- All user-facing strings live in `localization/strings/{en,fr}/strings.xml`; regenerate Kotlin with
  `./gradlew :localization:generator:generateKotlinFromXml`.

## Testing patterns
Shared tests live in `commonTest` and run as Android host (JVM) tests. Inject mocks through `Deps.di = DI { bindSingleton<X> { XMock() } }`
(see `RoutesListViewModelTests`). Networking tests use Ktor `MockEngine`.

## Commands
- `./gradlew sharedUnitTests`: run after any non-trivial change.
- `./gradlew build`: full Android + shared build with ktlint.
- `./gradlew :mockServer:run`: start the local server (Android emulator reaches it via 10.0.2.2, iOS via localhost).
- iOS: open `iosApp/iosApp.xcodeproj` (Gradle builds the `Application` framework in a build phase).
- Use JDK 17 (`JAVA_HOME`).
- Apple Silicon simulators and devices only (`iosArm64`, `iosSimulatorArm64`); `iosX64` is not supported by Kodein.
