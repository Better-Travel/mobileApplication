package com.bettertravel.presentation.aichat

import com.badoo.reaktive.completable.subscribe
import com.badoo.reaktive.disposable.SerialDisposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.combineLatest
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.subject.behavior.BehaviorObservable
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.bettertravel.common.Deps
import com.bettertravel.common.platform.PlatformConfig
import com.bettertravel.common.rx.shareReplayLatestUntil
import com.bettertravel.domain.speech.SpeechEvent
import com.bettertravel.domain.speech.SpeechFailure
import com.bettertravel.domain.speech.SpeechRecognitionException
import com.bettertravel.domain.speech.SpeechRecognizing
import com.bettertravel.domain.speech.SpeechSynthesizing
import com.bettertravel.localization.StringRepository
import com.bettertravel.presentation.ScreenViewModel

class AIVoiceViewModelImpl(
    private val parameters: AIVoiceViewModel.Parameters,
) : ScreenViewModel(parameters.context),
    AIVoiceViewModel {
    private val strings: StringRepository = Deps.instance()
    private val recognizing: SpeechRecognizing by lazy { Deps.instance() }
    private val synthesizing: SpeechSynthesizing by lazy { Deps.instance() }
    private val recognition = SerialDisposable()
    private val speech = SerialDisposable()
    private val speakingSubject = BehaviorSubject(false)
    private val listeningSubject = BehaviorSubject(false)
    private val partialSubject = BehaviorSubject<String?>(null)
    private val failureSubject = BehaviorSubject<SpeechFailure?>(null)

    override val isListening: BehaviorObservable<Boolean> = listeningSubject
    override val isSpeaking: BehaviorObservable<Boolean> = speakingSubject
    override val partialText: BehaviorObservable<String?> = partialSubject
    override val route: BehaviorObservable<AIVoiceRoute?> = parameters.route
    override val previewRouteTitle: String = strings.chatRoutePreview
    override val partialSpeaker: String = strings.voiceSpeakerYou
    override val statusText: Observable<String> =
        combineLatest(listeningSubject, failureSubject, speakingSubject) { listening, failure, speaking ->
            when {
                listening -> strings.voiceListening
                speaking -> strings.voiceSpeaking
                failure == SpeechFailure.PermissionDenied -> strings.voiceErrorPermission
                failure == SpeechFailure.Unavailable -> strings.voiceErrorUnavailable
                failure == SpeechFailure.NoSpeech -> strings.voiceNoSpeech
                failure == SpeechFailure.Failed -> strings.voiceErrorFailed
                else -> strings.voicePaused
            }
        }.shareReplayLatestUntil(closed)
    override val transcriptTitle: String = strings.voiceTranscriptTitle
    override val liveLabel: String = strings.voiceLive
    override val emptyTranscript: String = strings.voiceTranscriptEmpty
    override val closeTitle: String = strings.voiceClose
    override val transcript: BehaviorObservable<List<AIVoiceEntry>> = parameters.transcript
    override val suggestions: List<String> =
        listOf(
            strings.voiceSuggestionDistance,
            strings.voiceSuggestionAddToRoute,
            strings.voiceSuggestionHistory,
        )

    init {
        recognition.disposeWhenClosed()
        speech.disposeWhenClosed()
    }

    override fun startListening() {
        if (listeningSubject.value) {
            return
        }
        stopSpeaking()
        failureSubject.onNext(null)
        partialSubject.onNext(null)
        listeningSubject.onNext(true)
        recognition.set(
            recognizing
                .listen(PlatformConfig.languageCode)
                .subscribe(
                    onNext = { event -> handle(event) },
                    onError = { error -> fail(error) },
                    onComplete = { finish() },
                ),
        )
    }

    override fun toggleListening() {
        if (listeningSubject.value) {
            stopListening()
        } else {
            startListening()
        }
    }

    override fun select(suggestion: String) {
        parameters.onPhrase(suggestion)
    }

    override fun previewRoute() {
        stopListening()
        stopSpeaking()
        parameters.onPreviewRoute()
    }

    override fun close() {
        stopListening()
        stopSpeaking()
        parameters.onClose()
    }

    fun speak(text: String) {
        stopSpeaking()
        speakingSubject.onNext(true)
        speech.set(
            synthesizing
                .speak(text, PlatformConfig.languageCode)
                .subscribe(
                    onComplete = { speakingSubject.onNext(false) },
                    onError = { speakingSubject.onNext(false) },
                ),
        )
    }

    fun stopSpeaking() {
        speech.set(null)
        speakingSubject.onNext(false)
    }

    private fun handle(event: SpeechEvent) {
        when (event) {
            is SpeechEvent.Partial -> {
                partialSubject.onNext(event.text)
            }

            is SpeechEvent.Final -> {
                finish()
                if (event.text.isNotBlank()) {
                    parameters.onPhrase(event.text)
                }
            }
        }
    }

    private fun fail(error: Throwable) {
        val failure = (error as? SpeechRecognitionException)?.failure ?: SpeechFailure.Failed
        partialSubject.onNext(null)
        listeningSubject.onNext(false)
        failureSubject.onNext(failure)
    }

    private fun finish() {
        recognition.set(null)
        partialSubject.onNext(null)
        listeningSubject.onNext(false)
    }

    private fun stopListening() {
        recognition.set(null)
        partialSubject.onNext(null)
        listeningSubject.onNext(false)
    }
}
