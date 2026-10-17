package com.bettertravel.presentation.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.subject.behavior.BehaviorObservable
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.bettertravel.common.Deps
import com.bettertravel.domain.chat.SuggestedRoute
import com.bettertravel.domain.routes.MIN_ROUTE_STOPS
import com.bettertravel.localization.StringRepository
import com.bettertravel.presentation.ViewModelContext
import com.bettertravel.presentation.aichat.AIChatViewModel
import com.bettertravel.presentation.aichat.AIChatViewModelImpl
import com.bettertravel.presentation.drawer.DrawerViewModel
import com.bettertravel.presentation.drawer.DrawerViewModelImpl
import com.bettertravel.presentation.memories.MemoriesViewModel
import com.bettertravel.presentation.memories.MemoriesViewModelImpl
import com.bettertravel.presentation.route.RouteViewModel
import com.bettertravel.presentation.route.RouteViewModelImpl
import com.bettertravel.presentation.route.StopDetailsRequest
import kotlinx.serialization.Serializable

class DefaultMainComponent(
    componentContext: ComponentContext,
    private val onStartRoute: () -> Unit,
    private val onShowStopDetails: (StopDetailsRequest) -> Unit,
) : MainComponent,
    ComponentContext by componentContext {
    private val strings: StringRepository = Deps.instance()
    private val navigation = StackNavigation<Config>()
    private val drawerSubject = BehaviorSubject(false)
    private val previewedRouteSubject = BehaviorSubject<SuggestedRoute?>(null)

    override val appTitle: String = strings.appName
    override val tabs: List<MainTab> = MainTab.entries
    override val drawerViewModel: DrawerViewModel =
        DrawerViewModelImpl(DrawerViewModel.Parameters(ViewModelContext(childContext(key = "drawer"))))
    override val isDrawerOpen: BehaviorObservable<Boolean> = drawerSubject

    override val children: Value<ChildStack<*, MainComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Discover,
            key = "main-tabs",
            handleBackButton = true,
            childFactory = ::createChild,
        )

    override fun tabTitle(tab: MainTab): String {
        return when (tab) {
            MainTab.Discover -> strings.tabDiscover
            MainTab.Route -> strings.tabRoute
            MainTab.Memories -> strings.tabMemories
        }
    }

    override fun selectTab(tab: MainTab) {
        val config =
            when (tab) {
                MainTab.Discover -> Config.Discover
                MainTab.Route -> Config.Route
                MainTab.Memories -> Config.Memories
            }
        navigation.bringToFront(config)
    }

    fun removeStop(index: Int) {
        val route = previewedRouteSubject.value ?: return
        if (route.stops.size <= MIN_ROUTE_STOPS || index !in route.stops.indices) {
            return
        }
        previewedRouteSubject.onNext(route.copy(stops = route.stops.filterIndexed { position, _ -> position != index }))
    }

    override fun openDrawer() {
        drawerSubject.onNext(true)
    }

    override fun closeDrawer() {
        drawerSubject.onNext(false)
    }

    private fun previewRoute(route: SuggestedRoute) {
        previewedRouteSubject.onNext(route)
        selectTab(MainTab.Route)
    }

    private fun createChild(
        config: Config,
        context: ComponentContext,
    ): MainComponent.Child {
        val viewModelContext = ViewModelContext(context)
        return when (config) {
            Config.Discover -> {
                val parameters =
                    AIChatViewModel.Parameters(
                        context = viewModelContext,
                        onPreviewRoute = { route -> previewRoute(route) },
                        onStartRoute = onStartRoute,
                    )
                MainComponent.Child.Discover(AIChatViewModelImpl(parameters))
            }

            Config.Route -> {
                val parameters =
                    RouteViewModel.Parameters(
                        context = viewModelContext,
                        route = previewedRouteSubject,
                        onStart = onStartRoute,
                        onAskBetty = { selectTab(MainTab.Discover) },
                        onShowDetails = onShowStopDetails,
                        onDeleteStop = { index -> removeStop(index) },
                    )
                MainComponent.Child.Route(RouteViewModelImpl(parameters))
            }

            Config.Memories -> {
                val parameters =
                    MemoriesViewModel.Parameters(
                        context = viewModelContext,
                        onRewalk = { route -> previewRoute(route) },
                        onOpenPlace = onShowStopDetails,
                    )
                MainComponent.Child.Memories(MemoriesViewModelImpl(parameters))
            }
        }
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Discover : Config

        @Serializable
        data object Route : Config

        @Serializable
        data object Memories : Config
    }
}
