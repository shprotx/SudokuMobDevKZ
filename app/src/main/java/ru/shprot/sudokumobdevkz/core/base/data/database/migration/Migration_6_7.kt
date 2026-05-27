package ru.shprot.sudokumobdevkz.core.base.data.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_6_7: Migration = object : Migration(6, 7) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE game_history_table ADD COLUMN isStandardMode INTEGER NOT NULL DEFAULT 1",
        )
    }
}