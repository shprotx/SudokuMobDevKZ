package ru.shprot.sudokumobdevkz.model.database.entity

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
)
