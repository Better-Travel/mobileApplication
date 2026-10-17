package com.bettertravel.presentation.aichat

import com.bettertravel.presentation.ViewModel

interface AIChatStubViewModel : ViewModel {
    val title: String
    val message: String
    val actionTitle: String?
    val suggestions: List<String>

    fun performAction()

    fun select(suggestion: String)
}

class AIChatStubViewModelImpl(
    override val title: String,
    override val message: String,
    override val actionTitle: String?,
    override val suggestions: List<String>,
    private val onAction: () -> Unit,
    private val onSuggestion: (String) -> Unit,
) : AIChatStubViewModel {
    override fun performAction() {
        onAction()
    }

    override fun select(suggestion: String) {
        onSuggestion(suggestion)
    }
}
