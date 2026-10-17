package com.bettertravel.presentation.chat

import com.bettertravel.presentation.ViewModel
import com.bettertravel.presentation.ViewModelContext

interface ChatViewModel : ViewModel {
    val title: String
    val status: String
    val emptyMessage: String

    fun back()

    data class Parameters(
        val context: ViewModelContext,
        val onBack: () -> Unit,
    )
}
