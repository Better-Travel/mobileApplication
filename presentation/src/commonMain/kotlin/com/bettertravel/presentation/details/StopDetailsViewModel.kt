package com.bettertravel.presentation.details

import com.badoo.reaktive.subject.behavior.BehaviorObservable
import com.bettertravel.presentation.ViewModel
import com.bettertravel.presentation.ViewModelContext
import com.bettertravel.presentation.route.StopDetailsRequest

data class StopDetailsEssential(
    val title: String,
    val trailing: String?,
    val body: String,
    val note: String?,
)

data class StopDetailsHighlight(
    val title: String,
    val description: String,
)

data class StopDetailsMetric(
    val value: String,
    val caption: String,
)

interface StopDetailsViewModel : ViewModel {
    val eyebrow: String
    val screenTitle: String
    val stopBadge: String
    val tags: List<String>
    val name: String
    val nativeName: String
    val rating: String
    val reviews: String
    val visitDuration: StopDetailsMetric
    val distance: StopDetailsMetric
    val secretEyebrow: String
    val secretTag: String
    val secretQuote: String
    val essentialsTitle: String
    val essentials: List<StopDetailsEssential>
    val highlightsTitle: String
    val highlights: List<StopDetailsHighlight>
    val surroundingTitle: String
    val surroundingSubtitle: String
    val nextStop: String
    val keepTitle: String
    val swapTitle: String
    val removeTitle: String
    val isRouteStop: Boolean
    val isBookmarked: BehaviorObservable<Boolean>

    fun back()

    fun toggleBookmark()

    fun keep()

    fun swap()

    fun remove()

    data class Parameters(
        val context: ViewModelContext,
        val request: StopDetailsRequest,
        val onBack: () -> Unit,
        val onSwap: () -> Unit,
        val onRemove: (Int) -> Unit,
    )
}
