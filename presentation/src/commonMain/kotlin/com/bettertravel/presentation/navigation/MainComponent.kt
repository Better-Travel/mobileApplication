package com.bettertravel.presentation.navigation

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.subject.behavior.BehaviorObservable
import com.bettertravel.presentation.aichat.AIChatViewModel
import com.bettertravel.presentation.drawer.DrawerViewModel
import com.bettertravel.presentation.memories.MemoriesViewModel
import com.bettertravel.presentation.route.RouteViewModel

enum class MainTab {
    Discover,
    Route,
    Memories,
}

interface MainComponent {
    val appTitle: String
    val tabs: List<MainTab>
    val children: Value<ChildStack<*, Child>>

    val isDrawerOpen: BehaviorObservable<Boolean>
    val drawerViewModel: DrawerViewModel

    fun tabTitle(tab: MainTab): String

    fun selectTab(tab: MainTab)

    fun openDrawer()

    fun closeDrawer()

    sealed interface Child {
        val tab: MainTab

        class Discover(
            val viewModel: AIChatViewModel,
        ) : Child {
            override val tab: MainTab = MainTab.Discover
        }

        class Route(
            val viewModel: RouteViewModel,
        ) : Child {
            override val tab: MainTab = MainTab.Route
        }

        class Memories(
            val viewModel: MemoriesViewModel,
        ) : Child {
            override val tab: MainTab = MainTab.Memories
        }
    }
}
