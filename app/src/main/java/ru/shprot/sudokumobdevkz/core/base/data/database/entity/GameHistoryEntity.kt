package ru.shprot.sudokumobdevkz.core.base.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_history_table")
data class GameHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val difficulty: Int,
    val timeSeconds: Int,
    val errors: Int,
    val isWin: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    @ColumnInfo(defaultValue = "0")
    val hintsUsed: Int = 0,
    @ColumnInfo(defaultValue = "0")
    val isDaily: Boolean = false,
    @ColumnInfo(defaultValue = "1")
    val isStandardMode: Boolean = true,
)
