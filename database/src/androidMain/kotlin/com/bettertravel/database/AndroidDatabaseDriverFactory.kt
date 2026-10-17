package com.bettertravel.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

class AndroidDatabaseDriverFactory(
    private val context: Context,
) : DatabaseDriverFactory {
    override fun create(): SqlDriver {
        return AndroidSqliteDriver(BetterTravelDatabase.Schema, context, "bettertravel.db")
    }
}
