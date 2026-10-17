package com.bettertravel.networking.routes

import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.single.Single
import com.bettertravel.domain.routes.Route
import com.bettertravel.domain.routes.RoutesRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class HttpBasedRoutesRepository(
    private val client: HttpClient,
    private val baseUrl: String,
) : RoutesRepository {
    override fun loadRoutes(): Single<List<Route>> {
        return singleFromCoroutine {
            val dtos: List<RouteDto> = client.get("$baseUrl/routes").body()
            dtos.map { dto -> dto.toDomain() }
        }
    }
}
