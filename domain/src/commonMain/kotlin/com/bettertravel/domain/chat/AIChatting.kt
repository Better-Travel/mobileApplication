package com.bettertravel.domain.chat

import com.badoo.reaktive.single.Single

interface AIChatting {
    fun reply(history: List<ChatMessage>): Single<ChatReply>
}
