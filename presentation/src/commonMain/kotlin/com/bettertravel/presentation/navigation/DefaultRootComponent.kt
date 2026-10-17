package com.bettertravel.presentation.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.bettertravel.presentation.ViewModelContext
import com.bettertravel.presentation.routes.RoutesListViewModel
import com.bettertravel.presentation.routes.RoutesListViewModelImpl
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
            initialConfiguration = Config.RoutesList,
            handleBackButton = true,
            childFactory = ::createChild,
        )

    private fun createChild(
        config: Config,
        context: ComponentContext,
    ): RootComponent.Child {
        return when (config) {
            Config.RoutesList -> {
                val parameters =
                    RoutesListViewModel.Parameters(
                        context = ViewModelContext(context),
                        onRouteSelected = { },
                    )
                RootComponent.Child.RoutesList(RoutesListViewModelImpl(parameters))
            }
        }
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object RoutesList : Config
    }
}
