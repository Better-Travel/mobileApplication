package com.bettertravel.ui.preview

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.observableOf
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.singleOf
import com.badoo.reaktive.subject.behavior.BehaviorObservable
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.bettertravel.domain.chat.SuggestedRoute
import com.bettertravel.domain.map.GeoPoint
import com.bettertravel.domain.map.MapTile
import com.bettertravel.domain.routes.Route
import com.bettertravel.domain.routes.RouteStop
import com.bettertravel.presentation.details.StopDetailsEssential
import com.bettertravel.presentation.details.StopDetailsHighlight
import com.bettertravel.presentation.details.StopDetailsMetric
import com.bettertravel.presentation.details.StopDetailsViewModel
import com.bettertravel.presentation.drawer.DrawerCity
import com.bettertravel.presentation.drawer.DrawerProfile
import com.bettertravel.presentation.drawer.DrawerRoute
import com.bettertravel.presentation.drawer.DrawerRouteSketch
import com.bettertravel.presentation.drawer.DrawerStat
import com.bettertravel.presentation.drawer.DrawerViewModel
import com.bettertravel.presentation.memories.MemoriesViewModel
import com.bettertravel.presentation.memories.MemoryFilter
import com.bettertravel.presentation.memories.MemoryItem
import com.bettertravel.presentation.memories.MemoryNote
import com.bettertravel.presentation.memories.MemoryPhoto
import com.bettertravel.presentation.memories.MemorySummary
import com.bettertravel.presentation.navigation.MainComponent
import com.bettertravel.presentation.navigation.MainTab
import com.bettertravel.presentation.navigation.RootComponent
import com.bettertravel.presentation.route.RouteSelectedStop
import com.bettertravel.presentation.route.RouteStopDetails
import com.bettertravel.presentation.route.RouteSummary
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

internal class PreviewRouteViewModel(
    withRoute: Boolean = false,
) : RouteViewModel {
    private val sampleStops =
        listOf(
            RouteStop("Old Town Market Square", GeoPoint(52.24975, 21.01218)),
            RouteStop("St. John's Vaults & Bell View", GeoPoint(52.24902, 21.01368)),
            RouteStop("Freta Artisan Café (Courtyard)", GeoPoint(52.25315, 21.01044)),
        )
    private val sampleDetails =
        listOf(
            RouteStopDetails(
                1,
                sampleStops[0].name,
                "Historic landmark, vibrant cafes",
                "30 min",
                "4.9",
                "1.2k",
                "Departure 10:00 AM",
            ),
            RouteStopDetails(
                2,
                sampleStops[1].name,
                "Gothic architecture & crypt",
                "25 min",
                "4.8",
                "850",
                "7 min walk",
            ),
            RouteStopDetails(
                3,
                sampleStops[2].name,
                "Local coffee & warm pastries",
                "20 min",
                "4.7",
                "490",
                "Rest break spot",
            ),
        )

    override val title: String = "Route"
    override val emptyMessage: String = "Your route will appear here once Betty has planned it."
    override val errorMessage: String = "Could not build the walking route"
    override val retryTitle: String = "Try again"
    override val loadingMessage: String = "Betty is preparing a route for you"
    override val attribution: String = "© OpenStreetMap contributors © CARTO"
    override val pillTitle: String = "Optimal Loop"
    override val regenerateTitle: String = "Regenerate"
    override val walkSuffix: String = "walk"
    override val stopsSuffix: String = "stops"
    override val totalTimeCaption: String = "Total time"
    override val walkingCaption: String = "Walking"
    override val curatedCaption: String = "Curated"
    override val stopsCaption: String = "Stops"
    override val sheetHint: String = "Tap or swipe up for detailed stops"
    override val startTitle: String = "Start"
    override val startNavigationTitle: String = "Start Route Navigation"
    override val sequenceTitle: String = "Today's Sequence"
    override val walkableTitle: String = "Walkable"
    override val editOrderTitle: String = "Edit order"
    override val askTitle: String = "Need changes?"
    override val askActionTitle: String = "Ask me!"
    override val selectedTag: String = "Selected"
    override val showDetailsTitle: String = "Show details"
    override val changeTitle: String = "Change"
    override val deleteTitle: String = "Delete"
    override val routeTitle: BehaviorObservable<String?> =
        BehaviorSubject(if (withRoute) "Old Town & Courtyards" else null)
    override val summary: BehaviorObservable<RouteSummary?> =
        BehaviorSubject(if (withRoute) RouteSummary("3.2 km", "2h 45m", "3") else null)
    override val stops: BehaviorObservable<List<RouteStop>> =
        BehaviorSubject(if (withRoute) sampleStops else emptyList())
    override val stopDetails: BehaviorObservable<List<RouteStopDetails>> =
        BehaviorSubject(if (withRoute) sampleDetails else emptyList())
    override val selectedStop: BehaviorObservable<RouteSelectedStop?> =
        BehaviorSubject(
            if (withRoute) {
                RouteSelectedStop(
                    2,
                    sampleStops[1].name,
                    "Stop 2 • 0.1 km from previous stop",
                    true,
                )
            } else {
                null
            },
        )
    override val path: BehaviorObservable<List<GeoPoint>> =
        BehaviorSubject(if (withRoute) sampleStops.map { stop -> stop.location } else emptyList())
    override val isLoading: Observable<Boolean> = observableOf(false)
    override val hasError: BehaviorObservable<Boolean> = BehaviorSubject(false)

    override fun loadTile(tile: MapTile): Single<ByteArray> {
        return singleOf(ByteArray(0))
    }

    override fun regenerate() {
    }

    override fun start() {
    }

    override fun askBetty() {
    }

    override fun selectStop(order: Int) {
    }

    override fun clearSelection() {
    }

    override fun showDetails(order: Int) {
    }

    override fun deleteSelectedStop() {
    }
}

