package ru.shprot.sudokumobdevkz.core.base.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_themes_table")
data class CustomThemeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val isBuiltIn: Boolean,
    val colorsJson: String,
    val createdAt: Long,
)