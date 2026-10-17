package com.bettertravel.presentation.aichat

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.combineLatest
import com.badoo.reaktive.observable.distinctUntilChanged
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.subject.behavior.BehaviorObservable
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.bettertravel.common.Deps
import com.bettertravel.common.rx.shareReplayLatestUntil
import com.bettertravel.localization.StringRepository
import com.bettertravel.presentation.ScreenViewModel

class AIChatInputViewModelImpl(
    private val parameters: AIChatInputViewModel.Parameters,
) : ScreenViewModel(parameters.context),
    AIChatInputViewModel {
    private val strings: StringRepository = Deps.instance()
    private val textSubject = BehaviorSubject("")

    override val placeholder: Observable<String> =
        parameters.hasMessages
            .map { hasMessages -> if (hasMessages) strings.chatInputPlaceholder else strings.chatInputPlaceholderEmpty }
            .distinctUntilChanged()
            .shareReplayLatestUntil(closed)
    override val text: BehaviorObservable<String> = textSubject
    override val suggestions: BehaviorObservable<AIQuickRepliesViewModel?> = parameters.suggestions
    override val canSend: Observable<Boolean> =
        combineLatest(textSubject, parameters.isSending) { value, isSending ->
            value.isNotBlank() && !isSending
        }.distinctUntilChanged().shareReplayLatestUntil(closed)

    override fun onTextChanged(text: String) {
        textSubject.onNext(text)
    }

    override fun send() {
        val message = textSubject.value.trim()
        if (message.isEmpty()) {
            return
        }
        textSubject.onNext("")
        parameters.onSend(message)
    }

    override fun startVoice() {
        parameters.onVoice()
    }
}
