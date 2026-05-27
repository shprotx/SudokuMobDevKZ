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
class Migration6To7Test {

    private val dbName = "migration-test-6-7"

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        SudokuComposeDatabase::class.java,
        emptyList(),
        FrameworkSQLiteOpenHelperFactory(),
    )

    @Test
    fun migrate6To7_addsIsStandardModeColumn_existingRowsGetDefault1() {
        helper.createDatabase(dbName, 6).use { db ->
            db.execSQL(
                "INSERT INTO game_history_table (difficulty, timeSeconds, errors, isWin, timestamp, hintsUsed, isDaily) " +
                    "VALUES (2, 300, 0, 1, 1000000, 0, 0)",
            )
        }

        helper.runMigrationsAndValidate(dbName, 7, true, MIGRATION_6_7).use { db ->
            db.query("SELECT isStandardMode FROM game_history_table").use { cursor ->
                assert(cursor.moveToFirst())
                assert(cursor.getInt(0) == 1)
            }
        }
    }
}
