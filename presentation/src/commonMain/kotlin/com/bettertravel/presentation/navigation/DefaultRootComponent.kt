package com.bettertravel.presentation.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import com.bettertravel.presentation.ViewModelContext
import com.bettertravel.presentation.aichat.AIChatViewModel
import com.bettertravel.presentation.aichat.AIChatViewModelImpl
import kotlinx.serialization.Serializable

class DefaultRootComponent(
    componentContext: ComponentContext,
) : RootComponent,
    ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Main,
            handleBackButton = true,
            childFactory = ::createChild,
        )

    private fun createChild(
        config: Config,
        context: ComponentContext,
    ): RootComponent.Child {
        return when (config) {
            Config.Main -> {
                val main =
                    DefaultMainComponent(
                        componentContext = context,
                        onOpenChat = { navigation.pushNew(Config.Chat) },
                    )
                RootComponent.Child.Main(main)
            }

            Config.Chat -> {
                // TODO: open the Route tab with the suggested route once the Route screen is built
                val parameters =
                    AIChatViewModel.Parameters(
                        context = ViewModelContext(context),
                        onBack = { navigation.pop() },
                        onPreviewRoute = { },
                        onStartRoute = { },
                    )
                RootComponent.Child.Chat(AIChatViewModelImpl(parameters))
            }
        }
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Main : Config

        @Serializable
        data object Chat : Config
    }
}
