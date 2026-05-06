package ru.shprot.sudokumobdevkz.core.base.data.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS daily_challenge_table (
                dateKey TEXT NOT NULL PRIMARY KEY,
                difficultyOrdinal INTEGER NOT NULL,
                isCompleted INTEGER NOT NULL,
                completionTimeSeconds INTEGER NOT NULL,
                errors INTEGER NOT NULL,
                completedAt INTEGER NOT NULL
            )
            """.trimIndent()
        )
    }
}