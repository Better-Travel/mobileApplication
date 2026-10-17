package com.bettertravel.presentation.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
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
                // TODO: open the Route tab with the suggested route once the Route screen is built
                val main =
                    DefaultMainComponent(
                        componentContext = context,
                        onPreviewRoute = { },
                        onStartRoute = { },
                    )
                RootComponent.Child.Main(main)
            }
        }
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Main : Config
    }
}
