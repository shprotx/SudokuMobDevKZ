package ru.shprot.sudokumobdevkz.core.base.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.GameHistoryDao
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.SavedGameDao
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.StatisticDao
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.GameHistoryEntity
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.SavedGameEntity
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.StatisticEntity

@Database(
    entities = [
        StatisticEntity::class,
        GameHistoryEntity::class,
        SavedGameEntity::class,
    ],
    version = 3,
    exportSchema = false,
)
abstract class SudokuComposeDatabase : RoomDatabase() {
    abstract fun statisticDao(): StatisticDao
    abstract fun gameHistoryDao(): GameHistoryDao
    abstract fun savedGameDao(): SavedGameDao
}
