package com.bettertravel.networking

import com.bettertravel.domain.chat.AIChatting
import com.bettertravel.domain.map.MapTileLoading
import com.bettertravel.domain.routes.RoutesRepository
import com.bettertravel.domain.routes.WalkingRouting
import com.bettertravel.domain.speech.AudioPlaying
import com.bettertravel.domain.speech.SpeechSynthesizing
import com.bettertravel.networking.chat.HttpBasedAIChatting
import com.bettertravel.networking.map.HttpBasedMapTileLoading
import com.bettertravel.networking.routes.HttpBasedRoutesRepository
import com.bettertravel.networking.routes.HttpBasedWalkingRouting
import com.bettertravel.networking.speech.GoogleSpeechSynthesizing

object NetworkingFactory {
    fun createRoutesRepository(baseUrl: String): RoutesRepository {
        return HttpBasedRoutesRepository(HttpClientFactory.create(), baseUrl)
    }

    fun createWalkingRouting(baseUrl: String): WalkingRouting {
        return HttpBasedWalkingRouting(HttpClientFactory.create(), baseUrl)
    }

    fun createMapTileLoading(
        urlTemplate: String,
        attribution: String,
    ): MapTileLoading {
        return HttpBasedMapTileLoading(HttpClientFactory.create(), urlTemplate, attribution)
    }

    fun createSpeechSynthesizing(
        apiKey: String,
        player: AudioPlaying,
    ): SpeechSynthesizing {
        return GoogleSpeechSynthesizing(HttpClientFactory.create(), apiKey, player)
    }

    fun createAIChatting(baseUrl: String): AIChatting {
        return HttpBasedAIChatting(HttpClientFactory.create(), baseUrl)
    }
}
