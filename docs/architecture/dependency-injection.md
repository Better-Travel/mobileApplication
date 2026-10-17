# Dependency injection

Kodein, used through a small service-locator object in `common`:

```kotlin
object Deps {
    var di: DI = DI {}
    inline fun <reified T : Any> instance(): T
}
```

## Composition root

`application` builds the container:

- `sharedModule()` binds `RoutesRepository` (`NetworkingFactory`), `RoutesStoring` (`SqlDelightRoutesStore`) and
  `StringRepository` (`GeneratedStringRepository`).
- The platform entry point supplies a platform module (the `DatabaseDriverFactory`).
- `DependencyInitializer.initialize(platformModule)` assigns `Deps.di`. It is called from `BetterTravelApplication`
  on Android and from `MainViewController()` on iOS.

The Ktor `HttpClient` is created inside `networking` and never leaves it, so `application` and `presentation` do not
need a Ktor dependency on their API surface.

## Rules

- ViewModels fetch collaborators with `Deps.instance()`; only screen parameters use constructors.
- Bind interfaces from `domain`, never implementations.
- Do not bind `domainMock` classes in production code.

## Testing

Replace the container per test:

```kotlin
Deps.di = DI {
    bindSingleton<RoutesRepository> { repository }
    bindSingleton<StringRepository> { GeneratedStringRepository { "en" } }
}
```

See `RoutesListViewModelTests`. Networking tests use Ktor `MockEngine` instead of the mock repository.
