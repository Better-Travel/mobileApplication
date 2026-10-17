package com.bettertravel.presentation.navigation

import com.bettertravel.common.Deps
import com.bettertravel.domain.chat.AIChatting
import com.bettertravel.domain.chat.ChatReply
import com.bettertravel.domain.chat.SuggestedRoute
import com.bettertravel.domain.map.GeoPoint
import com.bettertravel.domain.map.MapTileLoading
import com.bettertravel.domain.places.PlaceBookmarking
import com.bettertravel.domain.routes.RouteStop
import com.bettertravel.domain.routes.WalkingRouting
import com.bettertravel.domainmock.chat.AIChattingMock
import com.bettertravel.domainmock.map.MapTileLoadingMock
import com.bettertravel.domainmock.places.PlaceBookmarkingMock
import com.bettertravel.domainmock.routes.WalkingRoutingMock
import com.bettertravel.localization.GeneratedStringRepository
import com.bettertravel.localization.StringRepository
import com.bettertravel.presentation.aichat.AIChatViewModel
import com.bettertravel.presentation.createTestComponentContext
import com.bettertravel.presentation.installTestDeps
import com.bettertravel.presentation.route.RouteViewModel
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RootComponentTests {
    @BeforeTest
    fun setUp() {
        installTestDeps()
    }

    @Test
    fun testThat_rootIsCreated_mainIsTheOnlyDestination() {
        val root = DefaultRootComponent(createTestComponentContext())

        assertTrue(root.stack.value.active.instance is RootComponent.Child.Main)
        assertEquals(1, root.stack.value.items.size)
    }

    @Test
    fun testThat_rootIsCreated_discoverTabIsActive() {
        val root = DefaultRootComponent(createTestComponentContext())
        val main = root.stack.value.active.instance as RootComponent.Child.Main

        assertEquals(MainTab.Discover, main.component.children.value.active.instance.tab)
    }

    private fun createRootWithPreviewedRoute(): DefaultRootComponent {
        val stops = (1..3).map { index -> RouteStop("Stop $index", GeoPoint(52.0 + index / 1000.0, 21.0)) }
        val chatting = AIChattingMock(Result.success(ChatReply("Here you go", SuggestedRoute("Route", 1.0, 60, stops))))
        Deps.di =
            DI {
                bindSingleton<AIChatting> { chatting }
                bindSingleton<WalkingRouting> { WalkingRoutingMock() }
                bindSingleton<PlaceBookmarking> { PlaceBookmarkingMock() }
                bindSingleton<MapTileLoading> { MapTileLoadingMock() }
                bindSingleton<StringRepository> { GeneratedStringRepository { "en" } }
            }
        val root = DefaultRootComponent(createTestComponentContext())
        val main = (root.stack.value.active.instance as RootComponent.Child.Main).component
        val chat = (main.children.value.active.instance as MainComponent.Child.Discover).viewModel
        chat.inputViewModel.onTextChanged("plan a walk")
        chat.inputViewModel.send()
        (chat.items.value.last() as AIChatViewModel.Item.AssistantMessage).viewModel.route?.preview()
        return root
    }

    private fun routeViewModel(root: DefaultRootComponent): RouteViewModel {
        val main =
            (
                root.stack.value.items
                    .first()
                    .instance as RootComponent.Child.Main
            ).component
        return (main.children.value.active.instance as MainComponent.Child.Route).viewModel
    }

    @Test
    fun testThat_detailsAreRequestedFromTheRoute_detailsScreenIsPushed() {
        val root = createRootWithPreviewedRoute()

        routeViewModel(root).showDetails(2)

        val details = root.stack.value.active.instance as RootComponent.Child.StopDetails
        assertEquals("Stop 2", details.viewModel.name)
        assertEquals(2, root.stack.value.items.size)
    }

    @Test
    fun testThat_detailsAreClosed_routeIsTheActiveDestinationAgain() {
        val root = createRootWithPreviewedRoute()
        routeViewModel(root).showDetails(2)

        (root.stack.value.active.instance as RootComponent.Child.StopDetails).viewModel.back()

        assertTrue(root.stack.value.active.instance is RootComponent.Child.Main)
    }

    @Test
    fun testThat_stopIsRemovedFromTheDetails_screenClosesAndRouteLosesTheStop() {
        val root = createRootWithPreviewedRoute()
        routeViewModel(root).showDetails(2)

        (root.stack.value.active.instance as RootComponent.Child.StopDetails).viewModel.remove()

        assertTrue(root.stack.value.active.instance is RootComponent.Child.Main)
        assertEquals(listOf("Stop 1", "Stop 3"), routeViewModel(root).stops.value.map { stop -> stop.name })
    }

    @Test
    fun testThat_swapIsTappedOnTheDetails_discoverTabIsOpened() {
        val root = createRootWithPreviewedRoute()
        routeViewModel(root).showDetails(1)

        (root.stack.value.active.instance as RootComponent.Child.StopDetails).viewModel.swap()

        val main = (root.stack.value.active.instance as RootComponent.Child.Main).component
        assertEquals(MainTab.Discover, main.children.value.active.instance.tab)
    }
}
