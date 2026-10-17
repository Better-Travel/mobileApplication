package com.bettertravel.networking.speech

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
internal data class SynthesizeInputDto(
    val text: String,
)

@Serializable
internal data class SynthesizeVoiceDto(
    val languageCode: String,
    val name: String? = null,
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
internal data class SynthesizeAudioConfigDto(
    @EncodeDefault val audioEncoding: String = "MP3",
)

@Serializable
internal data class SynthesizeRequestDto(
    val input: SynthesizeInputDto,
    val voice: SynthesizeVoiceDto,
    val audioConfig: SynthesizeAudioConfigDto,
)

@Serializable
internal data class SynthesizeResponseDto(
    val audioContent: String,
)
