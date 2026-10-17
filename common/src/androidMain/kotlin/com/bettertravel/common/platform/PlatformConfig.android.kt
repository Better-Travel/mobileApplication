package com.bettertravel.common.platform

import java.util.Locale

actual object PlatformConfig {
    actual val mockServerHost: String = "10.0.2.2"
    actual val languageCode: String
        get() {
            return Locale.getDefault().language
        }
}
