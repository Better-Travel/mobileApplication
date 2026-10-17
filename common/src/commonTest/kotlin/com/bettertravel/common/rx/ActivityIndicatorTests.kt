package com.bettertravel.common.rx

import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.single.singleOf
import com.badoo.reaktive.single.subscribe
import kotlin.test.Test
import kotlin.test.assertEquals

class ActivityIndicatorTests {
    @Test
    fun testThat_singleCompletes_indicatorReturnsToInactive() {
        val indicator = ActivityIndicator()
        val values = mutableListOf<Boolean>()
        indicator.isActive.subscribe { values.add(it) }

        singleOf(1).trackActivity(indicator).subscribe { }

        assertEquals(listOf(false, true, false), values)
    }
}
