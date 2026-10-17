package com.bettertravel.domain.speech

import com.badoo.reaktive.completable.Completable

interface SpeechSynthesizing {
    /**
     * Cold: subscribing starts speaking, completion means the speech finished, disposing stops it.
     * Failures arrive as [SpeechRecognitionException] with [SpeechFailure.Unavailable] or [SpeechFailure.Failed].
     */
    fun speak(
        text: String,
        languageTag: String,
    ): Completable
}

interface AudioPlaying {
    /** Cold: subscribing plays the encoded audio (MP3), completion means playback finished, disposing stops it. */
    fun play(audio: ByteArray): Completable
}
