package com.bettertravel.presentation.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.subject.behavior.BehaviorObservable
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.bettertravel.common.Deps
import com.bettertravel.localization.StringRepository
import com.bettertravel.presentation.ViewModelContext
import com.bettertravel.presentation.discover.DiscoverViewModel
import com.bettertravel.presentation.discover.DiscoverViewModelImpl
import com.bettertravel.presentation.memories.MemoriesViewModel
import com.bettertravel.presentation.memories.MemoriesViewModelImpl
import com.bettertravel.presentation.route.RouteViewModel
import com.bettertravel.presentation.route.RouteViewModelImpl
import kotlinx.serialization.Serializable

class DefaultMainComponent(
    componentContext: ComponentContext,
    private val onOpenChat: () -> Unit,
) : MainComponent,
    ComponentContext by componentContext {
    private val strings: StringRepository = Deps.instance()
    private val navigation = StackNavigation<Config>()
    private val drawerSubject = BehaviorSubject(false)

    override val appTitle: String = strings.appName
    override val tabs: List<MainTab> = MainTab.entries
    override val drawerTitle: String = strings.drawerTitle
    override val drawerMessage: String = strings.drawerMessage
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

    override fun openDrawer() {
        drawerSubject.onNext(true)
    }

    override fun closeDrawer() {
        drawerSubject.onNext(false)
    }

    private fun createChild(
        config: Config,
        context: ComponentContext,
    ): MainComponent.Child {
        val viewModelContext = ViewModelContext(context)
        return when (config) {
            Config.Discover -> {
                val parameters = DiscoverViewModel.Parameters(viewModelContext, onOpenChat)
                MainComponent.Child.Discover(DiscoverViewModelImpl(parameters))
            }

            Config.Route -> {
                val parameters = RouteViewModel.Parameters(viewModelContext)
                MainComponent.Child.Route(RouteViewModelImpl(parameters))
            }

            Config.Memories -> {
                val parameters = MemoriesViewModel.Parameters(viewModelContext)
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
