package com.bettertravel.presentation.memories

import com.badoo.reaktive.completable.subscribe
import com.bettertravel.common.Deps
import com.bettertravel.domain.chat.SuggestedRoute
import com.bettertravel.domain.map.GeoPoint
import com.bettertravel.domain.places.BookmarkedPlace
import com.bettertravel.domain.places.PlaceBookmarking
import com.bettertravel.domainmock.places.PlaceBookmarkingMock
import com.bettertravel.localization.GeneratedStringRepository
import com.bettertravel.localization.StringRepository
import com.bettertravel.presentation.ViewModelContext
import com.bettertravel.presentation.createTestComponentContext
import com.bettertravel.presentation.route.StopDetailsRequest
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MemoriesViewModelTests {
    private val bookmarking = PlaceBookmarkingMock()
    private val rewalked = mutableListOf<SuggestedRoute>()
    private val opened = mutableListOf<StopDetailsRequest>()

    @BeforeTest
    fun setUp() {
        Deps.di =
            DI {
                bindSingleton<PlaceBookmarking> { bookmarking }
                bindSingleton<StringRepository> { GeneratedStringRepository { "en" } }
            }
    }

    private fun createViewModel(): MemoriesViewModelImpl {
        val parameters =
            MemoriesViewModel.Parameters(
                context = ViewModelContext(createTestComponentContext()),
                onRewalk = { route -> rewalked.add(route) },
                onOpenPlace = { request -> opened.add(request) },
            )
        return MemoriesViewModelImpl(parameters)
    }

    private fun bookmark(name: String) {
        val location = GeoPoint(52.0, 21.0)
        bookmarking.add(BookmarkedPlace(BookmarkedPlace.idOf(name, location), name, "Loop", location, 0L)).subscribe()
    }

    private fun MemoriesViewModelImpl.filter(id: String): MemoryFilter {
        return filters.value.first { filter -> filter.id == id }
    }

    @Test
    fun testThat_nothingIsBookmarked_allFilterShowsOnlyWalks() {
        val viewModel = createViewModel()

        assertTrue(viewModel.items.value.all { item -> item is MemoryItem.Walk })
        assertEquals(3, viewModel.filter("all").count)
        assertEquals(0, viewModel.filter("favorites").count)
    }

    @Test
    fun testThat_placeIsBookmarked_itIsAddedToMemoriesAndFavorites() {
        val viewModel = createViewModel()

        bookmark("St. John's Archcathedral")

        val first = viewModel.items.value.first()
        assertTrue(first is MemoryItem.SavedPlace)
        assertEquals("St. John's Archcathedral", first.name)
        assertEquals(1, viewModel.filter("favorites").count)
    }

    @Test
    fun testThat_favoritesFilterIsSelected_onlyBookmarkedPlacesAreShown() {
        val viewModel = createViewModel()
        bookmark("Cathedral")

        viewModel.selectFilter(viewModel.filter("favorites"))

        assertEquals(listOf("Cathedral"), viewModel.items.value.map { item -> (item as MemoryItem.SavedPlace).name })
    }

    @Test
    fun testThat_cityFilterIsSelected_onlyWalksFromThatCityAreShown() {
        val viewModel = createViewModel()

        viewModel.selectFilter(viewModel.filter("Prague"))

        assertEquals(
            listOf("Charles Bridge Loop"),
            viewModel.items.value.map { item -> (item as MemoryItem.Walk).title },
        )
    }

    @Test
    fun testThat_bookmarkIsRemovedFromMemories_itDisappears() {
        val viewModel = createViewModel()
        bookmark("Cathedral")
        val saved = viewModel.items.value.first() as MemoryItem.SavedPlace

        viewModel.removeBookmark(saved)

        assertTrue(viewModel.items.value.none { item -> item is MemoryItem.SavedPlace })
        assertTrue(bookmarking.bookmarks.value.isEmpty())
    }

    @Test
    fun testThat_walkIsRewalked_itsRouteIsReported() {
        val viewModel = createViewModel()
        val walk = viewModel.items.value.first() as MemoryItem.Walk

        viewModel.rewalk(walk)

        assertEquals(listOf(walk.route), rewalked)
    }

    @Test
    fun testThat_savedPlaceIsOpened_detailsAreRequestedAsAMemory() {
        val viewModel = createViewModel()
        bookmark("Cathedral")
        val saved = viewModel.items.value.first() as MemoryItem.SavedPlace

        viewModel.open(saved)

        val request = opened.single()
        assertEquals("Cathedral", request.name)
        assertEquals("Loop", request.routeTitle)
        assertTrue(request.isFromMemories)
    }
}
