package com.bettertravel.presentation.aichat

import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.bettertravel.common.Deps
import com.bettertravel.domain.speech.SpeechEvent
import com.bettertravel.domain.speech.SpeechFailure
import com.bettertravel.domain.speech.SpeechRecognitionException
import com.bettertravel.domain.speech.SpeechRecognizing
import com.bettertravel.domain.speech.SpeechSynthesizing
import com.bettertravel.domainmock.speech.SpeechRecognizingMock
import com.bettertravel.domainmock.speech.SpeechSynthesizingMock
import com.bettertravel.localization.GeneratedStringRepository
import com.bettertravel.localization.StringRepository
import com.bettertravel.presentation.ViewModelContext
import com.bettertravel.presentation.createTestComponentContext
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AIVoiceViewModelTests {
    private val recognizer = SpeechRecognizingMock()
    private val synthesizer = SpeechSynthesizingMock()
    private val phrases = mutableListOf<String>()
    private var closeCount = 0
    private var previewCount = 0
    private var status = ""

    @BeforeTest
    fun setUp() {
        Deps.di =
            DI {
                bindSingleton<SpeechRecognizing> { recognizer }
                bindSingleton<SpeechSynthesizing> { synthesizer }
                bindSingleton<StringRepository> { GeneratedStringRepository { "en" } }
            }
    }

    private fun createViewModel(): AIVoiceViewModelImpl {
        val viewModel =
            AIVoiceViewModelImpl(
                AIVoiceViewModel.Parameters(
                    context = ViewModelContext(createTestComponentContext()),
                    transcript = BehaviorSubject(emptyList()),
                    route = BehaviorSubject(null),
                    onPreviewRoute = { previewCount += 1 },
                    onPhrase = { phrase -> phrases.add(phrase) },
                    onClose = { closeCount += 1 },
                ),
            )
        viewModel.statusText.subscribe(onNext = { text -> status = text })
        return viewModel
    }

    @Test
    fun testThat_listeningStarts_recognizerIsActiveWithAppLanguage() {
        val viewModel = createViewModel()

        viewModel.startListening()

        assertTrue(viewModel.isListening.value)
        assertTrue(recognizer.isActive)
        assertEquals("en", recognizer.lastLanguageTag)
        assertEquals("Betty is listening...", status)
    }

    @Test
    fun testThat_partialResultArrives_liveTextIsExposed() {
        val viewModel = createViewModel()
        viewModel.startListening()

        recognizer.emit(SpeechEvent.Partial("find me a caf"))

        assertEquals("find me a caf", viewModel.partialText.value)
    }

    @Test
    fun testThat_finalResultArrives_phraseIsSentAndListeningStops() {
        val viewModel = createViewModel()
        viewModel.startListening()
        recognizer.emit(SpeechEvent.Partial("find me"))

        recognizer.emit(SpeechEvent.Final("find me a café"))

        assertEquals(listOf("find me a café"), phrases)
        assertFalse(viewModel.isListening.value)
        assertNull(viewModel.partialText.value)
        assertFalse(recognizer.isActive)
    }

    @Test
    fun testThat_microphoneIsToggledOff_recognizerIsStopped() {
        val viewModel = createViewModel()
        viewModel.startListening()

        viewModel.toggleListening()

        assertFalse(viewModel.isListening.value)
        assertFalse(recognizer.isActive)
        assertTrue(phrases.isEmpty())
    }

    @Test
    fun testThat_permissionIsDenied_statusExplainsAndListeningStops() {
        val viewModel = createViewModel()
        viewModel.startListening()

        recognizer.fail(SpeechRecognitionException(SpeechFailure.PermissionDenied))

        assertFalse(viewModel.isListening.value)
        assertEquals("Allow microphone and speech access in Settings.", status)
    }

    @Test
    fun testThat_nothingWasHeard_statusAsksToTryAgain() {
        val viewModel = createViewModel()
        viewModel.startListening()

        recognizer.fail(SpeechRecognitionException(SpeechFailure.NoSpeech))

        assertEquals("I didn't catch that. Tap the mic to try again.", status)
    }

    @Test
    fun testThat_listeningStartsAgainAfterAFailure_errorIsCleared() {
        val viewModel = createViewModel()
        viewModel.startListening()
        recognizer.fail(SpeechRecognitionException(SpeechFailure.Unavailable))

        viewModel.startListening()

        assertEquals("Betty is listening...", status)
        assertEquals(2, recognizer.listenCount)
    }

    @Test
    fun testThat_voiceModeIsClosed_recognizerStopsAndCloseIsReported() {
        val viewModel = createViewModel()
        viewModel.startListening()

        viewModel.close()

        assertFalse(recognizer.isActive)
        assertEquals(1, closeCount)
    }

    @Test
    fun testThat_suggestionIsSelected_phraseIsSentWithoutRecognition() {
        val viewModel = createViewModel()

        viewModel.select("How far is it?")

        assertEquals(listOf("How far is it?"), phrases)
        assertEquals(0, recognizer.listenCount)
    }

    @Test
    fun testThat_bettySpeaks_statusSaysSoUntilSpeechFinishes() {
        val viewModel = createViewModel()

        viewModel.speak("Hello there")

        assertTrue(viewModel.isSpeaking.value)
        assertEquals("Betty is speaking...", status)

        synthesizer.finish()

        assertFalse(viewModel.isSpeaking.value)
        assertEquals("Tap the mic to talk", status)
    }

    @Test
    fun testThat_speechFails_speakingEndsQuietly() {
        val viewModel = createViewModel()
        viewModel.speak("Hello there")

        synthesizer.fail(IllegalStateException("offline"))

        assertFalse(viewModel.isSpeaking.value)
    }

    @Test
    fun testThat_listeningStartsWhileBettySpeaks_speechStops() {
        val viewModel = createViewModel()
        viewModel.speak("Hello there")

        viewModel.startListening()

        assertFalse(synthesizer.isSpeaking)
        assertEquals("Betty is listening...", status)
    }

    @Test
    fun testThat_routePreviewIsTapped_listeningAndSpeechStopAndPreviewIsReported() {
        val viewModel = createViewModel()
        viewModel.startListening()
        viewModel.speak("Here is your route")

        viewModel.previewRoute()

        assertFalse(recognizer.isActive)
        assertFalse(synthesizer.isSpeaking)
        assertEquals(1, previewCount)
    }
}
