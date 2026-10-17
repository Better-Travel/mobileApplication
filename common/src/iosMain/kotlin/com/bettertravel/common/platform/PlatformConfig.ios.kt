package com.bettertravel.common.platform

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

actual object PlatformConfig {
    actual val mockServerHost: String = "localhost"
    actual val languageCode: String
        get() {
            return NSLocale.currentLocale.languageCode
        }
}
