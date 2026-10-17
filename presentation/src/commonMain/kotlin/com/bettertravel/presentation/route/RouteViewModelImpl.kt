package com.bettertravel.presentation.route

import com.bettertravel.common.Deps
import com.bettertravel.localization.StringRepository
import com.bettertravel.presentation.ScreenViewModel

class RouteViewModelImpl(
    parameters: RouteViewModel.Parameters,
) : ScreenViewModel(parameters.context),
    RouteViewModel {
    private val strings: StringRepository = Deps.instance()

    override val title: String = strings.routeTitle
    override val emptyMessage: String = strings.routeEmptyMessage
}
