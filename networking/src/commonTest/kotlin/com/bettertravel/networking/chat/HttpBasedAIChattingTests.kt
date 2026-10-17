package com.bettertravel.networking.chat

import com.badoo.reaktive.single.subscribe
import com.bettertravel.domain.chat.ChatAuthor
import com.bettertravel.domain.chat.ChatMessage
import com.bettertravel.domain.chat.ChatReply
import com.bettertravel.domain.chat.SuggestedRoute
import com.bettertravel.domain.map.GeoPoint
import com.bettertravel.domain.routes.RouteStop
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.toByteArray
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

private const val REPLY_JSON =
    """{"text":"Here is a loop","route":{"title":"Old Town","distanceKm":3.2,""" +
        """"durationMinutes":165,"stops":[{"name":"Market Square","latitude":52.2497,"longitude":21.0122},""" +
        """{"name":"St. John's","latitude":52.249,"longitude":21.0137}]}}"""

class HttpBasedAIChattingTests {
    private var requestBody: String = ""
    private var requestPath: String = ""
    private var requestMethod: HttpMethod = HttpMethod.Get

    private fun createClient(): HttpClient {
        val engine =
            MockEngine { request ->
                requestBody = request.body.toByteArray().decodeToString()
                requestPath = request.url.encodedPath
                requestMethod = request.method
                respond(
                    content = REPLY_JSON,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            }
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }

    private suspend fun requestReply(): ChatReply {
        val chatting = HttpBasedAIChatting(createClient(), "http://localhost")
        val result = CompletableDeferred<ChatReply>()
        val history = listOf(ChatMessage("1", ChatAuthor.User, "Walk in Warsaw", 0L))

        chatting.reply(history).subscribe(
            onSuccess = { result.complete(it) },
            onError = { result.completeExceptionally(it) },
        )

        return result.await()
    }

    @Test
    fun testThat_serverReturnsReply_replyIsMappedToDomain() =
        runTest {
            val reply = requestReply()

            assertEquals(
                ChatReply(
                    text = "Here is a loop",
                    route =
                        SuggestedRoute(
                            "Old Town",
                            3.2,
                            165,
                            listOf(
                                RouteStop("Market Square", GeoPoint(52.2497, 21.0122)),
                                RouteStop("St. John's", GeoPoint(52.249, 21.0137)),
                            ),
                        ),
                ),
                reply,
            )
        }

    @Test
    fun testThat_historyIsSent_requestPostsMessagesToChatEndpoint() =
        runTest {
            requestReply()

            assertEquals("/chat", requestPath)
            assertEquals(HttpMethod.Post, requestMethod)
            assertEquals("""{"messages":[{"role":"user","text":"Walk in Warsaw"}]}""", requestBody)
        }
}
