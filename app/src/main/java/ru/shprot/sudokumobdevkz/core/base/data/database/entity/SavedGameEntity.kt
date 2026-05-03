package ru.shprot.sudokumobdevkz.core.base.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_game_table")
data class SavedGameEntity(
    @PrimaryKey val id: Int = 0,
    val difficulty: Int = 0,
    val timeSeconds: Int = 0,
    val errors: Int = 0,
    val maxErrors: Int = 3,
    val hintsRemaining: Int = 3,
    val isNotesEnabled: Boolean = false,
    val cellsJson: String = "",
    val solutionJson: String = "",
    val isStandardMode: Boolean = true,
    val timestamp: Long = System.currentTimeMillis(),
)
