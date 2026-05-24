package ru.shprot.sudokumobdevkz.core.base.data.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_5_6: Migration = object : Migration(5, 6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE game_history_table ADD COLUMN hintsUsed INTEGER NOT NULL DEFAULT 0",
        )
        db.execSQL(
            "ALTER TABLE game_history_table ADD COLUMN isDaily INTEGER NOT NULL DEFAULT 0",
        )
    }
}
