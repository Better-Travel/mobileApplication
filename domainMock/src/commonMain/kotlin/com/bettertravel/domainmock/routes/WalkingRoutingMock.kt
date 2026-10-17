package com.bettertravel.domainmock.routes

import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.singleOf
import com.badoo.reaktive.single.singleOfError
import com.bettertravel.domain.map.GeoPoint
import com.bettertravel.domain.routes.WalkingRouting

class WalkingRoutingMock(
    var result: Result<List<GeoPoint>>? = null,
) : WalkingRouting {
    val receivedWaypoints: MutableList<List<GeoPoint>> = mutableListOf()

    override fun walkingPath(waypoints: List<GeoPoint>): Single<List<GeoPoint>> {
        receivedWaypoints.add(waypoints)
        val outcome = result ?: Result.success(waypoints)
        return outcome.fold(
            onSuccess = { path -> singleOf(path) },
            onFailure = { error -> singleOfError(error) },
        )
    }
}
