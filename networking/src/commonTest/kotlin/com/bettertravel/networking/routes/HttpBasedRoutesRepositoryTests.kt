package com.bettertravel.networking.routes

import com.badoo.reaktive.single.subscribe
import com.bettertravel.domain.routes.Route
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

private const val ROUTES_JSON =
    """[{"id":"1","title":"Old Town Loop","city":"Warsaw",""" +
        """"distanceKm":3.4,"durationMinutes":112,"stopsCount":4}]"""

class HttpBasedRoutesRepositoryTests {
    @Test
    fun testThat_serverReturnsRoutes_repositoryMapsThemToDomain() =
        runTest {
            val engine =
                MockEngine {
                    respond(
                        content = ROUTES_JSON,
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json"),
                    )
                }
            val client =
                HttpClient(engine) {
                    install(ContentNegotiation) {
                        json(Json { ignoreUnknownKeys = true })
                    }
                }
            val repository = HttpBasedRoutesRepository(client, "http://localhost")
            val result = CompletableDeferred<List<Route>>()

            repository.loadRoutes().subscribe(
                onSuccess = { result.complete(it) },
                onError = { result.completeExceptionally(it) },
            )

            assertEquals(
                listOf(Route("1", "Old Town Loop", "Warsaw", 3.4, 112, 4)),
                result.await(),
            )
        }
}
