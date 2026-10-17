package com.bettertravel.mockserver

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.routing.get
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
        }
    }.start(wait = true)
}
