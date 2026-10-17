package com.bettertravel.domain.chat

enum class ChatAuthor {
    User,
    Assistant,
}

data class SuggestedRoute(
    val title: String,
    val distanceKm: Double,
    val durationMinutes: Int,
    val stops: List<String>,
)

data class ChatMessage(
    val id: String,
    val author: ChatAuthor,
    val text: String,
    val sentAtMillis: Long,
    val route: SuggestedRoute? = null,
)

data class ChatReply(
    val text: String,
    val route: SuggestedRoute? = null,
)
