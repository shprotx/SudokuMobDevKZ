package ru.shprot.sudokumobdevkz.core.base.data.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_7_8: Migration = object : Migration(7, 8) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS custom_themes_table (
                id TEXT NOT NULL PRIMARY KEY,
                name TEXT NOT NULL,
                isBuiltIn INTEGER NOT NULL DEFAULT 0,
                colorsJson TEXT NOT NULL,
                createdAt INTEGER NOT NULL DEFAULT 0
            )
            """.trimIndent(),
        )
    }
}