package com.bettertravel.presentation.aichat

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.subject.behavior.BehaviorObservable
import com.bettertravel.presentation.ViewModel
import com.bettertravel.presentation.ViewModelContext

data class AIVoiceEntry(
    val id: String,
    val speaker: String,
    val text: String,
    val fromUser: Boolean,
)

data class AIVoiceRoute(
    val title: String,
    val details: String,
)

interface AIVoiceViewModel : ViewModel {
    val statusText: Observable<String>
    val isListening: BehaviorObservable<Boolean>
    val isSpeaking: BehaviorObservable<Boolean>
    val partialText: BehaviorObservable<String?>
    val route: BehaviorObservable<AIVoiceRoute?>
    val previewRouteTitle: String
    val partialSpeaker: String
    val transcriptTitle: String
    val liveLabel: String
    val emptyTranscript: String
    val closeTitle: String
    val transcript: BehaviorObservable<List<AIVoiceEntry>>
    val suggestions: List<String>

    fun startListening()

    fun toggleListening()

    fun select(suggestion: String)

    fun previewRoute()

    fun close()

    data class Parameters(
        val context: ViewModelContext,
        val transcript: BehaviorObservable<List<AIVoiceEntry>>,
        val route: BehaviorObservable<AIVoiceRoute?>,
        val onPreviewRoute: () -> Unit,
        val onPhrase: (String) -> Unit,
        val onClose: () -> Unit,
    )
}
