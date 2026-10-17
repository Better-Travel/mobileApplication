package com.bettertravel.presentation.aichat

import com.bettertravel.presentation.ViewModel

interface AIUserMessageViewModel : ViewModel {
    val text: String
    val time: String
}

interface AIRouteCardViewModel : ViewModel {
    val title: String
    val duration: String
    val stops: List<String>
    val previewTitle: String
    val startTitle: String

    fun preview()

    fun start()
}

interface AIAssistantMessageViewModel : ViewModel {
    val text: String
    val time: String
    val route: AIRouteCardViewModel?
}

interface AIQuickRepliesViewModel : ViewModel {
    val title: String
    val replies: List<String>

    fun select(reply: String)
}

class AIUserMessageViewModelImpl(
    override val text: String,
    override val time: String,
) : AIUserMessageViewModel

class AIRouteCardViewModelImpl(
    override val title: String,
    override val duration: String,
    override val stops: List<String>,
    override val previewTitle: String,
    override val startTitle: String,
    private val onPreview: () -> Unit,
    private val onStart: () -> Unit,
) : AIRouteCardViewModel {
    override fun preview() {
        onPreview()
    }

    override fun start() {
        onStart()
    }
}

class AIAssistantMessageViewModelImpl(
    override val text: String,
    override val time: String,
    override val route: AIRouteCardViewModel?,
) : AIAssistantMessageViewModel

class AIQuickRepliesViewModelImpl(
    override val title: String,
    override val replies: List<String>,
    private val onSelect: (String) -> Unit,
) : AIQuickRepliesViewModel {
    override fun select(reply: String) {
        onSelect(reply)
    }
}
