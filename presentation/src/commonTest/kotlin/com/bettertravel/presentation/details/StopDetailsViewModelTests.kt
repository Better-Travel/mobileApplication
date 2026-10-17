package com.bettertravel.presentation.details

import com.badoo.reaktive.completable.subscribe
import com.bettertravel.common.Deps
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
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StopDetailsViewModelTests {
    private var backCount = 0
    private var swapCount = 0
    private val removedIndexes = mutableListOf<Int>()

    private val bookmarking = PlaceBookmarkingMock()

    @BeforeTest
    fun setUp() {
        Deps.di =
            DI {
                bindSingleton<PlaceBookmarking> { bookmarking }
                bindSingleton<StringRepository> { GeneratedStringRepository { "en" } }
            }
    }

    private fun createViewModel(): StopDetailsViewModelImpl {
        val parameters =
            StopDetailsViewModel.Parameters(
                context = ViewModelContext(createTestComponentContext()),
                request =
                    StopDetailsRequest(
                        2,
                        4,
                        "St. John's Archcathedral",
                        "Warsaw Old Town Loop",
                        GeoPoint(52.249, 21.0137),
                    ),
                onBack = { backCount += 1 },
                onSwap = { swapCount += 1 },
                onRemove = { index -> removedIndexes.add(index) },
            )
        return StopDetailsViewModelImpl(parameters)
    }

    @Test
    fun testThat_viewModelIsCreated_badgeDescribesTheStopInTheRoute() {
        val viewModel = createViewModel()

        assertEquals("Stop #2 of 4 · Warsaw Old Town Loop", viewModel.stopBadge)
        assertEquals("St. John's Archcathedral", viewModel.name)
    }

    @Test
    fun testThat_removeIsTapped_zeroBasedIndexIsReported() {
        val viewModel = createViewModel()

        viewModel.remove()

        assertEquals(listOf(1), removedIndexes)
    }

    @Test
    fun testThat_keepAndBackAreTapped_screenIsClosed() {
        val viewModel = createViewModel()

        viewModel.keep()
        viewModel.back()

        assertEquals(2, backCount)
    }

    @Test
    fun testThat_swapIsTapped_swapIsReported() {
        val viewModel = createViewModel()

        viewModel.swap()

        assertEquals(1, swapCount)
    }

    @Test
    fun testThat_bookmarkIsToggled_placeIsSavedAndRemoved() {
        val viewModel = createViewModel()
        assertFalse(viewModel.isBookmarked.value)

        viewModel.toggleBookmark()

        assertTrue(viewModel.isBookmarked.value)
        assertEquals(listOf("St. John's Archcathedral"), bookmarking.bookmarks.value.map { place -> place.name })

        viewModel.toggleBookmark()

        assertFalse(viewModel.isBookmarked.value)
        assertTrue(bookmarking.bookmarks.value.isEmpty())
    }

    @Test
    fun testThat_placeIsAlreadySaved_screenStartsBookmarked() {
        val request = StopDetailsRequest(2, 4, "St. John's Archcathedral", "Loop", GeoPoint(52.249, 21.0137))
        bookmarking
            .add(
                BookmarkedPlace(
                    BookmarkedPlace.idOf(request.name, request.location),
                    request.name,
                    "Loop",
                    request.location,
                    0L,
                ),
            ).subscribe()

        assertTrue(createViewModel().isBookmarked.value)
    }

    @Test
    fun testThat_detailsAreOpenedFromMemories_badgeAndRemoveActionDescribeASavedPlace() {
        val request =
            StopDetailsRequest(0, 0, "Cathedral", "Old Town Loop", GeoPoint(52.249, 21.0137), isFromMemories = true)
        val location = request.location
        bookmarking
            .add(
                BookmarkedPlace(
                    BookmarkedPlace.idOf("Cathedral", location),
                    "Cathedral",
                    "Old Town Loop",
                    location,
                    0L,
                ),
            ).subscribe()
        val viewModel =
            StopDetailsViewModelImpl(
                StopDetailsViewModel.Parameters(
                    context = ViewModelContext(createTestComponentContext()),
                    request = request,
                    onBack = { backCount += 1 },
                    onSwap = { swapCount += 1 },
                    onRemove = { index -> removedIndexes.add(index) },
                ),
            )

        viewModel.remove()

        assertFalse(viewModel.isRouteStop)
        assertEquals("Saved place · Old Town Loop", viewModel.stopBadge)
        assertTrue(bookmarking.bookmarks.value.isEmpty())
        assertEquals(1, backCount)
        assertTrue(removedIndexes.isEmpty())
    }
}
