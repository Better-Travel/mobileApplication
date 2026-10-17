# Reactive MVVM

Every screen has the same shape. The ViewModel never holds a reference to the view and there are no
`view.showX()` calls: the UI observes state and calls actions.

## ViewModel interface (`presentation.<feature>`)

```kotlin
interface RoutesListViewModel : ViewModel {
    val title: String                                    // user-visible strings, resolved from StringRepository
    val routes: BehaviorObservable<List<Route>>          // state as observables
    val isLoading: Observable<Boolean>
    val errorMessage: BehaviorObservable<String?>

    fun reload()                                         // user actions as plain functions
    fun select(route: Route)

    data class Parameters(val context: ViewModelContext, val onRouteSelected: (Route) -> Unit)
}
```

## Implementation (`XxxViewModelImpl`)

- Extends `ScreenViewModel(context)` and implements the interface.
- Private `BehaviorSubject` / `PublishSubject` backing fields, exposed as read-only observables.
- Collaborators come from `Deps.instance()`; only screen parameters go through the constructor.
- `trackActivity(ActivityIndicator)` drives loading state; `shareReplayLatestUntil(closed)` bounds sharing to the
  ViewModel lifecycle; `disposeWhenClosed()` disposes subscriptions.
- Navigation outcomes leave through callbacks in `Parameters` (the ViewModel does not navigate itself).

`ScreenViewModel` exposes `closed: Observable<Unit>`, which emits when the Decompose component is destroyed.

## Schedulers

ViewModels are scheduler-agnostic, which keeps them synchronously testable. The UI layer is responsible for
switching to the main thread: `ui.rx.collectAsState` subscribes with `observeOn(mainScheduler)`.

## Navigation (Decompose)

- `RootComponent` exposes `stack: Value<ChildStack<*, Child>>`; `DefaultRootComponent` owns the `StackNavigation`.
- Child ViewModels are created in `createChild` from a `ViewModelContext` wrapping the child `ComponentContext`.
- Configurations are `@Serializable` so the stack can be restored.
- Android: `ComponentActivity.createRootComponent()` uses `retainedComponent`.
  iOS: `MainViewController()` builds the component with a `LifecycleRegistry`.

## UI (`ui`)

- A composable depends only on the ViewModel interface, collects observables, renders state and calls actions.
- No business logic in composables. No dependency on `networking` or `database`.
- `RootContent(root)` renders the Decompose `Children` for the current stack.

## Adding a screen

1. Add the interface and `Impl` in `presentation.<feature>` with a `Parameters` class.
2. Add the strings to `localization/strings/{en,fr}/strings.xml` and run
   `./gradlew :localization:generator:generateKotlinFromXml`.
3. Add a `Config` and a `Child` to `RootComponent` / `DefaultRootComponent`.
4. Add the composable in `ui.<feature>` and handle the new `Child` in `RootContent`.
5. Add `{ViewModel}Tests` in `presentation/src/commonTest` and run `./gradlew sharedUnitTests`.
