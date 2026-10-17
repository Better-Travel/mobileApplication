package com.bettertravel.presentation.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import com.bettertravel.domain.map.GeoPoint
import com.bettertravel.presentation.ViewModelContext
import com.bettertravel.presentation.details.StopDetailsViewModel
import com.bettertravel.presentation.details.StopDetailsViewModelImpl
import com.bettertravel.presentation.route.StopDetailsRequest
import kotlinx.serialization.Serializable

class DefaultRootComponent(
    componentContext: ComponentContext,
) : RootComponent,
    ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()
    private var main: DefaultMainComponent? = null

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
                // TODO: start the walk once navigation mode is built
                val component =
                    DefaultMainComponent(
                        componentContext = context,
                        onStartRoute = { },
                        onShowStopDetails = { request -> navigation.pushNew(Config.StopDetails.from(request)) },
                    )
                main = component
                RootComponent.Child.Main(component)
            }

            is Config.StopDetails -> {
                val parameters =
                    StopDetailsViewModel.Parameters(
                        context = ViewModelContext(context),
                        request = config.toRequest(),
                        onBack = { navigation.pop() },
                        onSwap = {
                            navigation.pop()
                            main?.selectTab(MainTab.Discover)
                        },
                        onRemove = { index ->
                            main?.removeStop(index)
                            navigation.pop()
                        },
                    )
                RootComponent.Child.StopDetails(StopDetailsViewModelImpl(parameters))
            }
        }
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Main : Config

        @Serializable
        data class StopDetails(
            val order: Int,
            val total: Int,
            val name: String,
            val routeTitle: String,
            val latitude: Double,
            val longitude: Double,
            val isFromMemories: Boolean,
        ) : Config {
            fun toRequest(): StopDetailsRequest {
                return StopDetailsRequest(
                    order = order,
                    total = total,
                    name = name,
                    routeTitle = routeTitle,
                    location = GeoPoint(latitude = latitude, longitude = longitude),
                    isFromMemories = isFromMemories,
                )
            }

            companion object {
                fun from(request: StopDetailsRequest): StopDetails {
                    return StopDetails(
                        order = request.order,
                        total = request.total,
                        name = request.name,
                        routeTitle = request.routeTitle,
                        latitude = request.location.latitude,
                        longitude = request.location.longitude,
                        isFromMemories = request.isFromMemories,
                    )
                }
            }
        }
    }
}
