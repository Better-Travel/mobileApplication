package com.bettertravel.networking.map

import com.bettertravel.domain.map.MapTile
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import kotlin.test.Test
import kotlin.test.assertEquals

class HttpBasedMapTileLoadingTests {
    @Test
    fun testThat_tileIsRequested_templatePlaceholdersAreFilled() {
        val loading =
            HttpBasedMapTileLoading(HttpClient(MockEngine { respond("") }), "http://tiles.test/{z}/{x}/{y}.png", "")

        assertEquals("http://tiles.test/15/17830/10470.png", loading.tileUrl(MapTile(zoom = 15, x = 17830, y = 10470)))
    }
}
