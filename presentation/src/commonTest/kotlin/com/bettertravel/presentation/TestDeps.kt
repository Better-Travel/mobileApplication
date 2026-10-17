package com.bettertravel.presentation

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.bettertravel.common.Deps
import com.bettertravel.domain.chat.AIChatting
import com.bettertravel.domainmock.chat.AIChattingMock
import com.bettertravel.localization.GeneratedStringRepository
import com.bettertravel.localization.StringRepository
import org.kodein.di.DI
import org.kodein.di.bindSingleton

fun installTestDeps(language: String = "en") {
    Deps.di =
        DI {
            bindSingleton<AIChatting> { AIChattingMock() }
            bindSingleton<StringRepository> { GeneratedStringRepository { language } }
        }
}

fun createTestComponentContext(): DefaultComponentContext {
    val lifecycle = LifecycleRegistry()
    lifecycle.resume()
    return DefaultComponentContext(lifecycle)
}
