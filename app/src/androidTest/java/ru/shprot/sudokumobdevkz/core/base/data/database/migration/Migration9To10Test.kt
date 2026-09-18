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
class Migration9To10Test {

    private val dbName = "migration-test-9-10"

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        SudokuComposeDatabase::class.java,
        emptyList(),
        FrameworkSQLiteOpenHelperFactory(),
    )

    @Test
    fun migrate9To10_keepsStatistics_andBackfillsPlaytimeFromHistory() {
        helper.createDatabase(dbName, 9).use { db ->
            db.execSQL(
                "INSERT INTO statistic_table " +
                    "(difficulty, allTime, bestTime, averageTime, gamesStarted, gamesWon, percentOfWins, " +
                    "winsWithoutErrors, bestWinsLine, currentWinsLine, casualGamesPlayed) " +
                    "VALUES (1, 1200, 180, 240, 12, 5, 41, 3, 4, 2, 7)",
            )
            db.execSQL(
                "INSERT INTO game_history_table " +
                    "(difficulty, timeSeconds, errors, isWin, timestamp, hintsUsed, isDaily, isStandardMode) " +
                    "VALUES (1, 300, 0, 1, 1700000000000, 0, 0, 1)",
            )
            db.execSQL(
                "INSERT INTO game_history_table " +
                    "(difficulty, timeSeconds, errors, isWin, timestamp, hintsUsed, isDaily, isStandardMode) " +
                    "VALUES (1, 200, 1, 0, 1700000000000, 0, 0, 1)",
            )
            db.execSQL(
                "INSERT INTO game_history_table " +
                    "(difficulty, timeSeconds, errors, isWin, timestamp, hintsUsed, isDaily, isStandardMode) " +
                    "VALUES (2, 400, 0, 1, 1700000000000, 0, 0, 1)",
            )
        }

        helper.runMigrationsAndValidate(dbName, 10, true, MIGRATION_9_10).use { db ->
            db.query("SELECT gamesStarted, gamesWon, bestTime, casualGamesPlayed FROM statistic_table WHERE difficulty = 1")
                .use { cursor ->
                    assert(cursor.moveToFirst())
                    assert(cursor.getInt(0) == 12)
                    assert(cursor.getInt(1) == 5)
                    assert(cursor.getInt(2) == 180)
                    assert(cursor.getInt(3) == 7)
                }

            db.query("SELECT SUM(totalSeconds) FROM daily_playtime_table WHERE difficulty = 1").use { cursor ->
                assert(cursor.moveToFirst())
                assert(cursor.getInt(0) == 500)
            }

            db.query("SELECT SUM(totalSeconds) FROM daily_playtime_table WHERE difficulty = 2").use { cursor ->
                assert(cursor.moveToFirst())
                assert(cursor.getInt(0) == 400)
            }

            db.query("SELECT COUNT(*) FROM game_history_table").use { cursor ->
                assert(cursor.moveToFirst())
                assert(cursor.getInt(0) == 3)
            }
        }
    }
}