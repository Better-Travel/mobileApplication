package com.bettertravel.presentation.navigation

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.bettertravel.presentation.details.StopDetailsViewModel

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        class StopDetails(
            val viewModel: StopDetailsViewModel,
        ) : Child

        class Main(
            val component: MainComponent,
        ) : Child
    }
}
