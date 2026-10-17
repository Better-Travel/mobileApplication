package com.bettertravel.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

class IosDatabaseDriverFactory : DatabaseDriverFactory {
    override fun create(): SqlDriver {
        return NativeSqliteDriver(BetterTravelDatabase.Schema, "bettertravel.db")
    }
}
