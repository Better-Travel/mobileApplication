package com.bettertravel.presentation.memories

import com.bettertravel.common.Deps
import com.bettertravel.localization.StringRepository
import com.bettertravel.presentation.ScreenViewModel

class MemoriesViewModelImpl(
    parameters: MemoriesViewModel.Parameters,
) : ScreenViewModel(parameters.context),
    MemoriesViewModel {
    private val strings: StringRepository = Deps.instance()

    override val title: String = strings.memoriesTitle
    override val emptyMessage: String = strings.memoriesEmptyMessage
}
