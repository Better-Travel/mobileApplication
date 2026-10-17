package com.bettertravel.ui.preview

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.observableOf
import com.badoo.reaktive.subject.behavior.BehaviorObservable
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.bettertravel.presentation.aichat.AIAssistantMessageViewModel
import com.bettertravel.presentation.aichat.AIAssistantMessageViewModelImpl
import com.bettertravel.presentation.aichat.AIChatInputViewModel
import com.bettertravel.presentation.aichat.AIChatMode
import com.bettertravel.presentation.aichat.AIChatStubViewModel
import com.bettertravel.presentation.aichat.AIChatStubViewModelImpl
import com.bettertravel.presentation.aichat.AIChatViewModel
import com.bettertravel.presentation.aichat.AIQuickRepliesViewModel
import com.bettertravel.presentation.aichat.AIQuickRepliesViewModelImpl
import com.bettertravel.presentation.aichat.AIRouteCardViewModel
import com.bettertravel.presentation.aichat.AIRouteCardViewModelImpl
import com.bettertravel.presentation.aichat.AIUserMessageViewModel
import com.bettertravel.presentation.aichat.AIUserMessageViewModelImpl
import com.bettertravel.presentation.aichat.AIVoiceEntry
import com.bettertravel.presentation.aichat.AIVoiceViewModel

internal object PreviewAIChat {
    fun userMessage(): AIUserMessageViewModel {
        return AIUserMessageViewModelImpl(
            text = "Hey Betty! I'm in Warsaw for the afternoon. I want a scenic walk with great coffee.",
            time = "2:14 PM",
        )
    }

    fun routeCard(): AIRouteCardViewModel {
        return AIRouteCardViewModelImpl(
            title = "Old Town & Courtyards",
            duration = "2h 45m • 3.2 km",
            stops = listOf("Old Town Market Square", "St. John's Vaults & Bell View", "Freta Artisan Café"),
            previewTitle = "Preview on map",
            startTitle = "Start walk",
            onPreview = { },
            onStart = { },
        )
    }

    fun assistantMessage(withRoute: Boolean): AIAssistantMessageViewModel {
        return AIAssistantMessageViewModelImpl(
            text = "I love Warsaw! I've sketched a gentle loop through the Old Town. Here is what I curated:",
            time = "2:14 PM",
            route = if (withRoute) routeCard() else null,
        )
    }

    fun quickReplies(): AIQuickRepliesViewModel {
        return AIQuickRepliesViewModelImpl(
            title = "Suggested adjustments",
            replies = listOf("Make it shorter", "Add a café stop", "Avoid stairs"),
            onSelect = { },
        )
    }

    fun items(): List<AIChatViewModel.Item> {
        return listOf(
            AIChatViewModel.Item.DateDivider("divider", "Today"),
            AIChatViewModel.Item.UserMessage("user-1", userMessage()),
            AIChatViewModel.Item.AssistantMessage("assistant-1", assistantMessage(withRoute = true)),
            AIChatViewModel.Item.QuickReplies("quick-replies", quickReplies()),
        )
    }

    fun emptyStub(): AIChatStubViewModel {
        return AIChatStubViewModelImpl(
            title = "Ask Betty anything",
            message = "Tell Betty where you are and how long you have, and she will plan a walk for you.",
            actionTitle = null,
            suggestions = listOf("Plan 3 hours in Warsaw", "Find a quiet café nearby"),
            onAction = { },
            onSuggestion = { },
        )
    }

    fun errorStub(): AIChatStubViewModel {
        return AIChatStubViewModelImpl(
            title = "Betty lost the trail",
            message = "Something went wrong. Check your connection and try again.",
            actionTitle = "Try again",
            suggestions = emptyList(),
            onAction = { },
            onSuggestion = { },
        )
    }

    fun voiceViewModel(withTranscript: Boolean): AIVoiceViewModel {
        return PreviewVoiceViewModel(withTranscript)
    }

    fun viewModel(
        withMessages: Boolean,
        mode: AIChatMode = AIChatMode.Text,
    ): AIChatViewModel {
        return PreviewAIChatViewModel(withMessages, mode)
    }
}

private class PreviewInputViewModel : AIChatInputViewModel {
    override val placeholder: String = "Ask Betty anything or adjust route..."
    override val text: BehaviorObservable<String> = BehaviorSubject("")
    override val canSend: Observable<Boolean> = observableOf(true)

    override fun onTextChanged(text: String) {
    }

    override fun send() {
    }

    override fun startVoice() {
    }
}

private class PreviewVoiceViewModel(
    withTranscript: Boolean,
) : AIVoiceViewModel {
    override val statusText: Observable<String> = observableOf("Betty is listening...")
    override val isListening: BehaviorObservable<Boolean> = BehaviorSubject(true)
    override val transcriptTitle: String = "Real-time transcript"
    override val liveLabel: String = "Live"
    override val emptyTranscript: String = "Say something or tap a suggestion below."
    override val closeTitle: String = "Back to chat"
    override val transcript: BehaviorObservable<List<AIVoiceEntry>> =
        BehaviorSubject(
            if (withTranscript) {
                listOf(
                    AIVoiceEntry("1", "You", "Find me a quiet café near the Royal Castle.", fromUser = true),
                    AIVoiceEntry(
                        "2",
                        "Betty",
                        "There's an artisan bakery tucked in a courtyard 4 minutes away.",
                        false,
                    ),
                )
            } else {
                emptyList()
            },
        )
    override val suggestions: List<String> = listOf("How far is it?", "Add to route", "Read history")

    override fun startListening() {
    }

    override fun toggleListening() {
    }

    override fun select(suggestion: String) {
    }

    override fun close() {
    }
}

private class PreviewAIChatViewModel(
    withMessages: Boolean,
    initialMode: AIChatMode,
) : AIChatViewModel {
    override val title: String = "BetterTravel"
    override val status: String = "with Betty"
    override val items: BehaviorObservable<List<AIChatViewModel.Item>> =
        BehaviorSubject(if (withMessages) PreviewAIChat.items() else emptyList())
    override val loading: BehaviorObservable<Boolean> = BehaviorSubject(false)
    override val stub: Observable<AIChatStubViewModel?> =
        observableOf(if (withMessages) null else PreviewAIChat.emptyStub())
    override val mode: BehaviorObservable<AIChatMode> = BehaviorSubject(initialMode)
    override val inputViewModel: AIChatInputViewModel = PreviewInputViewModel()
    override val voiceViewModel: AIVoiceViewModel = PreviewVoiceViewModel(withTranscript = withMessages)

    override fun back() {
    }
}
