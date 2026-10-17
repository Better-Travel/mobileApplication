package com.bettertravel.domain.speech

import com.badoo.reaktive.observable.Observable

sealed interface SpeechEvent {
    data class Partial(
        val text: String,
    ) : SpeechEvent

    data class Final(
        val text: String,
    ) : SpeechEvent
}

enum class SpeechFailure {
    PermissionDenied,
    Unavailable,
    NoSpeech,
    Failed,
}

class SpeechRecognitionException(
    val failure: SpeechFailure,
    message: String? = null,
) : Exception(message ?: failure.name)

interface SpeechRecognizing {
    /**
     * Cold stream: subscribing starts the microphone, disposing stops it.
     * Emits [SpeechEvent.Partial] while the user speaks and one [SpeechEvent.Final] before completing.
     * Failures arrive as [SpeechRecognitionException].
     */
    fun listen(languageTag: String): Observable<SpeechEvent>
}
