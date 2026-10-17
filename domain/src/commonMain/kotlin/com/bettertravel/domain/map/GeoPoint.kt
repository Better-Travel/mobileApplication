package com.bettertravel.domain.map

import kotlin.math.PI
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

data class GeoPoint(
    val latitude: Double,
    val longitude: Double,
) {
    fun distanceMetersTo(other: GeoPoint): Double {
        val latitudeDelta = (other.latitude - latitude) * PI / HALF_TURN_DEGREES
        val longitudeDelta = (other.longitude - longitude) * PI / HALF_TURN_DEGREES
        val a =
            sin(latitudeDelta / 2).pow(2) +
                cos(latitude * PI / HALF_TURN_DEGREES) * cos(other.latitude * PI / HALF_TURN_DEGREES) *
                sin(longitudeDelta / 2).pow(2)
        return 2 * EARTH_RADIUS_METERS * asin(sqrt(a))
    }

    private companion object {
        const val EARTH_RADIUS_METERS = 6_371_000.0
        const val HALF_TURN_DEGREES = 180.0
    }
}
