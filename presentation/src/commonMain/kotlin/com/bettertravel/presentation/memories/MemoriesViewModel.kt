package com.bettertravel.presentation.memories

import com.badoo.reaktive.subject.behavior.BehaviorObservable
import com.bettertravel.domain.chat.SuggestedRoute
import com.bettertravel.domain.map.GeoPoint
import com.bettertravel.presentation.ViewModel
import com.bettertravel.presentation.ViewModelContext
import com.bettertravel.presentation.route.StopDetailsRequest

data class MemorySummary(
    val moments: String,
    val momentsCaption: String,
    val details: String,
)

data class MemoryFilter(
    val id: String,
    val title: String,
    val count: Int,
    val isFavorites: Boolean,
)

data class MemoryPhoto(
    val label: String,
)

sealed interface MemoryNote {
    data class Voice(
        val title: String,
        val quote: String,
        val length: String,
    ) : MemoryNote

    data class Reflection(
        val title: String,
        val text: String,
    ) : MemoryNote
}

sealed interface MemoryItem {
    val id: String

    data class Walk(
        override val id: String,
        val date: String,
        val place: String,
        val title: String,
        val distance: String,
        val stops: String,
        val duration: String,
        val photos: List<MemoryPhoto>,
        val note: MemoryNote?,
        val route: SuggestedRoute,
    ) : MemoryItem

    data class SavedPlace(
        override val id: String,
        val date: String,
        val routeTitle: String,
        val name: String,
        val location: GeoPoint,
    ) : MemoryItem
}

interface MemoriesViewModel : ViewModel {
    val title: String
    val subtitle: String
    val levelBadge: String
    val emptyMessage: String
    val rewalkTitle: String
    val savedPlaceTitle: String

    val summary: BehaviorObservable<MemorySummary>
    val filters: BehaviorObservable<List<MemoryFilter>>
    val selectedFilterId: BehaviorObservable<String>
    val items: BehaviorObservable<List<MemoryItem>>

    fun selectFilter(filter: MemoryFilter)

    fun rewalk(walk: MemoryItem.Walk)

    fun open(place: MemoryItem.SavedPlace)

    fun removeBookmark(place: MemoryItem.SavedPlace)

    data class Parameters(
        val context: ViewModelContext,
        val onRewalk: (SuggestedRoute) -> Unit,
        val onOpenPlace: (StopDetailsRequest) -> Unit,
    )
}
