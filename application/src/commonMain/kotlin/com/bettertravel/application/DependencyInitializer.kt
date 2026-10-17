package com.bettertravel.application

import com.bettertravel.common.Deps
import org.kodein.di.DI

object DependencyInitializer {
    fun initialize(platformModule: DI.Module) {
        Deps.di =
            DI {
                import(platformModule)
                import(sharedModule())
            }
    }
}
