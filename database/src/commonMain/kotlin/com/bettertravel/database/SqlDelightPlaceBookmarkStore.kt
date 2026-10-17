package com.bettertravel.database

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.completable.completableFromFunction
import com.badoo.reaktive.subject.behavior.BehaviorObservable
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.bettertravel.domain.map.GeoPoint
import com.bettertravel.domain.places.BookmarkedPlace
import com.bettertravel.domain.places.PlaceBookmarking

class SqlDelightPlaceBookmarkStore(
    driverFactory: DatabaseDriverFactory,
) : PlaceBookmarking {
    private val queries = BetterTravelDatabase(driverFactory.create()).bookmarkedPlaceQueries
    private val subject = BehaviorSubject(readAll())

    override val bookmarks: BehaviorObservable<List<BookmarkedPlace>> = subject

    override fun add(place: BookmarkedPlace): Completable {
        return completableFromFunction {
            queries.insertOrReplace(
                id = place.id,
                name = place.name,
                routeTitle = place.routeTitle,
                latitude = place.location.latitude,
                longitude = place.location.longitude,
                savedAtMillis = place.savedAtMillis,
            )
            subject.onNext(readAll())
        }
    }

    override fun remove(id: String): Completable {
        return completableFromFunction {
            queries.deleteById(id)
            subject.onNext(readAll())
        }
    }

    private fun readAll(): List<BookmarkedPlace> {
        return queries.selectAll().executeAsList().map { row ->
            BookmarkedPlace(
                id = row.id,
                name = row.name,
                routeTitle = row.routeTitle,
                location = GeoPoint(latitude = row.latitude, longitude = row.longitude),
                savedAtMillis = row.savedAtMillis,
            )
        }
    }
}
