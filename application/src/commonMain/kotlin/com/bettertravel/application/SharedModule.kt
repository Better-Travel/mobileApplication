package com.bettertravel.application

import com.bettertravel.common.platform.PlatformConfig
import com.bettertravel.database.DatabaseDriverFactory
import com.bettertravel.database.SqlDelightRoutesStore
import com.bettertravel.domain.routes.RoutesRepository
import com.bettertravel.domain.routes.RoutesStoring
import com.bettertravel.localization.GeneratedStringRepository
import com.bettertravel.localization.StringRepository
import com.bettertravel.networking.NetworkingFactory
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

private const val MOCK_SERVER_PORT = 8080

fun sharedModule(): DI.Module {
    return DI.Module("shared") {
        bindSingleton<RoutesRepository> {
            NetworkingFactory.createRoutesRepository("http://${PlatformConfig.mockServerHost}:$MOCK_SERVER_PORT")
        }
        bindSingleton<RoutesStoring> { SqlDelightRoutesStore(instance<DatabaseDriverFactory>()) }
        bindSingleton<StringRepository> { GeneratedStringRepository { PlatformConfig.languageCode } }
    }
}
