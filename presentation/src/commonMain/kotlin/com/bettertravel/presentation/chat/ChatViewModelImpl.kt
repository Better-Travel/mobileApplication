package com.bettertravel.presentation.chat

import com.bettertravel.common.Deps
import com.bettertravel.localization.StringRepository
import com.bettertravel.presentation.ScreenViewModel

class ChatViewModelImpl(
    private val parameters: ChatViewModel.Parameters,
) : ScreenViewModel(parameters.context),
    ChatViewModel {
    private val strings: StringRepository = Deps.instance()

    override val title: String = strings.chatTitle
    override val status: String = strings.chatStatus
    override val emptyMessage: String = strings.chatEmptyMessage

    override fun back() {
        parameters.onBack()
    }
}
