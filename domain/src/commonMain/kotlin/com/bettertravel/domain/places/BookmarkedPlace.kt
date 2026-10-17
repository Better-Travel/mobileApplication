package com.bettertravel.domain.places

import com.bettertravel.domain.map.GeoPoint

data class BookmarkedPlace(
    val id: String,
    val name: String,
    val routeTitle: String,
    val location: GeoPoint,
    val savedAtMillis: Long,
) {
    companion object {
        fun idOf(
            name: String,
            location: GeoPoint,
        ): String {
            return "$name@${location.latitude},${location.longitude}"
        }
    }
}
