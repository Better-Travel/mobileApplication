package com.bettertravel.networking

import com.bettertravel.domain.routes.RoutesRepository
import com.bettertravel.networking.routes.HttpBasedRoutesRepository

object NetworkingFactory {
    fun createRoutesRepository(baseUrl: String): RoutesRepository {
        return HttpBasedRoutesRepository(HttpClientFactory.create(), baseUrl)
    }
}
