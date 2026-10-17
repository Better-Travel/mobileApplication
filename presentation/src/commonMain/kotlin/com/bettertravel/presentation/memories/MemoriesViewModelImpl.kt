package com.bettertravel.presentation.memories

import com.badoo.reaktive.completable.subscribe
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.subject.behavior.BehaviorObservable
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.bettertravel.common.Deps
import com.bettertravel.common.time.formatShortDate
import com.bettertravel.domain.chat.SuggestedRoute
import com.bettertravel.domain.map.GeoPoint
import com.bettertravel.domain.places.BookmarkedPlace
import com.bettertravel.domain.places.PlaceBookmarking
import com.bettertravel.domain.routes.RouteStop
import com.bettertravel.localization.StringRepository
import com.bettertravel.presentation.ScreenViewModel
import com.bettertravel.presentation.route.StopDetailsRequest

class MemoriesViewModelImpl(
    private val parameters: MemoriesViewModel.Parameters,
) : ScreenViewModel(parameters.context),
    MemoriesViewModel {
    private val strings: StringRepository = Deps.instance()
    private val bookmarking: PlaceBookmarking = Deps.instance()

    private val summarySubject = BehaviorSubject(createSummary(0))
    private val filtersSubject = BehaviorSubject(createFilters(0))
    private val selectedFilterSubject = BehaviorSubject(FILTER_ALL)
    private val itemsSubject = BehaviorSubject<List<MemoryItem>>(emptyList())

    override val title: String = strings.memoriesTitle
    override val subtitle: String = strings.memoriesSubtitle
    override val levelBadge: String = "Level 4 Nomad"
    override val emptyMessage: String = strings.memoriesEmptyMessage
    override val rewalkTitle: String = strings.memoriesRewalk
    override val savedPlaceTitle: String = strings.memoriesSavedPlace

    override val summary: BehaviorObservable<MemorySummary> = summarySubject
    override val filters: BehaviorObservable<List<MemoryFilter>> = filtersSubject
    override val selectedFilterId: BehaviorObservable<String> = selectedFilterSubject
    override val items: BehaviorObservable<List<MemoryItem>> = itemsSubject

    private var savedPlaces: List<MemoryItem.SavedPlace> = emptyList()

    init {
        bookmarking.bookmarks
            .subscribe(onNext = { places -> showBookmarks(places) })
            .disposeWhenClosed()
    }

    override fun selectFilter(filter: MemoryFilter) {
        selectedFilterSubject.onNext(filter.id)
        refreshItems()
    }

    override fun rewalk(walk: MemoryItem.Walk) {
        parameters.onRewalk(walk.route)
    }

    override fun open(place: MemoryItem.SavedPlace) {
        parameters.onOpenPlace(
            StopDetailsRequest(
                order = 0,
                total = 0,
                name = place.name,
                routeTitle = place.routeTitle,
                location = place.location,
                isFromMemories = true,
            ),
        )
    }

    override fun removeBookmark(place: MemoryItem.SavedPlace) {
        bookmarking.remove(place.id).subscribe().disposeWhenClosed()
    }

    private fun showBookmarks(places: List<BookmarkedPlace>) {
        savedPlaces =
            places.map { place ->
                MemoryItem.SavedPlace(
                    id = place.id,
                    date = formatShortDate(place.savedAtMillis),
                    routeTitle = place.routeTitle,
                    name = place.name,
                    location = place.location,
                )
            }
        summarySubject.onNext(createSummary(savedPlaces.size))
        filtersSubject.onNext(createFilters(savedPlaces.size))
        refreshItems()
    }

    private fun refreshItems() {
        val selected = selectedFilterSubject.value
        val items: List<MemoryItem> =
            when (selected) {
                FILTER_ALL -> savedPlaces + MOCK_WALKS
                FILTER_FAVORITES -> savedPlaces
                else -> MOCK_WALKS.filter { walk -> walk.place.substringBefore(",") == selected }
            }
        itemsSubject.onNext(items)
    }

    // TODO: replace the mock summary and walks with completed walks once walking mode exists
    private fun createSummary(savedCount: Int): MemorySummary {
        return MemorySummary(
            moments = (MOCK_MOMENTS + savedCount).toString(),
            momentsCaption = strings.memoriesMomentsCaption,
            details = "Across 2 countries • 42.6 ${strings.unitKilometerShort} ${strings.drawerStatWalked.lowercase()}",
        )
    }

    private fun createFilters(savedCount: Int): List<MemoryFilter> {
        val warsaw = MOCK_WALKS.count { walk -> walk.place.startsWith("Warsaw") }
        val prague = MOCK_WALKS.count { walk -> walk.place.startsWith("Prague") }
        return listOf(
            MemoryFilter(FILTER_ALL, strings.memoriesFilterAll, MOCK_WALKS.size + savedCount, false),
            MemoryFilter("Warsaw", "Warsaw", warsaw, false),
            MemoryFilter("Prague", "Prague", prague, false),
            MemoryFilter(FILTER_FAVORITES, strings.memoriesFilterFavorites, savedCount, true),
        )
    }

    private companion object {
        const val FILTER_ALL = "all"
        const val FILTER_FAVORITES = "favorites"
        const val MOCK_MOMENTS = 18

        val OLD_TOWN_STOPS =
            listOf(
                RouteStop("Old Town Market Square", GeoPoint(52.24975, 21.01218)),
                RouteStop("St. John's Vaults & Bell View", GeoPoint(52.24902, 21.01368)),
                RouteStop("Royal Castle & Gardens", GeoPoint(52.24795, 21.01529)),
                RouteStop("Freta Artisan Café (Courtyard)", GeoPoint(52.25315, 21.01044)),
            )
        val VISTULA_STOPS =
            listOf(
                RouteStop("Multimedia Fountain Park", GeoPoint(52.25480, 21.01130)),
                RouteStop("Vistula Boulevards", GeoPoint(52.24060, 21.03870)),
                RouteStop("Copernicus Science Centre", GeoPoint(52.24190, 21.02880)),
            )

        val MOCK_WALKS: List<MemoryItem.Walk> =
            listOf(
                MemoryItem.Walk(
                    id = "mock-old-town",
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
                    route = SuggestedRoute("Warsaw Old Town Historic Loop", 3.4, 112, OLD_TOWN_STOPS),
                ),
                MemoryItem.Walk(
                    id = "mock-vistula",
                    date = "Feb 10, 2026",
                    place = "Warsaw Boulevards",
                    title = "Vistula Promenade & Sunset",
                    distance = "2.8 km",
                    stops = "3 stops",
                    duration = "1h 15m",
                    photos = listOf(MemoryPhoto("Golden Hour Walk")),
                    note =
                        MemoryNote.Reflection(
                            "Traveler Reflection",
                            "Grabbed cinnamon pastries near the Mermaid statue. The breeze off the river was chilly, " +
                                "but Betty suggested the coziest heated barge cafe just in time.",
                        ),
                    route = SuggestedRoute("Vistula Promenade & Sunset", 2.8, 75, VISTULA_STOPS),
                ),
                MemoryItem.Walk(
                    id = "mock-charles-bridge",
                    date = "Jan 24, 2026",
                    place = "Prague, Czechia",
                    title = "Charles Bridge Loop",
                    distance = "4.8 km",
                    stops = "6 stops",
                    duration = "2h 05m",
                    photos = listOf(MemoryPhoto("Charles Bridge")),
                    note = null,
                    route =
                        SuggestedRoute(
                            "Charles Bridge Loop",
                            4.8,
                            125,
                            listOf(
                                RouteStop("Old Town Bridge Tower", GeoPoint(50.08655, 14.41377)),
                                RouteStop("Charles Bridge", GeoPoint(50.08650, 14.41080)),
                                RouteStop("Lennon Wall", GeoPoint(50.08610, 14.40660)),
                            ),
                        ),
                ),
            )
    }
}
