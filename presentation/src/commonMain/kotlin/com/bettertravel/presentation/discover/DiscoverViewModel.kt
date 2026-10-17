package com.bettertravel.presentation.discover

import com.bettertravel.presentation.ViewModel
import com.bettertravel.presentation.ViewModelContext

interface DiscoverViewModel : ViewModel {
    val titlePrefix: String
    val titleAccent: String
    val subtitle: String
    val inputPlaceholder: String

    fun openChat()

    data class Parameters(
        val context: ViewModelContext,
        val onOpenChat: () -> Unit,
    )
}
