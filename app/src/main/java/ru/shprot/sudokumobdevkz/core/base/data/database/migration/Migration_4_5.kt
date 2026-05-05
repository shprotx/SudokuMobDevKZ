package ru.shprot.sudokumobdevkz.core.base.data.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_4_5: Migration = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS achievement_unlocked_table (
                id TEXT NOT NULL PRIMARY KEY,
                unlockedAt INTEGER NOT NULL
            )
            """.trimIndent(),
        )
    }
}