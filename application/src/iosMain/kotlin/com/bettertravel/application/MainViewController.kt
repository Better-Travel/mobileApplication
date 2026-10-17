package com.bettertravel.application

import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.bettertravel.database.DatabaseDriverFactory
import com.bettertravel.database.IosDatabaseDriverFactory
import com.bettertravel.presentation.navigation.DefaultRootComponent
import com.bettertravel.ui.RootContent
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import platform.UIKit.UIViewController

@Suppress("ktlint:standard:function-naming")
fun MainViewController(): UIViewController {
    DependencyInitializer.initialize(
        DI.Module("ios") {
            bindSingleton<DatabaseDriverFactory> { IosDatabaseDriverFactory() }
        },
    )
    val lifecycle = LifecycleRegistry()
    val root = DefaultRootComponent(DefaultComponentContext(lifecycle))
    lifecycle.resume()
    return ComposeUIViewController {
        RootContent(root)
    }
}
