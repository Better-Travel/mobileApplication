package com.bettertravel.presentation.memories

import com.bettertravel.presentation.ViewModel
import com.bettertravel.presentation.ViewModelContext

interface MemoriesViewModel : ViewModel {
    val title: String
    val emptyMessage: String

    data class Parameters(
        val context: ViewModelContext,
    )
}
