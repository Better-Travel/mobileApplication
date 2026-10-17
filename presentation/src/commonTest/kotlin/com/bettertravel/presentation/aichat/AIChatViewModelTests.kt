package com.bettertravel.presentation.aichat

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.subscribe
import com.bettertravel.common.Deps
import com.bettertravel.domain.chat.AIChatting
import com.bettertravel.domain.chat.ChatAuthor
import com.bettertravel.domain.chat.ChatReply
import com.bettertravel.domain.chat.SuggestedRoute
import com.bettertravel.domainmock.chat.AIChattingMock
import com.bettertravel.localization.GeneratedStringRepository
import com.bettertravel.localization.StringRepository
import com.bettertravel.presentation.ViewModelContext
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AIChatViewModelTests {
    private val chatting = AIChattingMock()
    private val backDispatcher = BackDispatcher()
    private var stub: AIChatStubViewModel? = null

    private val route =
        SuggestedRoute(
            title = "Old Town & Courtyards",
            distanceKm = 3.2,
            durationMinutes = 165,
            stops = listOf("Market Square", "St. John's"),
        )

    @BeforeTest
    fun setUp() {
        Deps.di =
            DI {
                bindSingleton<AIChatting> { chatting }
                bindSingleton<StringRepository> { GeneratedStringRepository { "en" } }
            }
    }

    private fun createViewModel(): AIChatViewModelImpl {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()
        val context = DefaultComponentContext(lifecycle, backHandler = backDispatcher)
        val viewModel =
            AIChatViewModelImpl(
                AIChatViewModel.Parameters(
                    context = ViewModelContext(context),
                    onPreviewRoute = { },
                    onStartRoute = { },
                ),
            )
        viewModel.stub.observe { stub = it }
        return viewModel
    }

    private fun <T> Observable<T>.observe(onValue: (T) -> Unit) {
        subscribe(onNext = onValue)
    }

    private fun AIChatViewModel.send(text: String) {
        inputViewModel.onTextChanged(text)
        inputViewModel.send()
    }

    @Test
    fun testThat_chatIsOpened_emptyStubShowsTheDiscoverLanding() {
        val viewModel = createViewModel()

        assertEquals("Let's plan your trip", stub?.title)
        assertEquals("Together", stub?.accentTitle)
        assertNull(stub?.actionTitle)
        assertTrue(viewModel.items.value.isEmpty())
    }

    @Test
    fun testThat_noMessagesYet_placeholderAsksWhereAndHowLong() {
        val viewModel = createViewModel()
        var placeholder = ""
        viewModel.inputViewModel.placeholder.observe { placeholder = it }

        assertEquals("Tell BetterTravel where & how long...", placeholder)

        viewModel.send("Hello")

        assertEquals("Ask Betty anything or adjust route...", placeholder)
    }

    @Test
    fun testThat_userSendsMessage_userAndAssistantMessagesAreListed() {
        chatting.result = Result.success(ChatReply(text = "Here is a plan"))
        val viewModel = createViewModel()

        viewModel.send("Plan 3 hours in Warsaw")

        val items = viewModel.items.value
        assertEquals(3, items.size)
        assertTrue(items[0] is AIChatViewModel.Item.DateDivider)
        assertEquals("Plan 3 hours in Warsaw", (items[1] as AIChatViewModel.Item.UserMessage).viewModel.text)
        assertEquals("Here is a plan", (items[2] as AIChatViewModel.Item.AssistantMessage).viewModel.text)
        assertNull(stub)
        assertFalse(viewModel.loading.value)
    }

    @Test
    fun testThat_userSendsMessage_historyContainsTheMessageForTheAssistant() {
        val viewModel = createViewModel()

        viewModel.send("Hello")

        val history = chatting.receivedHistories.single()
        assertEquals(listOf(ChatAuthor.User), history.map { it.author })
        assertEquals("Hello", history.single().text)
    }

    @Test
    fun testThat_inputIsBlank_nothingIsSent() {
        val viewModel = createViewModel()

        viewModel.send("   ")

        assertTrue(chatting.receivedHistories.isEmpty())
        assertTrue(viewModel.items.value.isEmpty())
    }

    @Test
    fun testThat_messageIsSent_inputIsCleared() {
        val viewModel = createViewModel()

        viewModel.send("Hello")

        assertEquals("", viewModel.inputViewModel.text.value)
    }

    @Test
    fun testThat_replyFails_errorStubIsShownAndRetryRequestsAgain() {
        chatting.result = Result.failure(IllegalStateException("offline"))
        val viewModel = createViewModel()

        viewModel.send("Hello")

        assertEquals("Betty lost the trail", stub?.title)
        assertEquals("Try again", stub?.actionTitle)

        chatting.result = Result.success(ChatReply(text = "Back online"))
        stub?.performAction()

        assertEquals(2, chatting.receivedHistories.size)
        assertNull(stub)
        assertEquals(3, viewModel.items.value.size)
    }

    @Test
    fun testThat_replyHasRoute_routeCardIsListedAndSuggestionsAreAttachedToTheInput() {
        chatting.result = Result.success(ChatReply(text = "Here is a loop", route = route))
        val viewModel = createViewModel()

        viewModel.send("Plan a walk")

        val assistant = viewModel.items.value[2] as AIChatViewModel.Item.AssistantMessage
        val card = assertNotNull(assistant.viewModel.route)
        assertEquals("Old Town & Courtyards", card.title)
        assertEquals("2h 45m • 3.2 km", card.duration)
        assertEquals(2, card.stops.size)
        assertTrue(
            viewModel.items.value.none {
                it !is AIChatViewModel.Item.DateDivider &&
                    it !is AIChatViewModel.Item.UserMessage &&
                    it !is AIChatViewModel.Item.AssistantMessage
            },
        )
        val suggestions = assertNotNull(viewModel.inputViewModel.suggestions.value)
        assertEquals(listOf("Make it shorter", "Add a café stop", "Avoid stairs"), suggestions.replies)
    }

    @Test
    fun testThat_replyHasNoRoute_noSuggestionsAreShown() {
        chatting.result = Result.success(ChatReply(text = "Just a tip"))
        val viewModel = createViewModel()

        viewModel.send("Coffee?")

        assertNull(viewModel.inputViewModel.suggestions.value)
    }

    @Test
    fun testThat_newMessageIsSent_suggestionsAreCleared() {
        chatting.result = Result.success(ChatReply(text = "Here is a loop", route = route))
        val viewModel = createViewModel()
        viewModel.send("Plan a walk")
        chatting.result = Result.success(ChatReply(text = "Just a tip"))

        viewModel.send("Coffee?")

        assertNull(viewModel.inputViewModel.suggestions.value)
    }

    @Test
    fun testThat_quickReplyIsSelected_itIsSentAsUserMessage() {
        chatting.result = Result.success(ChatReply(text = "Here is a loop", route = route))
        val viewModel = createViewModel()
        viewModel.send("Plan a walk")
        val suggestions = assertNotNull(viewModel.inputViewModel.suggestions.value)

        suggestions.select("Make it shorter")

        val userTexts =
            viewModel.items.value
                .filterIsInstance<AIChatViewModel.Item.UserMessage>()
                .map { it.viewModel.text }
        assertEquals(listOf("Plan a walk", "Make it shorter"), userTexts)
    }

    @Test
    fun testThat_voiceIsStarted_modeIsVoiceAndMicIsListening() {
        val viewModel = createViewModel()

        viewModel.inputViewModel.startVoice()

        assertEquals(AIChatMode.Voice, viewModel.mode.value)
        assertTrue(viewModel.voiceViewModel.isListening.value)
    }

    @Test
    fun testThat_voiceIsClosed_modeIsTextAgain() {
        val viewModel = createViewModel()
        viewModel.inputViewModel.startVoice()

        viewModel.voiceViewModel.close()

        assertEquals(AIChatMode.Text, viewModel.mode.value)
        assertFalse(viewModel.voiceViewModel.isListening.value)
    }

    @Test
    fun testThat_systemBackIsPressedInVoiceMode_voiceModeIsClosed() {
        val viewModel = createViewModel()
        viewModel.inputViewModel.startVoice()

        backDispatcher.back()

        assertEquals(AIChatMode.Text, viewModel.mode.value)
    }

    @Test
    fun testThat_micIsToggled_listeningStateFlips() {
        val viewModel = createViewModel()
        viewModel.inputViewModel.startVoice()

        viewModel.voiceViewModel.toggleListening()

        assertFalse(viewModel.voiceViewModel.isListening.value)
    }

    @Test
    fun testThat_voiceSuggestionIsSelected_transcriptContainsConversation() {
        chatting.result = Result.success(ChatReply(text = "About 300 meters"))
        val viewModel = createViewModel()
        viewModel.inputViewModel.startVoice()

        viewModel.voiceViewModel.select("How far is it?")

        val transcript = viewModel.voiceViewModel.transcript.value
        assertEquals(listOf("You", "Betty"), transcript.map { it.speaker })
        assertEquals("About 300 meters", transcript.last().text)
    }

    @Test
    fun testThat_chatIsOpened_scrollPositionStartsAtTheTop() {
        val viewModel = createViewModel()

        assertEquals(AIChatScrollPosition(index = 0, offset = 0), viewModel.scrollPosition)
    }

    @Test
    fun testThat_listIsScrolled_positionIsStoredForRestoring() {
        val viewModel = createViewModel()

        viewModel.onScrollPositionChanged(AIChatScrollPosition(index = 4, offset = 120))

        assertEquals(AIChatScrollPosition(index = 4, offset = 120), viewModel.scrollPosition)
    }
}
