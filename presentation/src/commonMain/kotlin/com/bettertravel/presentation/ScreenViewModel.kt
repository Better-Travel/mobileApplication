package com.bettertravel.presentation

import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.filter
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.observable.take
import com.badoo.reaktive.subject.behavior.BehaviorSubject

abstract class ScreenViewModel(
    context: ViewModelContext,
) : ViewModel {
    private val closedSubject = BehaviorSubject(false)

    protected val closed: Observable<Unit> = closedSubject.filter { it }.map { }

    init {
        context.lifecycle.doOnDestroy {
            closedSubject.onNext(true)
        }
    }

    protected fun Disposable.disposeWhenClosed() {
        closed.take(1).subscribe(onNext = { dispose() })
    }
}
