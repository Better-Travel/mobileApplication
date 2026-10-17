package com.bettertravel.presentation.route

import com.bettertravel.presentation.ViewModel
import com.bettertravel.presentation.ViewModelContext

interface RouteViewModel : ViewModel {
    val title: String
    val emptyMessage: String

    data class Parameters(
        val context: ViewModelContext,
    )
}
