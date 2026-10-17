package com.bettertravel.presentation.aichat

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.subject.behavior.BehaviorObservable
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.bettertravel.common.Deps
import com.bettertravel.common.rx.shareReplayLatestUntil
import com.bettertravel.localization.StringRepository
import com.bettertravel.presentation.ScreenViewModel

class AIVoiceViewModelImpl(
    private val parameters: AIVoiceViewModel.Parameters,
) : ScreenViewModel(parameters.context),
    AIVoiceViewModel {
    private val strings: StringRepository = Deps.instance()
    private val listeningSubject = BehaviorSubject(false)

    override val isListening: BehaviorObservable<Boolean> = listeningSubject
    override val statusText: Observable<String> =
        listeningSubject
            .map { listening -> if (listening) strings.voiceListening else strings.voicePaused }
            .shareReplayLatestUntil(closed)
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

    override fun startListening() {
        listeningSubject.onNext(true)
    }

    override fun toggleListening() {
        listeningSubject.onNext(!listeningSubject.value)
    }

    override fun select(suggestion: String) {
        parameters.onPhrase(suggestion)
    }

    override fun close() {
        listeningSubject.onNext(false)
        parameters.onClose()
    }
}
