package com.bettertravel.networking.speech

internal data class GoogleVoice(
    val languageCode: String,
    val name: String?,
)

internal object GoogleVoices {
    fun forLanguage(languageTag: String): GoogleVoice {
        return when (languageTag.substringBefore("-").lowercase()) {
            "fr" -> GoogleVoice("fr-FR", "fr-FR-Neural2-A")
            "en" -> GoogleVoice("en-US", "en-US-Neural2-F")
            else -> GoogleVoice(languageTag, null)
        }
    }
}
