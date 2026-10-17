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
import com.bettertravel.presentation.aichat.AIChatMode
import com.bettertravel.presentation.aichat.AIChatViewModel
import com.bettertravel.presentation.createTestComponentContext
import com.bettertravel.presentation.installTestDeps
import com.bettertravel.presentation.route.RouteViewModel
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MainComponentTests {
    @BeforeTest
    fun setUp() {
        installTestDeps()
    }

    private fun createComponent(): DefaultMainComponent {
        return DefaultMainComponent(
            componentContext = createTestComponentContext(),
            onStartRoute = { },
            onShowStopDetails = { },
        )
    }

    @Test
    fun testThat_componentIsCreated_discoverTabIsActive() {
        val component = createComponent()

        assertEquals(MainTab.Discover, component.children.value.active.instance.tab)
    }

    @Test
    fun testThat_tabIsSelected_selectedTabBecomesActive() {
        val component = createComponent()

        component.selectTab(MainTab.Memories)

        assertEquals(MainTab.Memories, component.children.value.active.instance.tab)
    }

    @Test
    fun testThat_tabsAreSwitchedBack_discoverIsActiveAgain() {
        val component = createComponent()

        component.selectTab(MainTab.Route)
        component.selectTab(MainTab.Discover)

        assertEquals(MainTab.Discover, component.children.value.active.instance.tab)
    }

    @Test
    fun testThat_drawerIsOpenedAndClosed_stateFollows() {
        val component = createComponent()
        assertFalse(component.isDrawerOpen.value)

        component.openDrawer()
        assertTrue(component.isDrawerOpen.value)

        component.closeDrawer()
        assertFalse(component.isDrawerOpen.value)
    }

    private fun createComponentWithPreviewedRoute(): DefaultMainComponent {
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
        val discover = DefaultMainComponent(createTestComponentContext(), onStartRoute = { }, onShowStopDetails = { })
        val chat = (discover.children.value.active.instance as MainComponent.Child.Discover).viewModel
        chat.inputViewModel.onTextChanged("plan a walk")
        chat.inputViewModel.send()
        val reply = chat.items.value.last() as AIChatViewModel.Item.AssistantMessage
        reply.viewModel.route
            ?.preview()
        return discover
    }

    private fun routeViewModel(component: DefaultMainComponent): RouteViewModel {
        return (component.children.value.active.instance as MainComponent.Child.Route).viewModel
    }

    @Test
    fun testThat_routeIsPreviewed_routeTabShowsItsStops() {
        val component = createComponentWithPreviewedRoute()

        assertEquals(MainTab.Route, component.children.value.active.instance.tab)
        assertEquals(3, routeViewModel(component).stops.value.size)
    }

    @Test
    fun testThat_stopIsRemoved_routeKeepsTheOtherStops() {
        val component = createComponentWithPreviewedRoute()

        component.removeStop(1)

        assertEquals(listOf("Stop 1", "Stop 3"), routeViewModel(component).stops.value.map { stop -> stop.name })
    }

    @Test
    fun testThat_routeHasMinimumStops_removalIsIgnored() {
        val component = createComponentWithPreviewedRoute()
        component.removeStop(0)

        component.removeStop(0)

        assertEquals(2, routeViewModel(component).stops.value.size)
    }

    @Test
    fun testThat_tabTitleIsRequested_titleIsLocalized() {
        val component = createComponent()

        assertEquals("Discover", component.tabTitle(MainTab.Discover))
        assertEquals("Route", component.tabTitle(MainTab.Route))
        assertEquals("Memories", component.tabTitle(MainTab.Memories))
    }

    @Test
    fun testThat_discoverTabIsActive_itHostsTheChat() {
        val component = createComponent()

        val discover = component.children.value.active.instance as MainComponent.Child.Discover

        assertEquals(AIChatMode.Text, discover.viewModel.mode.value)
        assertTrue(
            discover.viewModel.items.value
                .isEmpty(),
        )
    }
}
