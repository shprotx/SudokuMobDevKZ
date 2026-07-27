package ru.shprot.sudokumobdevkz.core.base.data.database.migration

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.shprot.sudokumobdevkz.core.base.data.database.SudokuComposeDatabase

@RunWith(AndroidJUnit4::class)
class Migration8To9Test {

    private val dbName = "migration-test-8-9"

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        SudokuComposeDatabase::class.java,
        emptyList(),
        FrameworkSQLiteOpenHelperFactory(),
    )

    @Test
    fun migrate8To9_addsDailyChallengeColumns_existingRowGetsDefaults() {
        helper.createDatabase(dbName, 8).use { db ->
            db.execSQL(
                "INSERT INTO saved_game_table " +
                    "(id, difficulty, timeSeconds, errors, maxErrors, hintsRemaining, isNotesEnabled, " +
                    "cellsJson, solutionJson, isStandardMode, timestamp) " +
                    "VALUES (0, 0, 60, 0, 3, 3, 0, '[]', '[]', 1, 1000000)",
            )
        }

        helper.runMigrationsAndValidate(dbName, 9, true, MIGRATION_8_9).use { db ->
            db.query("SELECT isDailyChallenge, dailyDateKey FROM saved_game_table WHERE id = 0").use { cursor ->
                assert(cursor.moveToFirst())
                assert(cursor.getInt(0) == 0)
                assert(cursor.getString(1) == "")
            }
        }
    }
}