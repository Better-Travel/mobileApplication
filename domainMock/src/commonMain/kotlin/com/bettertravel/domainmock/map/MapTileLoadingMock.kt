package com.bettertravel.domainmock.map

import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.singleOf
import com.bettertravel.domain.map.MapTile
import com.bettertravel.domain.map.MapTileLoading

class MapTileLoadingMock : MapTileLoading {
    override val attribution: String = "Mock attribution"
    val requestedTiles: MutableList<MapTile> = mutableListOf()

    override fun loadTile(tile: MapTile): Single<ByteArray> {
        requestedTiles.add(tile)
        return singleOf(ByteArray(0))
    }
}
