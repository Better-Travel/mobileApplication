package com.bettertravel.presentation.drawer

import com.badoo.reaktive.subject.behavior.BehaviorObservable
import com.bettertravel.presentation.ViewModel
import com.bettertravel.presentation.ViewModelContext

data class DrawerProfile(
    val name: String,
    val level: String,
)

data class DrawerStat(
    val value: String,
    val caption: String,
)

data class DrawerCity(
    val id: String,
    val name: String,
    val routes: String,
    val sights: String,
)

enum class DrawerRouteSketch {
    Trail,
    River,
    Loop,
}

data class DrawerRoute(
    val id: String,
    val title: String,
    val subtitle: String,
    val distance: String,
    val sights: String,
    val isCompleted: Boolean,
    val sketch: DrawerRouteSketch,
)

interface DrawerViewModel : ViewModel {
    val profile: DrawerProfile
    val stats: List<DrawerStat>
    val exploredCitiesTitle: String
    val mapViewTitle: String
    val cities: List<DrawerCity>
    val selectedCityId: BehaviorObservable<String>
    val visitedRoutesTitle: String
    val seeAllTitle: String
    val routes: List<DrawerRoute>
    val voiceGuideTitle: String
    val voiceGuideValue: String
    val offlineMapsTitle: String
    val offlineMapsSubtitle: String
    val isOfflineMapsEnabled: BehaviorObservable<Boolean>
    val languageUnitsTitle: String
    val languageUnitsValue: String
    val supportTitle: String
    val appVersion: String

    fun selectCity(city: DrawerCity)

    fun setOfflineMapsEnabled(enabled: Boolean)

    data class Parameters(
        val context: ViewModelContext,
    )
}
