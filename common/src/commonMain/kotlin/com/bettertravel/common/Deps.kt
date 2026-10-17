package com.bettertravel.common

import org.kodein.di.DI
import org.kodein.di.DirectDI
import org.kodein.di.direct
import org.kodein.di.instance

object Deps {
    var di: DI = DI {}

    val direct: DirectDI
        get() {
            return di.direct
        }

    inline fun <reified T : Any> instance(): T {
        return di.direct.instance()
    }
}
