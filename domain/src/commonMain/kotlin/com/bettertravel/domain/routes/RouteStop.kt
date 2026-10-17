package com.bettertravel.domain.routes

import com.bettertravel.domain.map.GeoPoint

const val MIN_ROUTE_STOPS = 2

data class RouteStop(
    val name: String,
    val location: GeoPoint,
)
