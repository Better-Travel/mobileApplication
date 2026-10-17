package com.bettertravel.presentation

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.bettertravel.common.Deps
import com.bettertravel.domain.chat.AIChatting
import com.bettertravel.domain.map.MapTileLoading
import com.bettertravel.domain.places.PlaceBookmarking
import com.bettertravel.domain.routes.WalkingRouting
import com.bettertravel.domain.speech.SpeechRecognizing
import com.bettertravel.domain.speech.SpeechSynthesizing
import com.bettertravel.domainmock.chat.AIChattingMock
import com.bettertravel.domainmock.map.MapTileLoadingMock
import com.bettertravel.domainmock.places.PlaceBookmarkingMock
import com.bettertravel.domainmock.routes.WalkingRoutingMock
import com.bettertravel.domainmock.speech.SpeechRecognizingMock
import com.bettertravel.domainmock.speech.SpeechSynthesizingMock
import com.bettertravel.localization.GeneratedStringRepository
import com.bettertravel.localization.StringRepository
import org.kodein.di.DI
import org.kodein.di.bindSingleton

fun installTestDeps(language: String = "en") {
    Deps.di =
        DI {
            bindSingleton<AIChatting> { AIChattingMock() }
            bindSingleton<WalkingRouting> { WalkingRoutingMock() }
            bindSingleton<PlaceBookmarking> { PlaceBookmarkingMock() }
            bindSingleton<SpeechRecognizing> { SpeechRecognizingMock() }
            bindSingleton<SpeechSynthesizing> { SpeechSynthesizingMock() }
            bindSingleton<MapTileLoading> { MapTileLoadingMock() }
            bindSingleton<StringRepository> { GeneratedStringRepository { language } }
        }
}

fun createTestComponentContext(): DefaultComponentContext {
    val lifecycle = LifecycleRegistry()
    lifecycle.resume()
    return DefaultComponentContext(lifecycle)
}
