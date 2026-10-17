package com.bettertravel.presentation.drawer

import com.badoo.reaktive.subject.behavior.BehaviorObservable
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.bettertravel.common.Deps
import com.bettertravel.localization.StringRepository
import com.bettertravel.presentation.ScreenViewModel

class DrawerViewModelImpl(
    parameters: DrawerViewModel.Parameters,
) : ScreenViewModel(parameters.context),
    DrawerViewModel {
    private val strings: StringRepository = Deps.instance()

    // TODO: replace the mock profile, cities and routes with real data
    override val profile: DrawerProfile = DrawerProfile(name = "Alex Morgan", level = "Explorer Lv. 3 • Wanderer")
    override val stats: List<DrawerStat> =
        listOf(
            DrawerStat(value = "4", caption = strings.drawerStatCities),
            DrawerStat(value = "12", caption = strings.drawerStatRoutes),
            DrawerStat(value = "38h", caption = strings.drawerStatWalked),
        )
    override val exploredCitiesTitle: String = strings.drawerExploredCities
    override val mapViewTitle: String = strings.drawerMapView
    override val cities: List<DrawerCity> =
        listOf(
            DrawerCity(id = "warsaw", name = "Warsaw", routes = "4 Routes", sights = "16 Sights logged"),
            DrawerCity(id = "prague", name = "Prague", routes = "2 Routes", sights = "9 Sights logged"),
            DrawerCity(id = "krakow", name = "Kraków", routes = "3 Routes", sights = "11 Sights logged"),
        )
    override val visitedRoutesTitle: String = strings.drawerVisitedRoutes
    override val seeAllTitle: String = strings.drawerSeeAll
    override val routes: List<DrawerRoute> =
        listOf(
            DrawerRoute(
                id = "old-town",
                title = "Old Town Historic Trail",
                subtitle = "Warsaw • Feb 12",
                distance = "3.4 km",
                sights = "5/5 Sights",
                isCompleted = true,
                sketch = DrawerRouteSketch.Trail,
            ),
            DrawerRoute(
                id = "vistula",
                title = "Vistula River Sunset Walk",
                subtitle = "Warsaw • Feb 10",
                distance = "2.1 km",
                sights = "3 Sights",
                isCompleted = false,
                sketch = DrawerRouteSketch.River,
            ),
            DrawerRoute(
                id = "charles-bridge",
                title = "Charles Bridge Loop",
                subtitle = "Prague • Jan 24",
                distance = "4.8 km",
                sights = "6 Sights",
                isCompleted = false,
                sketch = DrawerRouteSketch.Loop,
            ),
        )
    override val voiceGuideTitle: String = strings.drawerVoiceGuide
    override val voiceGuideValue: String = "Friendly Sky"
    override val offlineMapsTitle: String = strings.drawerOfflineMaps
    override val offlineMapsSubtitle: String = "1.4 GB cached"
    override val languageUnitsTitle: String = strings.drawerLanguageUnits
    override val languageUnitsValue: String = "EN • Metric"
    override val supportTitle: String = strings.drawerSupport
    override val appVersion: String = "v2.4"

    private val selectedCitySubject = BehaviorSubject(cities.first().id)
    private val offlineMapsSubject = BehaviorSubject(true)

    override val selectedCityId: BehaviorObservable<String> = selectedCitySubject
    override val isOfflineMapsEnabled: BehaviorObservable<Boolean> = offlineMapsSubject

    override fun selectCity(city: DrawerCity) {
        selectedCitySubject.onNext(city.id)
    }

    override fun setOfflineMapsEnabled(enabled: Boolean) {
        offlineMapsSubject.onNext(enabled)
    }
}
