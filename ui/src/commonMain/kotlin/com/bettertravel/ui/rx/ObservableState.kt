package com.bettertravel.ui.rx

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.observeOn
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.scheduler.mainScheduler
import com.badoo.reaktive.subject.behavior.BehaviorObservable

@Composable
fun <T> Observable<T>.collectAsState(initial: T): State<T> {
    val state = remember { mutableStateOf(initial) }
    DisposableEffect(this) {
        val disposable = observeOn(mainScheduler).subscribe(onNext = { value -> state.value = value })
        onDispose { disposable.dispose() }
    }
    return state
}

@Composable
fun <T> BehaviorObservable<T>.collectAsState(): State<T> {
    return collectAsState(initial = value)
}
