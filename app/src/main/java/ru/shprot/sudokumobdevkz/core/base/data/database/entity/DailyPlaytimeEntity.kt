package ru.shprot.sudokumobdevkz.core.base.data.database.entity

import androidx.room.Entity

@Entity(tableName = "daily_playtime_table", primaryKeys = ["dateKey", "difficulty"])
data class DailyPlaytimeEntity(
    val dateKey: String,
    val difficulty: Int,
    val totalSeconds: Int,
)