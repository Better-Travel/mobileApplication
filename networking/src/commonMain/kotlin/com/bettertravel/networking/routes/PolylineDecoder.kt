package com.bettertravel.networking.routes

import com.bettertravel.domain.map.GeoPoint

internal object PolylineDecoder {
    private const val PRECISION = 1e6
    private const val CHUNK_MASK = 0x1f
    private const val CONTINUATION_BIT = 0x20
    private const val CHUNK_BITS = 5
    private const val ASCII_OFFSET = 63

    fun decode(encoded: String): List<GeoPoint> {
        val points = mutableListOf<GeoPoint>()
        var index = 0
        var latitude = 0
        var longitude = 0
        while (index < encoded.length) {
            val latitudeDelta = nextValue(encoded, index)
            index = latitudeDelta.second
            val longitudeDelta = nextValue(encoded, index)
            index = longitudeDelta.second
            latitude += latitudeDelta.first
            longitude += longitudeDelta.first
            points.add(GeoPoint(latitude = latitude / PRECISION, longitude = longitude / PRECISION))
        }
        return points
    }

    private fun nextValue(
        encoded: String,
        start: Int,
    ): Pair<Int, Int> {
        var index = start
        var result = 0
        var shift = 0
        var chunk: Int
        do {
            chunk = encoded[index].code - ASCII_OFFSET
            index += 1
            result = result or ((chunk and CHUNK_MASK) shl shift)
            shift += CHUNK_BITS
        } while (chunk >= CONTINUATION_BIT)
        val value = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        return Pair(value, index)
    }
}
