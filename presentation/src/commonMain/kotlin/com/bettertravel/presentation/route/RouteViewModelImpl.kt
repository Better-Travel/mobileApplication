package com.bettertravel.presentation.route

import com.badoo.reaktive.disposable.SerialDisposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.subscribe
import com.badoo.reaktive.subject.behavior.BehaviorObservable
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.bettertravel.common.Deps
import com.bettertravel.common.rx.ActivityIndicator
import com.bettertravel.common.rx.trackActivity
import com.bettertravel.domain.chat.SuggestedRoute
import com.bettertravel.domain.map.GeoPoint
import com.bettertravel.domain.map.MapTile
import com.bettertravel.domain.map.MapTileLoading
import com.bettertravel.domain.routes.MIN_ROUTE_STOPS
import com.bettertravel.domain.routes.RouteStop
import com.bettertravel.domain.routes.WalkingRouting
import com.bettertravel.localization.StringRepository
import com.bettertravel.presentation.ScreenViewModel
import kotlin.math.roundToInt

class RouteViewModelImpl(
    private val parameters: RouteViewModel.Parameters,
) : ScreenViewModel(parameters.context),
    RouteViewModel {
    private val strings: StringRepository = Deps.instance()
    private val routing: WalkingRouting = Deps.instance()
    private val tiles: MapTileLoading = Deps.instance()

    private val activityIndicator = ActivityIndicator()
    private val pathRequest = SerialDisposable()

    private val routeTitleSubject = BehaviorSubject<String?>(null)
    private val summarySubject = BehaviorSubject<RouteSummary?>(null)
    private val stopDetailsSubject = BehaviorSubject<List<RouteStopDetails>>(emptyList())
    private val selectedIndexSubject = BehaviorSubject<Int?>(null)
    private val selectedStopSubject = BehaviorSubject<RouteSelectedStop?>(null)
    private val stopsSubject = BehaviorSubject<List<RouteStop>>(emptyList())
    private val pathSubject = BehaviorSubject<List<GeoPoint>>(emptyList())
    private val errorSubject = BehaviorSubject(false)

    override val title: String = strings.routeTitle
    override val emptyMessage: String = strings.routeEmptyMessage
    override val errorMessage: String = strings.routeError
    override val retryTitle: String = strings.routeRetry
    override val loadingMessage: String = strings.routeLoading
    override val attribution: String = tiles.attribution

    override val pillTitle: String = strings.routePillTitle
    override val regenerateTitle: String = strings.routeRegenerate
    override val walkSuffix: String = strings.routeWalkSuffix
    override val stopsSuffix: String = strings.routeStopsSuffix
    override val totalTimeCaption: String = strings.routeStatTotalTime
    override val walkingCaption: String = strings.routeStatWalking
    override val curatedCaption: String = strings.routeStatCurated
    override val stopsCaption: String = strings.routeStatStops
    override val sheetHint: String = strings.routeSheetHint
    override val startTitle: String = strings.routeStart
    override val startNavigationTitle: String = strings.routeStartNavigation
    override val sequenceTitle: String = strings.routeSequenceTitle
    override val walkableTitle: String = strings.routeWalkable
    override val editOrderTitle: String = strings.routeEditOrder
    override val askTitle: String = strings.routeAskTitle
    override val askActionTitle: String = strings.routeAskAction
    override val selectedTag: String = strings.routeSelectedTag
    override val showDetailsTitle: String = strings.routeShowDetails
    override val changeTitle: String = strings.routeChange
    override val deleteTitle: String = strings.routeDelete

    override val routeTitle: BehaviorObservable<String?> = routeTitleSubject
    override val summary: BehaviorObservable<RouteSummary?> = summarySubject
    override val stops: BehaviorObservable<List<RouteStop>> = stopsSubject
    override val selectedStop: BehaviorObservable<RouteSelectedStop?> = selectedStopSubject
    override val stopDetails: BehaviorObservable<List<RouteStopDetails>> = stopDetailsSubject
    override val path: BehaviorObservable<List<GeoPoint>> = pathSubject
    override val isLoading: Observable<Boolean> = activityIndicator.isActive
    override val hasError: BehaviorObservable<Boolean> = errorSubject

    init {
        parameters.route
            .subscribe(onNext = { route -> show(route) })
            .disposeWhenClosed()
        pathRequest.disposeWhenClosed()
    }

    override fun loadTile(tile: MapTile): Single<ByteArray> {
        return tiles.loadTile(tile)
    }

    override fun regenerate() {
        requestPath()
    }

    override fun start() {
        parameters.onStart()
    }

    override fun askBetty() {
        parameters.onAskBetty()
    }

    override fun selectStop(order: Int) {
        selectedIndexSubject.onNext(order - 1)
        updateSelection()
    }

    override fun clearSelection() {
        selectedIndexSubject.onNext(null)
        updateSelection()
    }

    override fun showDetails(order: Int) {
        val stop = stopsSubject.value.getOrNull(order - 1) ?: return
        parameters.onShowDetails(
            StopDetailsRequest(
                order = order,
                total = stopsSubject.value.size,
                name = stop.name,
                routeTitle = routeTitleSubject.value.orEmpty(),
                location = stop.location,
            ),
        )
    }

    override fun deleteSelectedStop() {
        val selected = selectedStopSubject.value ?: return
        if (selected.canDelete) {
            parameters.onDeleteStop(selected.order - 1)
        }
    }

    private fun show(route: SuggestedRoute?) {
        routeTitleSubject.onNext(route?.title)
        summarySubject.onNext(route?.let { suggested -> createSummary(suggested) })
        stopsSubject.onNext(route?.stops.orEmpty())
        selectedIndexSubject.onNext(null)
        updateSelection()
        stopDetailsSubject.onNext(route?.stops.orEmpty().mapIndexed { index, stop -> createDetails(index, stop) })
        pathSubject.onNext(emptyList())
        requestPath()
    }

    private fun updateSelection() {
        val index = selectedIndexSubject.value
        val stops = stopsSubject.value
        val stop = index?.let { stops.getOrNull(it) }
        if (index == null || stop == null) {
            selectedStopSubject.onNext(null)
            return
        }
        val subtitle =
            if (index == 0) {
                "${strings.routeStopLabel} 1 • ${strings.routeStartPoint}"
            } else {
                val meters = stops[index - 1].location.distanceMetersTo(stop.location)
                val kilometers = (meters / METERS_IN_KILOMETER * DECIMAL_SHIFT).roundToInt() / DECIMAL_SHIFT
                "${strings.routeStopLabel} ${index + 1} • $kilometers ${strings.unitKilometerShort} ${strings.routeFromPrevious}"
            }
        selectedStopSubject.onNext(
            RouteSelectedStop(
                order = index + 1,
                name = stop.name,
                subtitle = subtitle,
                canDelete = stops.size > MIN_ROUTE_STOPS,
            ),
        )
    }

    private fun createSummary(route: SuggestedRoute): RouteSummary {
        return RouteSummary(
            distance = "${route.distanceKm} ${strings.unitKilometerShort}",
            duration = formatDuration(route.durationMinutes),
            stopsCount = route.stops.size.toString(),
        )
    }

    private fun formatDuration(totalMinutes: Int): String {
        val hours = totalMinutes / MINUTES_IN_HOUR
        val minutes = totalMinutes % MINUTES_IN_HOUR
        if (hours == 0) {
            return "$minutes${strings.unitMinuteShort}"
        }
        return "$hours${strings.unitHourShort} $minutes${strings.unitMinuteShort}"
    }

    // TODO: replace the mock stop details with data from the server
    private fun createDetails(
        index: Int,
        stop: RouteStop,
    ): RouteStopDetails {
        val mock = MOCK_DETAILS[index % MOCK_DETAILS.size]
        return RouteStopDetails(
            order = index + 1,
            name = stop.name,
            description = mock.description,
            duration = mock.duration,
            rating = mock.rating,
            reviews = mock.reviews,
            note = mock.note,
        )
    }

    private fun requestPath() {
        val waypoints = stopsSubject.value.map { stop -> stop.location }
        errorSubject.onNext(false)
        if (waypoints.size < 2) {
            pathRequest.set(null)
            return
        }
        pathRequest.set(
            routing
                .walkingPath(waypoints)
                .trackActivity(activityIndicator)
                .subscribe(
                    onSuccess = { path -> pathSubject.onNext(path) },
                    onError = { errorSubject.onNext(true) },
                ),
        )
    }

    private companion object {
        const val MINUTES_IN_HOUR = 60
        const val METERS_IN_KILOMETER = 1000.0
        const val DECIMAL_SHIFT = 10.0

        val MOCK_DETAILS =
            listOf(
                MockDetails(
                    "Historic landmark, vibrant cafes & street artists",
                    "30 min",
                    "4.9",
                    "1.2k",
                    "Departure 10:00 AM",
                ),
                MockDetails(
                    "Gothic architecture & crypt chambers",
                    "25 min",
                    "4.8",
                    "850",
                    "7 min walk from the previous stop",
                ),
                MockDetails("Scenic viewpoint over the river & floral maze", "40 min", "4.9", "2.1k", "Free entry"),
            )
    }

    private class MockDetails(
        val description: String,
        val duration: String,
        val rating: String,
        val reviews: String,
        val note: String?,
    )
}
