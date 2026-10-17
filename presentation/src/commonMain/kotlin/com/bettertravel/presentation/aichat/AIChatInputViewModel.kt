package com.bettertravel.presentation.aichat

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.subject.behavior.BehaviorObservable
import com.bettertravel.presentation.ViewModel
import com.bettertravel.presentation.ViewModelContext

interface AIChatInputViewModel : ViewModel {
    val placeholder: Observable<String>
    val text: BehaviorObservable<String>
    val canSend: Observable<Boolean>

    fun onTextChanged(text: String)

    fun send()

    fun startVoice()

    data class Parameters(
        val context: ViewModelContext,
        val isSending: Observable<Boolean>,
        val hasMessages: Observable<Boolean>,
        val onSend: (String) -> Unit,
        val onVoice: () -> Unit,
    )
}
