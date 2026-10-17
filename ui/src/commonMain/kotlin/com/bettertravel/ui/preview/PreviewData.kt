package com.bettertravel.ui.preview

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.observableOf
import com.badoo.reaktive.subject.behavior.BehaviorObservable
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.bettertravel.domain.routes.Route
import com.bettertravel.presentation.navigation.RootComponent
import com.bettertravel.presentation.routes.RoutesListViewModel

internal val sampleRoutes: List<Route> =
    listOf(
        Route("1", "Old Town Loop", "Warsaw", 3.4, 112, 4),
        Route("2", "Castle District Walk", "Prague", 4.1, 140, 5),
    )

internal class PreviewRoutesListViewModel : RoutesListViewModel {
    override val title: String = "Your routes"
    override val emptyMessage: String = "No routes yet"
    override val retryTitle: String = "Try again"
    override val routes: BehaviorObservable<List<Route>> = BehaviorSubject(sampleRoutes)
    override val isLoading: Observable<Boolean> = observableOf(false)
    override val errorMessage: BehaviorObservable<String?> = BehaviorSubject(null)

    override fun reload() {
    }

    override fun select(route: Route) {
    }
}

internal class PreviewRootComponent : RootComponent {
    override val stack: Value<ChildStack<*, RootComponent.Child>> =
        MutableValue(
            ChildStack(
                configuration = "routes",
                instance = RootComponent.Child.RoutesList(PreviewRoutesListViewModel()),
            ),
        )
}
