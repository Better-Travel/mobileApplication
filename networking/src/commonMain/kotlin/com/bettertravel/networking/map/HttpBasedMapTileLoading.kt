package com.bettertravel.networking.map

import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.single.Single
import com.bettertravel.domain.map.MapTile
import com.bettertravel.domain.map.MapTileLoading
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class HttpBasedMapTileLoading(
    private val client: HttpClient,
    private val urlTemplate: String,
    override val attribution: String,
) : MapTileLoading {
    override fun loadTile(tile: MapTile): Single<ByteArray> {
        return singleFromCoroutine {
            client.get(tileUrl(tile)).body<ByteArray>()
        }
    }

    internal fun tileUrl(tile: MapTile): String {
        return urlTemplate
            .replace("{z}", tile.zoom.toString())
            .replace("{x}", tile.x.toString())
            .replace("{y}", tile.y.toString())
    }
}
