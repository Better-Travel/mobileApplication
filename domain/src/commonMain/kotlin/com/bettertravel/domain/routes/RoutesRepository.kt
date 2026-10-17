package com.bettertravel.domain.routes

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.single.Single

interface RoutesRepository {
    fun loadRoutes(): Single<List<Route>>
}

interface RoutesStoring {
    fun store(routes: List<Route>): Completable

    fun storedRoutes(): Single<List<Route>>
}
