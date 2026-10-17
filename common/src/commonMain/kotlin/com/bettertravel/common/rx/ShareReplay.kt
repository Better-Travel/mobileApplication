package com.bettertravel.common.rx

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.autoConnect
import com.badoo.reaktive.observable.replay
import com.badoo.reaktive.observable.takeUntil

fun <T> Observable<T>.shareReplayLatestUntil(closed: Observable<*>): Observable<T> {
    return takeUntil(closed)
        .replay(1)
        .autoConnect()
}
