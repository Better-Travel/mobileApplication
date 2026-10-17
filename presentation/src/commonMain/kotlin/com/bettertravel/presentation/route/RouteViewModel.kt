package com.bettertravel.presentation.route

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.subject.behavior.BehaviorObservable
import com.bettertravel.domain.chat.SuggestedRoute
import com.bettertravel.domain.map.GeoPoint
import com.bettertravel.domain.map.MapTile
import com.bettertravel.domain.routes.RouteStop
import com.bettertravel.presentation.ViewModel
import com.bettertravel.presentation.ViewModelContext

data class RouteSummary(
    val distance: String,
    val duration: String,
    val stopsCount: String,
)

data class RouteStopDetails(
    val order: Int,
    val name: String,
    val description: String,
    val duration: String,
    val rating: String,
    val reviews: String,
    val note: String?,
)

data class RouteSelectedStop(
    val order: Int,
    val name: String,
    val subtitle: String,
    val canDelete: Boolean,
)

interface RouteViewModel : ViewModel {
    val title: String
    val emptyMessage: String
    val errorMessage: String
    val retryTitle: String
    val loadingMessage: String
    val attribution: String

    val pillTitle: String
    val regenerateTitle: String
    val walkSuffix: String
    val stopsSuffix: String
    val totalTimeCaption: String
    val walkingCaption: String
    val curatedCaption: String
    val stopsCaption: String
    val sheetHint: String
    val startTitle: String
    val startNavigationTitle: String
    val sequenceTitle: String
    val walkableTitle: String
    val editOrderTitle: String
    val askTitle: String
    val askActionTitle: String
    val selectedTag: String
    val showDetailsTitle: String
    val changeTitle: String
    val deleteTitle: String

    val routeTitle: BehaviorObservable<String?>
    val summary: BehaviorObservable<RouteSummary?>
    val stops: BehaviorObservable<List<RouteStop>>
    val stopDetails: BehaviorObservable<List<RouteStopDetails>>
    val selectedStop: BehaviorObservable<RouteSelectedStop?>
    val path: BehaviorObservable<List<GeoPoint>>
    val isLoading: Observable<Boolean>
    val hasError: BehaviorObservable<Boolean>

    fun loadTile(tile: MapTile): Single<ByteArray>

    fun regenerate()

    fun start()

    fun askBetty()

    fun selectStop(order: Int)

    fun clearSelection()

    fun showDetails(order: Int)

    fun deleteSelectedStop()

    data class Parameters(
        val context: ViewModelContext,
        val route: BehaviorObservable<SuggestedRoute?>,
        val onStart: () -> Unit,
        val onAskBetty: () -> Unit,
        val onShowDetails: (StopDetailsRequest) -> Unit,
        val onDeleteStop: (Int) -> Unit,
    )
}
