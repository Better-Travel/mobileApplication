package com.bettertravel.ui.preview

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.observableOf
import com.badoo.reaktive.subject.behavior.BehaviorObservable
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.bettertravel.domain.routes.Route
import com.bettertravel.presentation.chat.ChatViewModel
import com.bettertravel.presentation.discover.DiscoverViewModel
import com.bettertravel.presentation.memories.MemoriesViewModel
import com.bettertravel.presentation.navigation.MainComponent
import com.bettertravel.presentation.navigation.MainTab
import com.bettertravel.presentation.navigation.RootComponent
import com.bettertravel.presentation.route.RouteViewModel
import com.bettertravel.presentation.routes.RoutesListViewModel

internal val sampleRoutes: List<Route> =
    listOf(
        Route("1", "Old Town Loop", "Warsaw", 3.4, 112, 4),
        Route("2", "Castle District Walk", "Prague", 4.1, 140, 5),
    )

internal class PreviewRoutesListViewModel : RoutesListViewModel {
    override val title: String = "Your routes"
    override val emptyMessage: String = "No routes yet"
    override val retryTitle: String = "Try again"
    override val routes: BehaviorObservable<List<Route>> = BehaviorSubject(sampleRoutes)
    override val isLoading: Observable<Boolean> = observableOf(false)
    override val errorMessage: BehaviorObservable<String?> = BehaviorSubject(null)

    override fun reload() {
    }

    override fun select(route: Route) {
    }
}

internal class PreviewDiscoverViewModel : DiscoverViewModel {
    override val titlePrefix: String = "Let's plan your trip"
    override val titleAccent: String = "Together"
    override val subtitle: String = "Meet Betty, your pocket-sized explorer ready to curate custom journeys in seconds."
    override val inputPlaceholder: String = "Tell BetterTravel where & how long..."

    override fun openChat() {
    }
}

internal class PreviewChatViewModel : ChatViewModel {
    override val title: String = "BetterTravel"
    override val status: String = "with Betty"
    override val emptyMessage: String = "Tell Betty where you are and how long you have. Chat is coming soon."

    override fun back() {
    }
}

internal class PreviewRouteViewModel : RouteViewModel {
    override val title: String = "Route"
    override val emptyMessage: String = "Your route will appear here once Betty has planned it."
}

internal class PreviewMemoriesViewModel : MemoriesViewModel {
    override val title: String = "Memories"
    override val emptyMessage: String = "Your saved walks will appear here."
}

internal class PreviewMainComponent : MainComponent {
    override val appTitle: String = "BetterTravel"
    override val tabs: List<MainTab> = MainTab.entries
    override val children: Value<ChildStack<*, MainComponent.Child>> =
        MutableValue(
            ChildStack(
                configuration = "discover",
                instance = MainComponent.Child.Discover(PreviewDiscoverViewModel()),
            ),
        )
    override val isDrawerOpen: BehaviorObservable<Boolean> = BehaviorSubject(false)
    override val drawerTitle: String = "Menu"
    override val drawerMessage: String = "Your profile, cities and settings will appear here."

    override fun tabTitle(tab: MainTab): String {
        return tab.name
    }

    override fun selectTab(tab: MainTab) {
    }

    override fun openDrawer() {
    }

    override fun closeDrawer() {
    }
}

internal class PreviewRootComponent : RootComponent {
    override val stack: Value<ChildStack<*, RootComponent.Child>> =
        MutableValue(
            ChildStack(
                configuration = "main",
                instance = RootComponent.Child.Main(PreviewMainComponent()),
            ),
        )
}
