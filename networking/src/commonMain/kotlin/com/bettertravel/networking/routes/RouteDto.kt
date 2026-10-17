package com.bettertravel.networking.routes

import com.bettertravel.domain.routes.Route
import kotlinx.serialization.Serializable

@Serializable
internal data class RouteDto(
    val id: String,
    val title: String,
    val city: String,
    val distanceKm: Double,
    val durationMinutes: Int,
    val stopsCount: Int,
) {
    fun toDomain(): Route {
        return Route(
            id = id,
            title = title,
            city = city,
            distanceKm = distanceKm,
            durationMinutes = durationMinutes,
            stopsCount = stopsCount,
        )
    }
}
