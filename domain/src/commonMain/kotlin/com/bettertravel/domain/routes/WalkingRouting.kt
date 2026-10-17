package com.bettertravel.domain.routes

import com.badoo.reaktive.single.Single
import com.bettertravel.domain.map.GeoPoint

interface WalkingRouting {
    fun walkingPath(waypoints: List<GeoPoint>): Single<List<GeoPoint>>
}
