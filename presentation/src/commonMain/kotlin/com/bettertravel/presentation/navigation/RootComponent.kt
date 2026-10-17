package com.bettertravel.presentation.navigation

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.bettertravel.presentation.chat.ChatViewModel

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        class Main(
            val component: MainComponent,
        ) : Child

        class Chat(
            val viewModel: ChatViewModel,
        ) : Child
    }
}
