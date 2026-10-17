package com.bettertravel.networking.routes

import com.badoo.reaktive.single.subscribe
import com.bettertravel.domain.map.GeoPoint
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
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

private const val FIRST_LEG = "khatbBglnag@zm@_~A"
private const val SECOND_LEG = "oy_tbBgkqag@kbGvjE"

private val VALHALLA_JSON =
    """{"trip":{"status":0,"legs":[{"shape":"${FIRST_LEG.replace("\\", "\\\\")}"},""" +
        """{"shape":"${SECOND_LEG.replace("\\", "\\\\")}"}]}}"""

class HttpBasedWalkingRoutingTests {
    private var requestUrl: String = ""

    private fun createRouting(body: String): HttpBasedWalkingRouting {
        val engine =
            MockEngine { request ->
                requestUrl = request.url.toString()
                respond(
                    content = body,
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
        return HttpBasedWalkingRouting(client, "http://routing.test")
    }

    private suspend fun HttpBasedWalkingRouting.path(waypoints: List<GeoPoint>): List<GeoPoint> {
        val result = CompletableDeferred<List<GeoPoint>>()
        walkingPath(waypoints).subscribe(
            onSuccess = { result.complete(it) },
            onError = { result.completeExceptionally(it) },
        )
        return result.await()
    }

    @Test
    fun testThat_serverReturnsLegs_pathJoinsThemWithoutDuplicatedPoint() =
        runTest {
            val routing = createRouting(VALHALLA_JSON)

            val path = routing.path(listOf(GeoPoint(52.24975, 21.01218), GeoPoint(52.25315, 21.01044)))

            assertEquals(
                listOf(
                    GeoPoint(52.24975, 21.01218),
                    GeoPoint(52.249, 21.0137),
                    GeoPoint(52.25315, 21.01044),
                ),
                path,
            )
        }

    @Test
    fun testThat_waypointsAreRequested_requestAsksForPedestrianCosting() =
        runTest {
            val routing = createRouting(VALHALLA_JSON)

            routing.path(listOf(GeoPoint(52.24975, 21.01218), GeoPoint(52.25315, 21.01044)))

            assertTrue(requestUrl.startsWith("http://routing.test/route?json="))
            assertTrue(requestUrl.contains("pedestrian"))
            assertTrue(requestUrl.contains("52.24975"))
        }

    @Test
    fun testThat_serverReportsFailure_requestFails() =
        runTest {
            val routing = createRouting("""{"trip":{"status":171,"legs":[]}}""")

            assertFailsWith<IllegalStateException> {
                routing.path(listOf(GeoPoint(0.0, 0.0), GeoPoint(1.0, 1.0)))
            }
        }
}
