package com.bettertravel.presentation.details

import com.badoo.reaktive.completable.subscribe
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.subject.behavior.BehaviorObservable
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.bettertravel.common.Deps
import com.bettertravel.common.time.currentTimeMillis
import com.bettertravel.domain.places.BookmarkedPlace
import com.bettertravel.domain.places.PlaceBookmarking
import com.bettertravel.localization.StringRepository
import com.bettertravel.presentation.ScreenViewModel

// TODO: replace the mock place content with data from the server
class StopDetailsViewModelImpl(
    private val parameters: StopDetailsViewModel.Parameters,
) : ScreenViewModel(parameters.context),
    StopDetailsViewModel {
    private val strings: StringRepository = Deps.instance()
    private val bookmarking: PlaceBookmarking = Deps.instance()
    private val placeId = BookmarkedPlace.idOf(parameters.request.name, parameters.request.location)
    private val bookmarkSubject = BehaviorSubject(isBookmarkedNow())

    override val eyebrow: String = strings.appName.uppercase()
    override val screenTitle: String = strings.detailsTitle
    override val isRouteStop: Boolean = !parameters.request.isFromMemories
    override val stopBadge: String =
        if (isRouteStop) {
            "${strings.detailsStop} #${parameters.request.order} ${strings.detailsOf} ${parameters.request.total} · " +
                parameters.request.routeTitle
        } else {
            "${strings.memoriesSavedPlace} · ${parameters.request.routeTitle}"
        }
    override val tags: List<String> = listOf("UNESCO World Heritage", "Free Entry Nave", "Mazovian Gothic")
    override val name: String = parameters.request.name
    override val nativeName: String = "Archikatedra św. Jana Chrzciciela"
    override val rating: String = "4.8"
    override val reviews: String = "1,420 ${strings.detailsReviews}"
    override val visitDuration: StopDetailsMetric = StopDetailsMetric("25 min", strings.detailsIdealVisit)
    override val distance: StopDetailsMetric = StopDetailsMetric("160 m", "From Castle Sq")
    override val secretEyebrow: String = strings.detailsSecretEyebrow
    override val secretTag: String = strings.detailsSecretTag
    override val secretQuote: String =
        "\"Did you know? During WWII, 90% of the cathedral was destroyed, but the subterranean crypts " +
            "holding Poland's last king and Nobel laureate Henryk Sienkiewicz survived intact!\""
    override val essentialsTitle: String = strings.detailsEssentials
    override val essentials: List<StopDetailsEssential> =
        listOf(
            StopDetailsEssential(
                title = strings.detailsOpeningHours,
                trailing = strings.detailsOpenNow,
                body = "Today: 10:00 AM – 6:00 PM",
                note = "Royal Crypts close 30 min before nave (5:30 PM)",
            ),
            StopDetailsEssential(
                title = strings.detailsAdmission,
                trailing = "5 PLN (~€1.20)",
                body = "Free entry for main gothic nave & prayer chapels",
                note = "Small maintenance fee to access royal vaults",
            ),
            StopDetailsEssential(
                title = strings.detailsAddress,
                trailing = strings.detailsMap,
                body = "Świętojańska 8, 00-278 Warszawa",
                note = "Directly connects to Royal Castle via private corridor bridge",
            ),
        )
    override val highlightsTitle: String = strings.detailsHighlights
    override val highlights: List<StopDetailsHighlight> =
        listOf(
            StopDetailsHighlight("Royal Crypts & Sarcophagi", "Final resting place of King Stanisław August…"),
            StopDetailsHighlight("Mazovian Gothic Vaults", "Distinctive high ribbed star vaults reconstructed…"),
            StopDetailsHighlight(
                "Renaissance Monument of Dukes",
                "The striking red marble tomb of the last Mazovian Dukes,…",
            ),
        )
    override val surroundingTitle: String = strings.detailsSurrounding
    override val surroundingSubtitle: String = "Old Town Heritage Path"
    override val nextStop: String = "Next stop: Warsaw Old Town Market (3 min walk)"
    override val keepTitle: String = strings.detailsKeep
    override val swapTitle: String = strings.detailsSwap
    override val removeTitle: String = if (isRouteStop) strings.detailsRemove else strings.detailsRemoveSaved
    override val isBookmarked: BehaviorObservable<Boolean> = bookmarkSubject

    init {
        bookmarking.bookmarks
            .subscribe(onNext = { places -> bookmarkSubject.onNext(places.any { place -> place.id == placeId }) })
            .disposeWhenClosed()
    }

    private fun isBookmarkedNow(): Boolean {
        return bookmarking.bookmarks.value.any { place -> place.id == placeId }
    }

    override fun back() {
        parameters.onBack()
    }

    override fun toggleBookmark() {
        if (bookmarkSubject.value) {
            bookmarking.remove(placeId).subscribe().disposeWhenClosed()
        } else {
            val place =
                BookmarkedPlace(
                    id = placeId,
                    name = parameters.request.name,
                    routeTitle = parameters.request.routeTitle,
                    location = parameters.request.location,
                    savedAtMillis = currentTimeMillis(),
                )
            bookmarking.add(place).subscribe().disposeWhenClosed()
        }
    }

    override fun keep() {
        parameters.onBack()
    }

    override fun swap() {
        parameters.onSwap()
    }

    override fun remove() {
        if (isRouteStop) {
            parameters.onRemove(parameters.request.order - 1)
        } else {
            bookmarking.remove(placeId).subscribe().disposeWhenClosed()
            parameters.onBack()
        }
    }
}