internal class PreviewStopDetailsViewModel : StopDetailsViewModel {
    override val eyebrow: String = "BETTERTRAVEL"
    override val screenTitle: String = "Live Navigation Hud"
    override val stopBadge: String = "Stop #2 of 4 · Warsaw Old Town Loop"
    override val tags: List<String> = listOf("UNESCO World Heritage", "Free Entry Nave", "Mazovian Gothic")
    override val name: String = "St. John's Archcathedral"
    override val nativeName: String = "Archikatedra św. Jana Chrzciciela"
    override val rating: String = "4.8"
    override val reviews: String = "1,420 reviews"
    override val visitDuration: StopDetailsMetric = StopDetailsMetric("25 min", "Ideal visit")
    override val distance: StopDetailsMetric = StopDetailsMetric("160 m", "From Castle Sq")
    override val secretEyebrow: String = "Betty Secret"
    override val secretTag: String = "Historical Flash"
    override val secretQuote: String =
        "\"Did you know? During WWII, 90% of the cathedral was destroyed, " +
            "but the crypts survived intact!\""
    override val essentialsTitle: String = "Visiting Essentials"
    override val essentials: List<StopDetailsEssential> =
        listOf(
            StopDetailsEssential(
                "Opening Hours",
                "Open Now",
                "Today: 10:00 AM – 6:00 PM",
                "Royal Crypts close 30 min before nave",
            ),
            StopDetailsEssential(
                "Admission & Fees",
                "5 PLN (~€1.20)",
                "Free entry for main gothic nave",
                "Small maintenance fee for vaults",
            ),
            StopDetailsEssential("Address", "Map", "Świętojańska 8, 00-278 Warszawa", null),
        )
    override val highlightsTitle: String = "Highlights & What to See"
    override val highlights: List<StopDetailsHighlight> =
        listOf(
            StopDetailsHighlight("Royal Crypts & Sarcophagi", "Final resting place of King Stanisław August…"),
            StopDetailsHighlight("Mazovian Gothic Vaults", "Distinctive high ribbed star vaults…"),
        )
    override val surroundingTitle: String = "Surrounding Walk"
    override val surroundingSubtitle: String = "Old Town Heritage Path"
    override val nextStop: String = "Next stop: Warsaw Old Town Market (3 min walk)"
    override val keepTitle: String = "Keep in Route"
    override val swapTitle: String = "Swap"
    override val removeTitle: String = "Remove from this walk"
    override val isRouteStop: Boolean = true
    override val isBookmarked: BehaviorObservable<Boolean> = BehaviorSubject(false)

    override fun back() {
    }

    override fun toggleBookmark() {
    }

    override fun keep() {
    }

    override fun swap() {
    }

    override fun remove() {
    }
}

internal class PreviewMemoriesViewModel : MemoriesViewModel {
    private val sampleRoute =
        SuggestedRoute(
            "Warsaw Old Town Historic Loop",
            3.4,
            112,
            listOf(RouteStop("Old Town Market Square", GeoPoint(52.24975, 21.01218))),
        )

