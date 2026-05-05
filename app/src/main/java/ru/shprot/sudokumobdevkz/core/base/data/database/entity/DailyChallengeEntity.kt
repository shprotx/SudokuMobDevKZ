package ru.shprot.sudokumobdevkz.core.base.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_challenge_table")
data class DailyChallengeEntity(
    @PrimaryKey val dateKey: String,
    val difficultyOrdinal: Int,
    val isCompleted: Boolean,
    val completionTimeSeconds: Int,
    val errors: Int,
    val completedAt: Long,
)