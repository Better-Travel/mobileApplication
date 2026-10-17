package com.bettertravel.presentation.routes

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.single.subscribe
import com.badoo.reaktive.subject.behavior.BehaviorObservable
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.bettertravel.common.Deps
import com.bettertravel.common.rx.ActivityIndicator
import com.bettertravel.common.rx.shareReplayLatestUntil
import com.bettertravel.common.rx.trackActivity
import com.bettertravel.domain.routes.Route
import com.bettertravel.domain.routes.RoutesRepository
import com.bettertravel.localization.StringRepository
import com.bettertravel.presentation.ScreenViewModel

class RoutesListViewModelImpl(
    private val parameters: RoutesListViewModel.Parameters,
) : ScreenViewModel(parameters.context),
    RoutesListViewModel {
    private val repository: RoutesRepository = Deps.instance()
    private val strings: StringRepository = Deps.instance()

    private val activityIndicator = ActivityIndicator()
    private val routesSubject = BehaviorSubject<List<Route>>(emptyList())
    private val errorSubject = BehaviorSubject<String?>(null)

    override val title: String = strings.routesListTitle
    override val emptyMessage: String = strings.routesListEmpty
    override val retryTitle: String = strings.routesListRetry

    override val routes: BehaviorObservable<List<Route>> = routesSubject
    override val errorMessage: BehaviorObservable<String?> = errorSubject
    override val isLoading: Observable<Boolean> = activityIndicator.isActive.shareReplayLatestUntil(closed)

    init {
        reload()
    }

    override fun reload() {
        errorSubject.onNext(null)
        repository
            .loadRoutes()
            .trackActivity(activityIndicator)
            .subscribe(
                onSuccess = { loaded -> routesSubject.onNext(loaded) },
                onError = { errorSubject.onNext(strings.routesListError) },
            ).disposeWhenClosed()
    }

    override fun select(route: Route) {
        parameters.onRouteSelected(route)
    }
}