    override val title: String = "BetterTravel Memories"
    override val subtitle: String = "Relive your wanderlust and shared paths"
    override val levelBadge: String = "Level 4 Nomad"
    override val emptyMessage: String = "Your saved walks will appear here."
    override val rewalkTitle: String = "Re-walk"
    override val savedPlaceTitle: String = "Saved place"
    override val summary: BehaviorObservable<MemorySummary> =
        BehaviorSubject(MemorySummary("18", "Moments Captured", "Across 2 countries • 42.6 km walked"))
    override val filters: BehaviorObservable<List<MemoryFilter>> =
        BehaviorSubject(
            listOf(
                MemoryFilter("all", "All", 6, false),
                MemoryFilter("Warsaw", "Warsaw", 4, false),
                MemoryFilter("favorites", "Favorites", 1, true),
            ),
        )
    override val selectedFilterId: BehaviorObservable<String> = BehaviorSubject("all")
    override val items: BehaviorObservable<List<MemoryItem>> =
        BehaviorSubject(
            listOf(
                MemoryItem.SavedPlace(
                    "saved",
                    "Feb 14, 2026",
                    "Old Town & Courtyards",
                    "St. John's Archcathedral",
                    GeoPoint(52.24902, 21.01368),
                ),
                MemoryItem.Walk(
                    id = "walk",
                    date = "Feb 12, 2026",
                    place = "Warsaw, Poland",
                    title = "Warsaw Old Town Historic Loop",
                    distance = "3.4 km",
                    stops = "4 stops",
                    duration = "1h 52m",
                    photos = listOf(MemoryPhoto("Market"), MemoryPhoto("Cathedral"), MemoryPhoto("+5 more")),
                    note =
                        MemoryNote.Voice(
                            "Betty Voice Memory",
                            "\"The crypts under St. John's date back to…",
                            "0:24",
                        ),
                    route = sampleRoute,
                ),
            ),
        )

    override fun selectFilter(filter: MemoryFilter) {
    }

    override fun rewalk(walk: MemoryItem.Walk) {
    }

    override fun open(place: MemoryItem.SavedPlace) {
    }

    override fun removeBookmark(place: MemoryItem.SavedPlace) {
    }
}

internal class PreviewDrawerViewModel : DrawerViewModel {
    override val profile: DrawerProfile = DrawerProfile("Alex Morgan", "Explorer Lv. 3 • Wanderer")
    override val stats: List<DrawerStat> =
        listOf(DrawerStat("4", "Cities"), DrawerStat("12", "Routes"), DrawerStat("38h", "Walked"))
    override val exploredCitiesTitle: String = "Explored cities"
    override val mapViewTitle: String = "Map view"
    override val cities: List<DrawerCity> =
        listOf(
            DrawerCity("warsaw", "Warsaw", "4 Routes", "16 Sights logged"),
            DrawerCity("prague", "Prague", "2 Routes", "9 Sights logged"),
            DrawerCity("krakow", "Kraków", "3 Routes", "11 Sights logged"),
        )
    override val selectedCityId: BehaviorObservable<String> = BehaviorSubject("warsaw")
    override val visitedRoutesTitle: String = "Visited routes"
    override val seeAllTitle: String = "See All"
    override val routes: List<DrawerRoute> =
        listOf(
            DrawerRoute(
                "1",
                "Old Town Historic Trail",
                "Warsaw • Feb 12",
                "3.4 km",
                "5/5 Sights",
                true,
                DrawerRouteSketch.Trail,
            ),
            DrawerRoute(
                "2",
                "Vistula River Sunset Walk",
                "Warsaw • Feb 10",
                "2.1 km",
                "3 Sights",
                false,
                DrawerRouteSketch.River,
            ),
            DrawerRoute(
                "3",
                "Charles Bridge Loop",
                "Prague • Jan 24",
                "4.8 km",
                "6 Sights",
                false,
                DrawerRouteSketch.Loop,
            ),
        )
    override val voiceGuideTitle: String = "Nimbus Voice Guide"
    override val voiceGuideValue: String = "Friendly Sky"
    override val offlineMapsTitle: String = "Offline Maps"
    override val offlineMapsSubtitle: String = "1.4 GB cached"
    override val isOfflineMapsEnabled: BehaviorObservable<Boolean> = BehaviorSubject(true)
    override val languageUnitsTitle: String = "Language & Units"
    override val languageUnitsValue: String = "EN • Metric"
    override val supportTitle: String = "Support & Betty Tips"
    override val appVersion: String = "v2.4"

    override fun selectCity(city: DrawerCity) {
    }

    override fun setOfflineMapsEnabled(enabled: Boolean) {
    }
}

internal class PreviewMainComponent : MainComponent {
    override val appTitle: String = "BetterTravel"
    override val tabs: List<MainTab> = MainTab.entries
    override val children: Value<ChildStack<*, MainComponent.Child>> =
        MutableValue(
            ChildStack(
                configuration = "discover",
                instance = MainComponent.Child.Discover(PreviewAIChat.viewModel(withMessages = false)),
            ),
        )
    override val isDrawerOpen: BehaviorObservable<Boolean> = BehaviorSubject(false)
    override val drawerViewModel: DrawerViewModel = PreviewDrawerViewModel()

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
