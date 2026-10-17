package com.bettertravel.common.time

import kotlin.time.Clock

fun currentTimeMillis(): Long {
    return Clock.System.now().toEpochMilliseconds()
}

expect fun formatClockTime(epochMillis: Long): String
