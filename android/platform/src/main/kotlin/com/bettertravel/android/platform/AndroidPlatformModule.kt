package com.bettertravel.android.platform

import android.content.Context
import com.bettertravel.database.AndroidDatabaseDriverFactory
import com.bettertravel.database.DatabaseDriverFactory
import org.kodein.di.DI
import org.kodein.di.bindSingleton

fun androidPlatformModule(context: Context): DI.Module {
    return DI.Module("android") {
        bindSingleton<DatabaseDriverFactory> { AndroidDatabaseDriverFactory(context) }
    }
}
