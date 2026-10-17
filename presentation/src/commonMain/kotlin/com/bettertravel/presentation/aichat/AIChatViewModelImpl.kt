package com.bettertravel.presentation.aichat

import com.arkivanov.essenty.backhandler.BackCallback
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.single.subscribe
import com.badoo.reaktive.subject.behavior.BehaviorObservable
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.bettertravel.common.Deps
import com.bettertravel.common.rx.ActivityIndicator
import com.bettertravel.common.rx.trackActivity
import com.bettertravel.common.time.currentTimeMillis
import com.bettertravel.common.time.formatClockTime
import com.bettertravel.domain.chat.AIChatting
import com.bettertravel.domain.chat.ChatAuthor
import com.bettertravel.domain.chat.ChatMessage
import com.bettertravel.domain.chat.SuggestedRoute
import com.bettertravel.localization.StringRepository
import com.bettertravel.presentation.ScreenViewModel

class AIChatViewModelImpl(
    private val parameters: AIChatViewModel.Parameters,
) : ScreenViewModel(parameters.context),
    AIChatViewModel {
    private val chatting: AIChatting = Deps.instance()
    private val strings: StringRepository = Deps.instance()

    private val activityIndicator = ActivityIndicator()
    private val messages = mutableListOf<ChatMessage>()
    private var messageCounter = 0

    private val itemsSubject = BehaviorSubject<List<AIChatViewModel.Item>>(emptyList())
    private val loadingSubject = BehaviorSubject(false)
    private val stubSubject = BehaviorSubject<AIChatStubViewModel?>(null)
    private val modeSubject = BehaviorSubject(AIChatMode.Text)
    private val transcriptSubject = BehaviorSubject<List<AIVoiceEntry>>(emptyList())

    private val voiceBackCallback = BackCallback(isEnabled = false) { closeVoice() }

    override val title: String = strings.chatTitle
    override val status: String = strings.chatStatus
    override val items: BehaviorObservable<List<AIChatViewModel.Item>> = itemsSubject
    override val loading: BehaviorObservable<Boolean> = loadingSubject
    override val stub: Observable<AIChatStubViewModel?> = stubSubject
    override val mode: BehaviorObservable<AIChatMode> = modeSubject

    override val inputViewModel: AIChatInputViewModel =
        AIChatInputViewModelImpl(
            AIChatInputViewModel.Parameters(
                context = parameters.context,
                isSending = loadingSubject,
                onSend = { text -> send(text) },
                onVoice = { openVoice() },
            ),
        )

    override val voiceViewModel: AIVoiceViewModel =
        AIVoiceViewModelImpl(
            AIVoiceViewModel.Parameters(
                context = parameters.context,
                transcript = transcriptSubject,
                onPhrase = { phrase -> send(phrase) },
                onClose = { closeVoice() },
            ),
        )

    init {
        parameters.context.backHandler.register(voiceBackCallback)
        activityIndicator.isActive
            .subscribe(onNext = { active -> loadingSubject.onNext(active) })
            .disposeWhenClosed()
        showEmptyStub()
    }

    override fun back() {
        parameters.onBack()
    }

    private fun openVoice() {
        modeSubject.onNext(AIChatMode.Voice)
        voiceBackCallback.isEnabled = true
        (voiceViewModel as AIVoiceViewModelImpl).startListening()
    }

    private fun closeVoice() {
        modeSubject.onNext(AIChatMode.Text)
        voiceBackCallback.isEnabled = false
    }

    private fun send(text: String) {
        if (loadingSubject.value) {
            return
        }
        messages.add(createMessage(ChatAuthor.User, text, null))
        stubSubject.onNext(null)
        refresh()
        requestReply()
    }

    private fun requestReply() {
        chatting
            .reply(messages.toList())
            .trackActivity(activityIndicator)
            .subscribe(
                onSuccess = { reply ->
                    messages.add(createMessage(ChatAuthor.Assistant, reply.text, reply.route))
                    refresh()
                },
                onError = { showErrorStub() },
            ).disposeWhenClosed()
    }

    private fun createMessage(
        author: ChatAuthor,
        text: String,
        route: SuggestedRoute?,
    ): ChatMessage {
        messageCounter += 1
        return ChatMessage(
            id = "message-$messageCounter",
            author = author,
            text = text,
            sentAtMillis = currentTimeMillis(),
            route = route,
        )
    }

    private fun refresh() {
        itemsSubject.onNext(buildItems())
        transcriptSubject.onNext(buildTranscript())
    }

    private fun buildItems(): List<AIChatViewModel.Item> {
        if (messages.isEmpty()) {
            return emptyList()
        }
        val result = mutableListOf<AIChatViewModel.Item>()
        result.add(AIChatViewModel.Item.DateDivider(id = "divider-today", title = strings.chatDateToday))
        messages.forEach { message ->
            val time = formatClockTime(message.sentAtMillis)
            when (message.author) {
                ChatAuthor.User -> {
                    result.add(
                        AIChatViewModel.Item.UserMessage(
                            id = message.id,
                            viewModel = AIUserMessageViewModelImpl(text = message.text, time = time),
                        ),
                    )
                }

                ChatAuthor.Assistant -> {
                    result.add(
                        AIChatViewModel.Item.AssistantMessage(
                            id = message.id,
                            viewModel =
                                AIAssistantMessageViewModelImpl(
                                    text = message.text,
                                    time = time,
                                    route = message.route?.let { route -> createRouteCard(route) },
                                ),
                        ),
                    )
                }
            }
        }
        val last = messages.last()
        if (last.author == ChatAuthor.Assistant && last.route != null) {
            result.add(
                AIChatViewModel.Item.QuickReplies(
                    id = "quick-replies-${last.id}",
                    viewModel =
                        AIQuickRepliesViewModelImpl(
                            title = strings.chatQuickRepliesTitle,
                            replies =
                                listOf(
                                    strings.chatQuickReplyShorter,
                                    strings.chatQuickReplyCafe,
                                    strings.chatQuickReplyStairs,
                                ),
                            onSelect = { reply -> send(reply) },
                        ),
                ),
            )
        }
        return result
    }

    private fun buildTranscript(): List<AIVoiceEntry> {
        return messages.takeLast(TRANSCRIPT_SIZE).map { message ->
            val fromUser = message.author == ChatAuthor.User
            AIVoiceEntry(
                id = message.id,
                speaker = if (fromUser) strings.voiceSpeakerYou else strings.voiceSpeakerBetty,
                text = message.text,
                fromUser = fromUser,
            )
        }
    }

    private fun createRouteCard(route: SuggestedRoute): AIRouteCardViewModel {
        return AIRouteCardViewModelImpl(
            title = route.title,
            duration = formatDuration(route),
            stops = route.stops,
            previewTitle = strings.chatRoutePreview,
            startTitle = strings.chatRouteStart,
            onPreview = { parameters.onPreviewRoute() },
            onStart = { parameters.onStartRoute() },
        )
    }

    private fun formatDuration(route: SuggestedRoute): String {
        val hours = route.durationMinutes / MINUTES_IN_HOUR
        val minutes = route.durationMinutes % MINUTES_IN_HOUR
        val time =
            if (hours > 0) {
                "$hours${strings.unitHourShort} $minutes${strings.unitMinuteShort}"
            } else {
                "$minutes${strings.unitMinuteShort}"
            }
        return "$time • ${route.distanceKm} ${strings.unitKilometerShort}"
    }

    private fun showEmptyStub() {
        stubSubject.onNext(
            AIChatStubViewModelImpl(
                title = strings.chatEmptyTitle,
                message = strings.chatEmptyMessage,
                actionTitle = null,
                suggestions =
                    listOf(
                        strings.chatEmptySuggestionPlan,
                        strings.chatEmptySuggestionCafe,
                        strings.chatEmptySuggestionScenic,
                    ),
                onAction = { },
                onSuggestion = { suggestion -> send(suggestion) },
            ),
        )
    }

    private fun showErrorStub() {
        stubSubject.onNext(
            AIChatStubViewModelImpl(
                title = strings.chatErrorTitle,
                message = strings.chatErrorMessage,
                actionTitle = strings.chatRetry,
                suggestions = emptyList(),
                onAction = {
                    stubSubject.onNext(null)
                    requestReply()
                },
                onSuggestion = { },
            ),
        )
    }

    private companion object {
        const val MINUTES_IN_HOUR = 60
        const val TRANSCRIPT_SIZE = 2
    }
}
