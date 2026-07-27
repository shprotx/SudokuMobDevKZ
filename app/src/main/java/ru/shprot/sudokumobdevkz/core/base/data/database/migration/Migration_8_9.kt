package ru.shprot.sudokumobdevkz.core.base.data.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_8_9: Migration = object : Migration(8, 9) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE saved_game_table ADD COLUMN isDailyChallenge INTEGER NOT NULL DEFAULT 0",
        )
        db.execSQL(
            "ALTER TABLE saved_game_table ADD COLUMN dailyDateKey TEXT NOT NULL DEFAULT ''",
        )
    }
}