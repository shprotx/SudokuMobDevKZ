package ru.shprot.sudokumobdevkz.core.base.data.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_9_10: Migration = object : Migration(9, 10) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `daily_playtime_table` (" +
                "`dateKey` TEXT NOT NULL, " +
                "`difficulty` INTEGER NOT NULL, " +
                "`totalSeconds` INTEGER NOT NULL, " +
                "PRIMARY KEY(`dateKey`, `difficulty`))",
        )
        db.execSQL(
            "INSERT OR REPLACE INTO `daily_playtime_table` (dateKey, difficulty, totalSeconds) " +
                "SELECT date(timestamp / 1000, 'unixepoch', 'localtime'), difficulty, SUM(timeSeconds) " +
                "FROM game_history_table GROUP BY 1, 2",
        )
    }
}