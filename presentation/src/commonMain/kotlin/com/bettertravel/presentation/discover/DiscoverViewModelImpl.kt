package com.bettertravel.presentation.discover

import com.bettertravel.common.Deps
import com.bettertravel.localization.StringRepository
import com.bettertravel.presentation.ScreenViewModel

class DiscoverViewModelImpl(
    private val parameters: DiscoverViewModel.Parameters,
) : ScreenViewModel(parameters.context),
    DiscoverViewModel {
    private val strings: StringRepository = Deps.instance()

    override val titlePrefix: String = strings.discoverTitlePrefix
    override val titleAccent: String = strings.discoverTitleAccent
    override val subtitle: String = strings.discoverSubtitle
    override val inputPlaceholder: String = strings.discoverInputPlaceholder

    override fun openChat() {
        parameters.onOpenChat()
    }
}
