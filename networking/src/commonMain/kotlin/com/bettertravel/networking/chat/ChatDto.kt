package com.bettertravel.networking.chat

import com.bettertravel.domain.chat.ChatAuthor
import com.bettertravel.domain.chat.ChatMessage
import com.bettertravel.domain.chat.ChatReply
import com.bettertravel.domain.chat.SuggestedRoute
import kotlinx.serialization.Serializable

@Serializable
internal data class ChatRequestDto(
    val messages: List<ChatMessageDto>,
)

@Serializable
internal data class ChatMessageDto(
    val role: String,
    val text: String,
) {
    companion object {
        fun from(message: ChatMessage): ChatMessageDto {
            val role =
                when (message.author) {
                    ChatAuthor.User -> "user"
                    ChatAuthor.Assistant -> "assistant"
                }
            return ChatMessageDto(role = role, text = message.text)
        }
    }
}

@Serializable
internal data class SuggestedRouteDto(
    val title: String,
    val distanceKm: Double,
    val durationMinutes: Int,
    val stops: List<String>,
) {
    fun toDomain(): SuggestedRoute {
        return SuggestedRoute(
            title = title,
            distanceKm = distanceKm,
            durationMinutes = durationMinutes,
            stops = stops,
        )
    }
}

@Serializable
internal data class ChatReplyDto(
    val text: String,
    val route: SuggestedRouteDto? = null,
) {
    fun toDomain(): ChatReply {
        return ChatReply(text = text, route = route?.toDomain())
    }
}
