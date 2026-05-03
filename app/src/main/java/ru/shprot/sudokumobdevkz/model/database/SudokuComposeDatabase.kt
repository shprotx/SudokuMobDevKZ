package ru.shprot.sudokumobdevkz.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.shprot.sudokumobdevkz.model.database.dao.GameHistoryDao
import ru.shprot.sudokumobdevkz.model.database.dao.StatisticDao
import ru.shprot.sudokumobdevkz.model.database.entity.GameHistoryEntity
import ru.shprot.sudokumobdevkz.model.database.entity.StatisticEntity

@Database(
    entities = [
        StatisticEntity::class,
        GameHistoryEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class SudokuComposeDatabase : RoomDatabase() {
    abstract fun statisticDao(): StatisticDao
    abstract fun gameHistoryDao(): GameHistoryDao
}
