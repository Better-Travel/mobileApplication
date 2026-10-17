package com.bettertravel.common.rx

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.distinctUntilChanged
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.doOnBeforeFinally
import com.badoo.reaktive.single.doOnBeforeSubscribe
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.decrementAndFetch
import kotlin.concurrent.atomics.incrementAndFetch

@OptIn(ExperimentalAtomicApi::class)
class ActivityIndicator {
    private val activeCount = AtomicInt(0)
    private val countSubject = BehaviorSubject(0)

    val isActive: Observable<Boolean> =
        countSubject
            .map { count -> count > 0 }
            .distinctUntilChanged()

    internal fun increment() {
        countSubject.onNext(activeCount.incrementAndFetch())
    }

    internal fun decrement() {
        countSubject.onNext(activeCount.decrementAndFetch())
    }
}

fun <T> Single<T>.trackActivity(indicator: ActivityIndicator): Single<T> {
    return doOnBeforeSubscribe { indicator.increment() }
        .doOnBeforeFinally { indicator.decrement() }
}
