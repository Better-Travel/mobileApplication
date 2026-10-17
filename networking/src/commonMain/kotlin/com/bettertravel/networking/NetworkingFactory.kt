package com.bettertravel.networking

import com.bettertravel.domain.chat.AIChatting
import com.bettertravel.domain.routes.RoutesRepository
import com.bettertravel.networking.chat.HttpBasedAIChatting
import com.bettertravel.networking.routes.HttpBasedRoutesRepository

object NetworkingFactory {
    fun createRoutesRepository(baseUrl: String): RoutesRepository {
        return HttpBasedRoutesRepository(HttpClientFactory.create(), baseUrl)
    }

    fun createAIChatting(baseUrl: String): AIChatting {
        return HttpBasedAIChatting(HttpClientFactory.create(), baseUrl)
    }
}
