package com.bettertravel.application

import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.ObservableEmitter
import com.badoo.reaktive.observable.observable
import com.bettertravel.domain.speech.SpeechEvent
import com.bettertravel.domain.speech.SpeechFailure
import com.bettertravel.domain.speech.SpeechRecognitionException
import com.bettertravel.domain.speech.SpeechRecognizing
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFAudio.AVAudioEngine
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryRecord
import platform.AVFAudio.AVAudioSessionModeMeasurement
import platform.AVFAudio.AVAudioSessionRecordPermissionGranted
import platform.AVFAudio.AVAudioSessionSetActiveOptionNotifyOthersOnDeactivation
import platform.AVFAudio.setActive
import platform.Foundation.NSLocale
import platform.Speech.SFSpeechAudioBufferRecognitionRequest
import platform.Speech.SFSpeechRecognitionTask
import platform.Speech.SFSpeechRecognizer
import platform.Speech.SFSpeechRecognizerAuthorizationStatus
import platform.darwin.DISPATCH_TIME_NOW
import platform.darwin.dispatch_after
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import platform.darwin.dispatch_time

private const val SILENCE_TIMEOUT_NANOS = 1_500_000_000L
private const val BUFFER_SIZE = 1024u

@OptIn(ExperimentalForeignApi::class)
class IosSpeechRecognizer : SpeechRecognizing {
    override fun listen(languageTag: String): Observable<SpeechEvent> {
        return observable { emitter ->
            val session = RecognitionSession(languageTag, emitter)
            emitter.setDisposable(
                object : Disposable {
                    override val isDisposed: Boolean
                        get() = session.isStopped

                    override fun dispose() {
                        onMain { session.stop() }
                    }
                },
            )
            onMain { session.begin() }
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private class RecognitionSession(
    private val languageTag: String,
    private val emitter: ObservableEmitter<SpeechEvent>,
) {
    private val engine = AVAudioEngine()
    private val request = SFSpeechAudioBufferRecognitionRequest()
    private var task: SFSpeechRecognitionTask? = null
    private var activityCounter = 0
    var isStopped = false
        private set

    fun begin() {
        SFSpeechRecognizer.requestAuthorization { status ->
            onMain {
                if (status != SFSpeechRecognizerAuthorizationStatus.SFSpeechRecognizerAuthorizationStatusAuthorized) {
                    fail(SpeechFailure.PermissionDenied)
                } else {
                    requestMicrophone()
                }
            }
        }
    }

    private fun requestMicrophone() {
        val session = AVAudioSession.sharedInstance()
        if (session.recordPermission == AVAudioSessionRecordPermissionGranted) {
            start()
            return
        }
        session.requestRecordPermission { granted ->
            onMain {
                if (granted) {
                    start()
                } else {
                    fail(SpeechFailure.PermissionDenied)
                }
            }
        }
    }

    private fun start() {
        if (isStopped) {
            return
        }
        val recognizer = SFSpeechRecognizer(locale = NSLocale(localeIdentifier = languageTag))
        if (!recognizer.isAvailable()) {
            fail(SpeechFailure.Unavailable)
            return
        }
        val session = AVAudioSession.sharedInstance()
        session.setCategory(
            AVAudioSessionCategoryRecord,
            mode = AVAudioSessionModeMeasurement,
            options = 0u,
            error = null,
        )
        session.setActive(true, withOptions = 0u, error = null)
        request.shouldReportPartialResults = true
        val input = engine.inputNode
        input.installTapOnBus(0u, BUFFER_SIZE, input.outputFormatForBus(0u)) { buffer, _ ->
            buffer?.let { request.appendAudioPCMBuffer(it) }
        }
        engine.prepare()
        if (!engine.startAndReturnError(null)) {
            fail(SpeechFailure.Failed)
            return
        }
        task =
            recognizer.recognitionTaskWithRequest(request) { result, error ->
                onMain { handle(result?.bestTranscription?.formattedString, result?.isFinal() == true, error != null) }
            }
        scheduleSilenceCheck()
    }

    private fun handle(
        text: String?,
        isFinal: Boolean,
        hasError: Boolean,
    ) {
        if (isStopped) {
            return
        }
        if (text != null && text.isNotBlank()) {
            activityCounter += 1
            if (isFinal) {
                emitter.onNext(SpeechEvent.Final(text))
                complete()
            } else {
                emitter.onNext(SpeechEvent.Partial(text))
                scheduleSilenceCheck()
            }
        } else if (isFinal || hasError) {
            fail(SpeechFailure.NoSpeech)
        }
    }

    private fun scheduleSilenceCheck() {
        val expected = activityCounter
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, SILENCE_TIMEOUT_NANOS), dispatch_get_main_queue()) {
            if (!isStopped && expected == activityCounter) {
                request.endAudio()
            }
        }
    }

    private fun complete() {
        stop()
        emitter.onComplete()
    }

    private fun fail(failure: SpeechFailure) {
        if (isStopped) {
            return
        }
        stop()
        emitter.onError(SpeechRecognitionException(failure))
    }

    fun stop() {
        if (isStopped) {
            return
        }
        isStopped = true
        task?.cancel()
        if (engine.running) {
            engine.stop()
            engine.inputNode.removeTapOnBus(0u)
        }
        request.endAudio()
        AVAudioSession.sharedInstance().setActive(
            false,
            withOptions = AVAudioSessionSetActiveOptionNotifyOthersOnDeactivation,
            error = null,
        )
    }
}

private fun onMain(block: () -> Unit) {
    dispatch_async(dispatch_get_main_queue()) { block() }
}
