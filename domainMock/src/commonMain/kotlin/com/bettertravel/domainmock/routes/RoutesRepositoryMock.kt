package com.bettertravel.domainmock.routes

import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.singleOf
import com.badoo.reaktive.single.singleOfError
import com.bettertravel.domain.routes.Route
import com.bettertravel.domain.routes.RoutesRepository

class RoutesRepositoryMock(
    var result: Result<List<Route>> = Result.success(emptyList()),
) : RoutesRepository {
    var loadCount: Int = 0
        private set

    override fun loadRoutes(): Single<List<Route>> {
        loadCount += 1
        return result.fold(
            onSuccess = { routes -> singleOf(routes) },
            onFailure = { error -> singleOfError(error) },
        )
    }
}
