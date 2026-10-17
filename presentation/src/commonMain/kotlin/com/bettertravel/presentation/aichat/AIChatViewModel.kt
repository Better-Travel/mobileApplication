package com.bettertravel.presentation.aichat

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.subject.behavior.BehaviorObservable
import com.bettertravel.presentation.ViewModel
import com.bettertravel.presentation.ViewModelContext

data class AIChatScrollPosition(
    val index: Int,
    val offset: Int,
)

enum class AIChatMode {
    Text,
    Voice,
}

interface AIChatViewModel : ViewModel {
    val title: String
    val items: BehaviorObservable<List<Item>>
    val loading: BehaviorObservable<Boolean>
    val stub: Observable<AIChatStubViewModel?>
    val mode: BehaviorObservable<AIChatMode>

    val scrollPosition: AIChatScrollPosition
    val inputViewModel: AIChatInputViewModel
    val voiceViewModel: AIVoiceViewModel

    fun onScrollPositionChanged(position: AIChatScrollPosition)

    sealed interface Item {
        val id: String

        data class DateDivider(
            override val id: String,
            val title: String,
        ) : Item

        data class UserMessage(
            override val id: String,
            val viewModel: AIUserMessageViewModel,
        ) : Item

        data class AssistantMessage(
            override val id: String,
            val viewModel: AIAssistantMessageViewModel,
        ) : Item

        data class QuickReplies(
            override val id: String,
            val viewModel: AIQuickRepliesViewModel,
        ) : Item
    }

    data class Parameters(
        val context: ViewModelContext,
        val onPreviewRoute: () -> Unit,
        val onStartRoute: () -> Unit,
    )
}
