package ru.shprot.sudokumobdevkz.core.base.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievement_unlocked_table")
data class AchievementUnlockedEntity(
    @PrimaryKey val id: String,
    val unlockedAt: Long,
)