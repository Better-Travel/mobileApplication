package com.bettertravel.application

import com.bettertravel.common.platform.PlatformConfig
import com.bettertravel.database.DatabaseDriverFactory
import com.bettertravel.database.SqlDelightPlaceBookmarkStore
import com.bettertravel.database.SqlDelightRoutesStore
import com.bettertravel.domain.chat.AIChatting
import com.bettertravel.domain.map.MapTileLoading
import com.bettertravel.domain.places.PlaceBookmarking
import com.bettertravel.domain.routes.RoutesRepository
import com.bettertravel.domain.routes.RoutesStoring
import com.bettertravel.domain.routes.WalkingRouting
import com.bettertravel.domain.speech.AudioPlaying
import com.bettertravel.domain.speech.SpeechSynthesizing
import com.bettertravel.localization.GeneratedStringRepository
import com.bettertravel.localization.StringRepository
import com.bettertravel.networking.NetworkingFactory
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

private const val MOCK_SERVER_PORT = 8080
private const val WALKING_ROUTING_URL = "https://valhalla1.openstreetmap.de"
private const val OSM_TILE_URL_TEMPLATE = "https://tile.openstreetmap.org/{z}/{x}/{y}.png"
private const val OSM_ATTRIBUTION = "© OpenStreetMap contributors"
private const val CARTO_TILE_URL_TEMPLATE = "https://basemaps.cartocdn.com/light_all/{z}/{x}/{y}@2x.png"
private const val CARTO_ATTRIBUTION = "© OpenStreetMap contributors © CARTO"

fun sharedModule(): DI.Module {
    return DI.Module("shared") {
        bindSingleton<RoutesRepository> {
            NetworkingFactory.createRoutesRepository("http://${PlatformConfig.mockServerHost}:$MOCK_SERVER_PORT")
        }
        bindSingleton<AIChatting> {
            NetworkingFactory.createAIChatting("http://${PlatformConfig.mockServerHost}:$MOCK_SERVER_PORT")
        }
        bindSingleton<WalkingRouting> { NetworkingFactory.createWalkingRouting(WALKING_ROUTING_URL) }
        bindSingleton<MapTileLoading> { createMapTileLoading() }
        bindSingleton<SpeechSynthesizing> {
            NetworkingFactory.createSpeechSynthesizing(BuildSecrets.GOOGLE_TTS_API_KEY, instance<AudioPlaying>())
        }
        bindSingleton<PlaceBookmarking> { SqlDelightPlaceBookmarkStore(instance<DatabaseDriverFactory>()) }
        bindSingleton<RoutesStoring> { SqlDelightRoutesStore(instance<DatabaseDriverFactory>()) }
        bindSingleton<StringRepository> { GeneratedStringRepository { PlatformConfig.languageCode } }
    }
}

private fun createMapTileLoading(): MapTileLoading {
    val key = BuildSecrets.CARTO_API_KEY
    if (key.isBlank()) {
        return NetworkingFactory.createMapTileLoading(OSM_TILE_URL_TEMPLATE, OSM_ATTRIBUTION)
    }
    return NetworkingFactory.createMapTileLoading("$CARTO_TILE_URL_TEMPLATE?key=$key", CARTO_ATTRIBUTION)
}
