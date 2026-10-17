package com.bettertravel.presentation.route

import com.bettertravel.domain.map.GeoPoint

data class StopDetailsRequest(
    val order: Int,
    val total: Int,
    val name: String,
    val routeTitle: String,
    val location: GeoPoint,
    val isFromMemories: Boolean = false,
)
