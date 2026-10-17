package com.bettertravel.android.application

import android.app.Application
import com.bettertravel.android.platform.androidPlatformModule
import com.bettertravel.application.DependencyInitializer

class BetterTravelApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DependencyInitializer.initialize(androidPlatformModule(this))
    }
}
