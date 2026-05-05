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
class Migration_4_5_Test {

    private val dbName = "migration-test"

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        SudokuComposeDatabase::class.java,
        emptyList(),
        FrameworkSQLiteOpenHelperFactory(),
    )

    @Test
    fun migrate4To5_createsTable_andKeepsExistingData() {
        helper.createDatabase(dbName, 4).use { db ->
            db.execSQL(
                "INSERT INTO statistic_table (difficulty, allTime, bestTime, averageTime, gamesStarted, gamesWon, percentOfWins, winsWithoutErrors, bestWinsLine, currentWinsLine, casualGamesPlayed) " +
                    "VALUES (0, 100, 50, 60, 5, 3, 60, 1, 2, 1, 0)",
            )
        }

        helper.runMigrationsAndValidate(dbName, 5, true, MIGRATION_4_5).use { db ->
            db.query("SELECT * FROM achievement_unlocked_table").use { cursor ->
                assert(cursor.count == 0)
            }
            db.query("SELECT gamesWon FROM statistic_table WHERE difficulty = 0").use { cursor ->
                assert(cursor.moveToFirst())
                assert(cursor.getInt(0) == 3)
            }
        }
    }
}