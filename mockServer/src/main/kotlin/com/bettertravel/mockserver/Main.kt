package com.bettertravel.mockserver

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.serialization.Serializable

private const val PORT = 8080

@Serializable
data class RouteResponse(
    val id: String,
    val title: String,
    val city: String,
    val distanceKm: Double,
    val durationMinutes: Int,
    val stopsCount: Int,
)

@Serializable
data class ChatMessageRequest(
    val role: String,
    val text: String,
)

@Serializable
data class ChatRequest(
    val messages: List<ChatMessageRequest>,
)

@Serializable
data class SuggestedRouteResponse(
    val title: String,
    val distanceKm: Double,
    val durationMinutes: Int,
    val stops: List<String>,
)

@Serializable
data class ChatReplyResponse(
    val text: String,
    val route: SuggestedRouteResponse? = null,
)

private val oldTownLoop =
    SuggestedRouteResponse(
        title = "Old Town & Courtyards",
        distanceKm = 3.2,
        durationMinutes = 165,
        stops = listOf("Old Town Market Square", "St. John's Vaults & Bell View", "Freta Artisan Café (Courtyard)"),
    )

private fun replyTo(request: ChatRequest): ChatReplyResponse {
    val last =
        request.messages
            .lastOrNull { it.role == "user" }
            ?.text
            .orEmpty()
            .lowercase()
    return when {
        listOf("walk", "route", "warsaw", "hours", "plan").any { last.contains(it) } -> {
            ChatReplyResponse(
                text =
                    "I've sketched a gentle loop through the Old Town and hidden cobblestone paths. " +
                        "Here is what I curated:",
                route = oldTownLoop,
            )
        }

        listOf("shorter", "short").any { last.contains(it) } -> {
            ChatReplyResponse(
                text = "Done. I trimmed the loop to the three best stops, about 2 hours in total.",
                route = oldTownLoop.copy(durationMinutes = 120, distanceKm = 2.4),
            )
        }

        listOf("coffee", "café", "cafe").any { last.contains(it) } -> {
            ChatReplyResponse(
                text = "There's an artisan bakery tucked in a courtyard on Freta Street, about 4 minutes away.",
            )
        }

        else -> {
            ChatReplyResponse(
                text = "Tell me where you are and how much time you have, and I'll plan a walk for you.",
            )
        }
    }
}

private val routes =
    listOf(
        RouteResponse("warsaw-old-town", "Old Town Loop", "Warsaw", 3.4, 112, 4),
        RouteResponse("warsaw-courtyards", "Old Town & Courtyards", "Warsaw", 3.2, 165, 3),
        RouteResponse("prague-castle", "Castle District Walk", "Prague", 4.1, 140, 5),
    )

fun main() {
    embeddedServer(CIO, port = PORT, host = "0.0.0.0") {
        install(ContentNegotiation) {
            json()
        }
        routing {
            get("/routes") {
                call.respond(routes)
            }
            post("/chat") {
                val request = call.receive<ChatRequest>()
                call.respond(replyTo(request))
            }
        }
    }.start(wait = true)
}
