package com.bettertravel.presentation.routes

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.subject.behavior.BehaviorObservable
import com.bettertravel.domain.routes.Route
import com.bettertravel.presentation.ViewModel
import com.bettertravel.presentation.ViewModelContext

interface RoutesListViewModel : ViewModel {
    val title: String
    val emptyMessage: String
    val retryTitle: String

    val routes: BehaviorObservable<List<Route>>
    val isLoading: Observable<Boolean>
    val errorMessage: BehaviorObservable<String?>

    fun reload()

    fun select(route: Route)

    data class Parameters(
        val context: ViewModelContext,
        val onRouteSelected: (Route) -> Unit,
    )
}
