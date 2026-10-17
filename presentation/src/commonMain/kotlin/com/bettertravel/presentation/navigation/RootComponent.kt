package com.bettertravel.presentation.navigation

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.bettertravel.presentation.routes.RoutesListViewModel

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        class RoutesList(
            val viewModel: RoutesListViewModel,
        ) : Child
    }
}
