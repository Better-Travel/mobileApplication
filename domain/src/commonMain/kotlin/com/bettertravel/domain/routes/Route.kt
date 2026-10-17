package com.bettertravel.domain.routes

data class Route(
    val id: String,
    val title: String,
    val city: String,
    val distanceKm: Double,
    val durationMinutes: Int,
    val stopsCount: Int,
)
