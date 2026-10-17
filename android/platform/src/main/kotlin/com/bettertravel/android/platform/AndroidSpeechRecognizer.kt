package com.bettertravel.android.platform

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.observable
import com.bettertravel.domain.speech.SpeechEvent
import com.bettertravel.domain.speech.SpeechFailure
import com.bettertravel.domain.speech.SpeechRecognitionException
import com.bettertravel.domain.speech.SpeechRecognizing

class AndroidSpeechRecognizer(
    private val context: Context,
) : SpeechRecognizing {
    private val mainHandler = Handler(Looper.getMainLooper())

    override fun listen(languageTag: String): Observable<SpeechEvent> {
        return observable { emitter ->
            var recognizer: SpeechRecognizer? = null
            var disposed = false
            emitter.setDisposable(
                object : Disposable {
                    override val isDisposed: Boolean
                        get() = disposed

                    override fun dispose() {
                        disposed = true
                        mainHandler.post {
                            recognizer?.destroy()
                            recognizer = null
                        }
                    }
                },
            )

            fun start() {
                if (disposed) {
                    return
                }
                if (!SpeechRecognizer.isRecognitionAvailable(context)) {
                    emitter.onError(SpeechRecognitionException(SpeechFailure.Unavailable))
                    return
                }
                val created = SpeechRecognizer.createSpeechRecognizer(context)
                recognizer = created
                created.setRecognitionListener(createListener(emitter))
                created.startListening(createIntent(languageTag))
            }

            mainHandler.post {
                if (MicrophonePermission.isGranted(context)) {
                    start()
                    return@post
                }
                val requester = MicrophonePermission.requester
                if (requester == null) {
                    emitter.onError(SpeechRecognitionException(SpeechFailure.PermissionDenied))
                    return@post
                }
                requester { granted ->
                    if (granted) {
                        start()
                    } else {
                        emitter.onError(SpeechRecognitionException(SpeechFailure.PermissionDenied))
                    }
                }
            }
        }
    }

    private fun createIntent(languageTag: String): Intent {
        return Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageTag)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }
    }

    private fun createListener(
        emitter: com.badoo.reaktive.observable.ObservableEmitter<SpeechEvent>,
    ): RecognitionListener {
        return object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) = Unit

            override fun onBeginningOfSpeech() = Unit

            override fun onRmsChanged(rmsdB: Float) = Unit

            override fun onBufferReceived(buffer: ByteArray?) = Unit

            override fun onEndOfSpeech() = Unit

            override fun onEvent(
                eventType: Int,
                params: Bundle?,
            ) = Unit

            override fun onPartialResults(partialResults: Bundle?) {
                bestText(partialResults)?.let { text -> emitter.onNext(SpeechEvent.Partial(text)) }
            }

            override fun onResults(results: Bundle?) {
                val text = bestText(results)
                if (text == null) {
                    emitter.onError(SpeechRecognitionException(SpeechFailure.NoSpeech))
                } else {
                    emitter.onNext(SpeechEvent.Final(text))
                    emitter.onComplete()
                }
            }

            override fun onError(error: Int) {
                emitter.onError(SpeechRecognitionException(failureOf(error), "SpeechRecognizer error $error"))
            }
        }
    }

    private fun bestText(bundle: Bundle?): String? {
        return bundle
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            ?.firstOrNull()
            ?.takeIf { text -> text.isNotBlank() }
    }

    private fun failureOf(error: Int): SpeechFailure {
        return when (error) {
            SpeechRecognizer.ERROR_NO_MATCH, SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> SpeechFailure.NoSpeech
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> SpeechFailure.PermissionDenied
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY, SpeechRecognizer.ERROR_CLIENT -> SpeechFailure.Failed
            else -> SpeechFailure.Failed
        }
    }
}
