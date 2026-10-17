package com.bettertravel.networking

import io.ktor.client.HttpClient
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val USER_AGENT = "BetterTravel/1.0 (Kotlin Multiplatform demo)"

object HttpClientFactory {
    fun create(): HttpClient {
        return HttpClient {
            install(UserAgent) {
                agent = USER_AGENT
            }
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }
}
