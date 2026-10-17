package com.bettertravel.domainmock.chat

import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.singleOf
import com.badoo.reaktive.single.singleOfError
import com.bettertravel.domain.chat.AIChatting
import com.bettertravel.domain.chat.ChatMessage
import com.bettertravel.domain.chat.ChatReply

class AIChattingMock(
    var result: Result<ChatReply> = Result.success(ChatReply(text = "Mock reply")),
) : AIChatting {
    val receivedHistories: MutableList<List<ChatMessage>> = mutableListOf()

    override fun reply(history: List<ChatMessage>): Single<ChatReply> {
        receivedHistories.add(history)
        return result.fold(
            onSuccess = { reply -> singleOf(reply) },
            onFailure = { error -> singleOfError(error) },
        )
    }
}
