package com.bettertravel.presentation.aichat

import com.bettertravel.presentation.ViewModel

interface AIChatStubViewModel : ViewModel {
    val title: String
    val accentTitle: String?
    val message: String
    val actionTitle: String?

    fun performAction()
}

class AIChatStubViewModelImpl(
    override val title: String,
    override val accentTitle: String?,
    override val message: String,
    override val actionTitle: String?,
    private val onAction: () -> Unit,
) : AIChatStubViewModel {
    override fun performAction() {
        onAction()
    }
}
