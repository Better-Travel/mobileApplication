package com.bettertravel.domainmock.speech

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.doOnBeforeDispose
import com.badoo.reaktive.observable.doOnBeforeSubscribe
import com.badoo.reaktive.subject.publish.PublishSubject
import com.bettertravel.domain.speech.SpeechEvent
import com.bettertravel.domain.speech.SpeechRecognizing

class SpeechRecognizingMock : SpeechRecognizing {
    private var subject: PublishSubject<SpeechEvent>? = null

    var listenCount: Int = 0
        private set
    var isActive: Boolean = false
        private set
    var lastLanguageTag: String? = null
        private set

    override fun listen(languageTag: String): Observable<SpeechEvent> {
        lastLanguageTag = languageTag
        val current = PublishSubject<SpeechEvent>()
        subject = current
        return current
            .doOnBeforeSubscribe {
                listenCount += 1
                isActive = true
            }.doOnBeforeDispose { isActive = false }
    }

    fun emit(event: SpeechEvent) {
        subject?.onNext(event)
    }

    fun complete() {
        isActive = false
        subject?.onComplete()
    }

    fun fail(error: Throwable) {
        isActive = false
        subject?.onError(error)
    }
}
