package com.bettertravel.domain.places

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.subject.behavior.BehaviorObservable

interface PlaceBookmarking {
    val bookmarks: BehaviorObservable<List<BookmarkedPlace>>

    fun add(place: BookmarkedPlace): Completable

    fun remove(id: String): Completable
}
