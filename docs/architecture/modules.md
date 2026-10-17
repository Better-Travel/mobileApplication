# Modules

| Module | Purpose | May depend on |
|---|---|---|
| `common` | Utilities, `Deps` service locator, rx helpers (`ActivityIndicator`, `shareReplayLatestUntil`), `PlatformConfig` expect/actual | nothing |
| `domain` | Models and repository/service interfaces with verb-style names | common |
| `domainMock` | Hand-written `...Mock` implementations of domain interfaces; test-only | common, domain |
| `networking` | Ktor HTTP layer; implementations prefixed `HttpBased...` | common, domain |
| `database` | SQLDelight persistence implementing domain interfaces | common, domain |
| `localization`, `localization:generator` | EN/FR `strings.xml` and the generator for `StringRepository` | common |
| `presentation` | ViewModels (Decompose + Reaktive) and navigation. No Compose, Ktor or SQLDelight | common, domain, localization |
| `ui` | Compose Multiplatform screens and components | common, domain, presentation |
| `application` | Umbrella module and composition root; builds the iOS framework | all shared modules |
| `android:app` | Android entry point (`MainActivity`, manifest) | application, ui, android:application, android:presentation |
| `android:application` | `Application` class, DI bootstrap | application, database, android:platform |
| `android:platform` | Android-specific bindings (e.g. database driver) | common, database |
| `android:presentation` | Android glue creating the retained root component | common, presentation |
| `mockServer` | Local Ktor server, `GET /routes` on port 8080 | none |
| `uiTest` | Appium end-to-end test skeleton | none |
| `iosApp` | Xcode project hosting the Compose UI | `application` framework |

## Rules

- `domain` knows nothing about Ktor, SQLDelight, Compose or Decompose.
- `presentation` never depends on `ui`, `networking` or `database`.
- `ui` never depends on `networking` or `database`.
- `domainMock` is only allowed in test configurations.

`./gradlew checkModuleDependencies` fails the build on any other edge. The allowed edges live in the root
`build.gradle.kts` (`allowedDependencies`). It runs as part of `check` and `sharedUnitTests`. When adding a legitimate
new edge, update that map and this table.

## Why `application` is the umbrella

It `api`-depends on and exports the shared modules, and it owns the composition root (`DependencyInitializer`,
`sharedModule()`). It is the only module that knows both abstractions (`domain`) and implementations (`networking`,
`database`), so it is the only place where they are wired together. On iOS it produces the static framework
`Application`, whose `MainViewController()` hosts the Compose UI.

## Platform differences

- Shared modules target `android`, `iosArm64` and `iosSimulatorArm64`. `iosX64` is not supported (Kodein no longer publishes it).
- Platform-specific values go through `expect object PlatformConfig` in `common` (mock server host, language code).
- Platform-specific bindings (database driver) are provided as a Kodein module by the platform entry point:
  `androidPlatformModule(context)` on Android, an inline module in `MainViewController` on iOS.
