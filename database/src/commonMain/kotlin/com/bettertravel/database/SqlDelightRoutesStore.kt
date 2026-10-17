package com.bettertravel.database

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.completable.completableFromFunction
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.singleFromFunction
import com.bettertravel.domain.routes.Route
import com.bettertravel.domain.routes.RoutesStoring

class SqlDelightRoutesStore(
    driverFactory: DatabaseDriverFactory,
) : RoutesStoring {
    private val queries = BetterTravelDatabase(driverFactory.create()).storedRouteQueries

    override fun store(routes: List<Route>): Completable {
        return completableFromFunction {
            queries.transaction {
                queries.deleteAll()
                routes.forEach { route ->
                    queries.insertOrReplace(
                        id = route.id,
                        title = route.title,
                        city = route.city,
                        distanceKm = route.distanceKm,
                        durationMinutes = route.durationMinutes.toLong(),
                        stopsCount = route.stopsCount.toLong(),
                    )
                }
            }
        }
    }

    override fun storedRoutes(): Single<List<Route>> {
        return singleFromFunction {
            queries.selectAll().executeAsList().map { row ->
                Route(
                    id = row.id,
                    title = row.title,
                    city = row.city,
                    distanceKm = row.distanceKm,
                    durationMinutes = row.durationMinutes.toInt(),
                    stopsCount = row.stopsCount.toInt(),
                )
            }
        }
    }
}
