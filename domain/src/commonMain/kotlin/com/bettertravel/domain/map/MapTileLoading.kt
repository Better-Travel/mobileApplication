package com.bettertravel.domain.map

import com.badoo.reaktive.single.Single

interface MapTileLoading {
    val attribution: String

    fun loadTile(tile: MapTile): Single<ByteArray>
}
