package com.bettertravel.common.time

import java.text.DateFormat
import java.util.Date

actual fun formatClockTime(epochMillis: Long): String {
    return DateFormat.getTimeInstance(DateFormat.SHORT).format(Date(epochMillis))
}

actual fun formatShortDate(epochMillis: Long): String {
    return DateFormat.getDateInstance(DateFormat.MEDIUM).format(Date(epochMillis))
}
