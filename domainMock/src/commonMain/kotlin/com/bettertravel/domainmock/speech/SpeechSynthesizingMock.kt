package com.bettertravel.domainmock.speech

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.completable.CompletableCallbacks
import com.badoo.reaktive.completable.completable
import com.badoo.reaktive.disposable.Disposable
import com.bettertravel.domain.speech.SpeechSynthesizing

class SpeechSynthesizingMock : SpeechSynthesizing {
    private var callbacks: CompletableCallbacks? = null

    val spokenTexts: MutableList<String> = mutableListOf()
    var isSpeaking: Boolean = false
        private set

    override fun speak(
        text: String,
        languageTag: String,
    ): Completable {
        return completable { emitter ->
            spokenTexts.add(text)
            isSpeaking = true
            callbacks = emitter
            emitter.setDisposable(
                object : Disposable {
                    override val isDisposed: Boolean
                        get() = !isSpeaking

                    override fun dispose() {
                        isSpeaking = false
                    }
                },
            )
        }
    }

    fun finish() {
        isSpeaking = false
        callbacks?.onComplete()
    }

    fun fail(error: Throwable) {
        isSpeaking = false
        callbacks?.onError(error)
    }
}
