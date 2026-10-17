package com.bettertravel.networking.chat

import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.single.Single
import com.bettertravel.domain.chat.AIChatting
import com.bettertravel.domain.chat.ChatMessage
import com.bettertravel.domain.chat.ChatReply
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class HttpBasedAIChatting(
    private val client: HttpClient,
    private val baseUrl: String,
) : AIChatting {
    override fun reply(history: List<ChatMessage>): Single<ChatReply> {
        return singleFromCoroutine {
            val request = ChatRequestDto(messages = history.map { message -> ChatMessageDto.from(message) })
            val response: ChatReplyDto =
                client
                    .post("$baseUrl/chat") {
                        contentType(ContentType.Application.Json)
                        setBody(request)
                    }.body()
            response.toDomain()
        }
    }
}
