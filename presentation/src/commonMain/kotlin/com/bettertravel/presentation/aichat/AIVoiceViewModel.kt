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

interface AIVoiceViewModel : ViewModel {
    val statusText: Observable<String>
    val isListening: BehaviorObservable<Boolean>
    val transcriptTitle: String
    val liveLabel: String
    val emptyTranscript: String
    val closeTitle: String
    val transcript: BehaviorObservable<List<AIVoiceEntry>>
    val suggestions: List<String>

    fun startListening()

    fun toggleListening()

    fun select(suggestion: String)

    fun close()

    data class Parameters(
        val context: ViewModelContext,
        val transcript: BehaviorObservable<List<AIVoiceEntry>>,
        val onPhrase: (String) -> Unit,
        val onClose: () -> Unit,
    )
}
