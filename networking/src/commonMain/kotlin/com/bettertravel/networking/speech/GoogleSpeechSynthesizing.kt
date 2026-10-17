package com.bettertravel.networking.speech

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.completable.completableFromFunction
import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.single.flatMapCompletable
import com.bettertravel.domain.speech.AudioPlaying
import com.bettertravel.domain.speech.SpeechFailure
import com.bettertravel.domain.speech.SpeechRecognitionException
import com.bettertravel.domain.speech.SpeechSynthesizing
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

private const val ENDPOINT = "https://texttospeech.googleapis.com/v1/text:synthesize"

class GoogleSpeechSynthesizing(
    private val client: HttpClient,
    private val apiKey: String,
    private val player: AudioPlaying,
) : SpeechSynthesizing {
    override fun speak(
        text: String,
        languageTag: String,
    ): Completable {
        if (apiKey.isBlank()) {
            return completableFromFunction { throw SpeechRecognitionException(SpeechFailure.Unavailable, "No TTS key") }
        }
        return singleFromCoroutine { synthesize(text, languageTag) }
            .flatMapCompletable { audio -> player.play(audio) }
    }

    @OptIn(ExperimentalEncodingApi::class)
    private suspend fun synthesize(
        text: String,
        languageTag: String,
    ): ByteArray {
        val voice = GoogleVoices.forLanguage(languageTag)
        val response: SynthesizeResponseDto =
            client
                .post(ENDPOINT) {
                    parameter("key", apiKey)
                    contentType(ContentType.Application.Json)
                    setBody(
                        SynthesizeRequestDto(
                            input = SynthesizeInputDto(text),
                            voice = SynthesizeVoiceDto(languageCode = voice.languageCode, name = voice.name),
                            audioConfig = SynthesizeAudioConfigDto(),
                        ),
                    )
                }.body()
        return Base64.decode(response.audioContent)
    }
}
