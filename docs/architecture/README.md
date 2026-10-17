# BetterTravel architecture

BetterTravel is an AI walking-tour companion ("Nimbus") for Android and iOS, built with Kotlin Multiplatform and
Compose Multiplatform. Almost all code is shared; only the app entry points and a few platform bindings are native.

Reactive MVVM on top of Clean Architecture. Dependencies point inward to `domain`.

| Document | Content |
|---|---|
| [modules.md](modules.md) | Gradle modules, allowed dependencies, how the rule is enforced |
| [mvvm.md](mvvm.md) | ViewModel pattern, Reaktive usage, Decompose navigation, UI rules |
| [dependency-injection.md](dependency-injection.md) | `Deps` service locator, composition root, testing with mocks |
| [ai-chat.md](ai-chat.md) | AI chat and voice mode: layers, ViewModels, transitions |
| [route-flow.puml](route-flow.puml) | PlantUML class diagram of the route flow: chat suggestion, Route tab, map, stop details |
| [design-system.md](design-system.md) | Theme, atoms, molecules, organisms, icons, fonts and preview rules |
| [build-and-tooling.md](build-and-tooling.md) | Versions, targets, localization generator, ktlint, tests, running the apps |

## Layers at a glance

```
            android:app            iosApp (Xcode)
                 |                      |
                 +-------> application <+        composition root, iOS framework "Application"
                              |
        +---------+-----------+----------+
        |         |           |          |
       ui    networking    database      |
        |         |           |          |
   presentation   +-----+-----+          |
        |               |                |
        +-----> domain <+                |
                  |                      |
               common <------------------+
```

`localization` sits next to `domain` (depends only on `common`) and is consumed by `presentation`.

## Vertical slice: routes list

`RoutesList` proves the architecture end to end:

1. `domain.routes`: `Route` model, `RoutesRepository` and `RoutesStoring` interfaces.
2. `networking.routes`: `HttpBasedRoutesRepository` (Ktor) calling `GET /routes` on the local `mockServer`.
3. `database`: `SqlDelightRoutesStore` implementing `RoutesStoring`.
4. `domainMock`: `RoutesRepositoryMock`.
5. `presentation.routes`: `RoutesListViewModel` and `RoutesListViewModelImpl`, created by `DefaultRootComponent`.
6. `ui.routes`: `RoutesListScreen`.
7. `localization`: title, empty, error and retry strings in EN and FR.

Planned features from the design (not built yet): AI chat, live voice mode, route map and editing, stop details,
live navigation, route completion and XP, memories, profile and side navigation.
