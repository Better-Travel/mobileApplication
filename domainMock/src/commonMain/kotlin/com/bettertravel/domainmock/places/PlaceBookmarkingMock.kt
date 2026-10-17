package com.bettertravel.domainmock.places

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.completable.completableFromFunction
import com.badoo.reaktive.subject.behavior.BehaviorObservable
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.bettertravel.domain.places.BookmarkedPlace
import com.bettertravel.domain.places.PlaceBookmarking

class PlaceBookmarkingMock(
    initial: List<BookmarkedPlace> = emptyList(),
) : PlaceBookmarking {
    private val subject = BehaviorSubject(initial)

    override val bookmarks: BehaviorObservable<List<BookmarkedPlace>> = subject

    override fun add(place: BookmarkedPlace): Completable {
        return completableFromFunction {
            subject.onNext(listOf(place) + subject.value.filter { existing -> existing.id != place.id })
        }
    }

    override fun remove(id: String): Completable {
        return completableFromFunction {
            subject.onNext(subject.value.filter { existing -> existing.id != id })
        }
    }
}
