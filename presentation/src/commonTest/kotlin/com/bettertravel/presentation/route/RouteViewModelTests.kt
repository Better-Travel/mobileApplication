package com.bettertravel.presentation.route

import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.bettertravel.common.Deps
import com.bettertravel.domain.chat.SuggestedRoute
import com.bettertravel.domain.map.GeoPoint
import com.bettertravel.domain.map.MapTileLoading
import com.bettertravel.domain.routes.RouteStop
import com.bettertravel.domain.routes.WalkingRouting
import com.bettertravel.domainmock.map.MapTileLoadingMock
import com.bettertravel.domainmock.routes.WalkingRoutingMock
import com.bettertravel.localization.GeneratedStringRepository
import com.bettertravel.localization.StringRepository
import com.bettertravel.presentation.ViewModelContext
import com.bettertravel.presentation.createTestComponentContext
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RouteViewModelTests {
    private val routing = WalkingRoutingMock()
    private val tiles = MapTileLoadingMock()
    private var startCount = 0
    private var askCount = 0
    private val detailsRequests = mutableListOf<StopDetailsRequest>()
    private val deletedIndexes = mutableListOf<Int>()
    private val routeSubject = BehaviorSubject<SuggestedRoute?>(null)

    private val market = RouteStop("Market Square", GeoPoint(52.2497, 21.0122))
    private val cathedral = RouteStop("St. John's", GeoPoint(52.2490, 21.0137))
    private val route = SuggestedRoute("Old Town", 3.2, 165, listOf(market, cathedral))

    @BeforeTest
    fun setUp() {
        Deps.di =
            DI {
                bindSingleton<WalkingRouting> { routing }
                bindSingleton<MapTileLoading> { tiles }
                bindSingleton<StringRepository> { GeneratedStringRepository { "en" } }
            }
    }

    private fun createViewModel(): RouteViewModelImpl {
        val parameters =
            RouteViewModel.Parameters(
                context = ViewModelContext(createTestComponentContext()),
                route = routeSubject,
                onStart = { startCount += 1 },
                onAskBetty = { askCount += 1 },
                onShowDetails = { request -> detailsRequests.add(request) },
                onDeleteStop = { index -> deletedIndexes.add(index) },
            )
        return RouteViewModelImpl(parameters)
    }

    @Test
    fun testThat_noRouteIsPreviewed_mapStateIsEmpty() {
        val viewModel = createViewModel()

        assertNull(viewModel.routeTitle.value)
        assertTrue(viewModel.stops.value.isEmpty())
        assertTrue(viewModel.path.value.isEmpty())
    }

    @Test
    fun testThat_routeIsPreviewed_stopsAndPathAreExposed() {
        val viewModel = createViewModel()
        val path = listOf(market.location, GeoPoint(52.2493, 21.0130), cathedral.location)
        routing.result = Result.success(path)

        routeSubject.onNext(route)

        assertEquals("Old Town", viewModel.routeTitle.value)
        assertEquals(listOf(market, cathedral), viewModel.stops.value)
        assertEquals(path, viewModel.path.value)
        assertEquals(listOf(listOf(market.location, cathedral.location)), routing.receivedWaypoints)
    }

    @Test
    fun testThat_routingFails_errorIsExposedAndRegenerateRecovers() {
        val viewModel = createViewModel()
        routing.result = Result.failure(IllegalStateException("offline"))
        routeSubject.onNext(route)
        assertTrue(viewModel.hasError.value)

        routing.result = Result.success(listOf(market.location, cathedral.location))
        viewModel.regenerate()

        assertFalse(viewModel.hasError.value)
        assertEquals(2, viewModel.path.value.size)
    }

    @Test
    fun testThat_routeIsPreviewed_summaryAndStopDetailsAreFormatted() {
        val viewModel = createViewModel()

        routeSubject.onNext(route)

        assertEquals(RouteSummary(distance = "3.2 km", duration = "2h 45m", stopsCount = "2"), viewModel.summary.value)
        assertEquals(listOf(1, 2), viewModel.stopDetails.value.map { details -> details.order })
        assertEquals(
            "Market Square",
            viewModel.stopDetails.value
                .first()
                .name,
        )
    }

    @Test
    fun testThat_startAndAskAreTapped_callbacksAreInvoked() {
        val viewModel = createViewModel()

        viewModel.start()
        viewModel.askBetty()

        assertEquals(1, startCount)
        assertEquals(1, askCount)
    }

    @Test
    fun testThat_stopIsSelected_selectionDescribesDistanceFromPreviousStop() {
        val viewModel = createViewModel()
        routeSubject.onNext(route)

        viewModel.selectStop(2)

        val selected = viewModel.selectedStop.value
        assertEquals("St. John's", selected?.name)
        assertTrue(selected?.subtitle?.startsWith("Stop 2 • 0.1 km") == true)
    }

    @Test
    fun testThat_selectionIsCleared_noStopIsSelected() {
        val viewModel = createViewModel()
        routeSubject.onNext(route)
        viewModel.selectStop(1)

        viewModel.clearSelection()

        assertNull(viewModel.selectedStop.value)
    }

    @Test
    fun testThat_routeHasOnlyMinimumStops_deleteDoesNothing() {
        val viewModel = createViewModel()
        routeSubject.onNext(route)
        viewModel.selectStop(1)

        viewModel.deleteSelectedStop()

        assertFalse(viewModel.selectedStop.value?.canDelete == true)
        assertTrue(deletedIndexes.isEmpty())
    }

    @Test
    fun testThat_routeHasMoreStops_deleteReportsTheSelectedIndex() {
        val viewModel = createViewModel()
        val third = RouteStop("Freta", GeoPoint(52.2531, 21.0104))
        routeSubject.onNext(route.copy(stops = route.stops + third))
        viewModel.selectStop(2)

        viewModel.deleteSelectedStop()

        assertEquals(listOf(1), deletedIndexes)
    }

    @Test
    fun testThat_detailsAreRequested_requestCarriesStopAndRouteInfo() {
        val viewModel = createViewModel()
        routeSubject.onNext(route)

        viewModel.showDetails(2)

        assertEquals(StopDetailsRequest(2, 2, "St. John's", "Old Town", cathedral.location), detailsRequests.single())
    }
}
