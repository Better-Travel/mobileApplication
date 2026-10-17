package com.bettertravel.networking.routes

import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.single.Single
import com.bettertravel.domain.map.GeoPoint
import com.bettertravel.domain.routes.WalkingRouting
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

private const val STATUS_OK = 0

class HttpBasedWalkingRouting(
    private val client: HttpClient,
    private val baseUrl: String,
) : WalkingRouting {
    override fun walkingPath(waypoints: List<GeoPoint>): Single<List<GeoPoint>> {
        return singleFromCoroutine {
            val request =
                buildJsonObject {
                    put(
                        "locations",
                        buildJsonArray {
                            waypoints.forEach { point ->
                                add(
                                    buildJsonObject {
                                        put("lat", point.latitude)
                                        put("lon", point.longitude)
                                    },
                                )
                            }
                        },
                    )
                    put("costing", "pedestrian")
                    put("directions_type", "none")
                }
            val response: ValhallaResponseDto =
                client
                    .get("$baseUrl/route") {
                        parameter("json", request.toString())
                    }.body()
            if (response.trip.status != STATUS_OK || response.trip.legs.isEmpty()) {
                throw IllegalStateException("Routing failed with status ${response.trip.status}")
            }
            response.trip.legs
                .flatMapIndexed { index, leg ->
                    val points = PolylineDecoder.decode(leg.shape)
                    if (index == 0) points else points.drop(1)
                }
        }
    }
}
