package ru.shprot.sudokumobdevkz.core.base.data.cloud.model

import kotlinx.serialization.Serializable

@Serializable
data class CustomThemeDto(
    val id: String,
    val name: String,
    val colorsJson: String,
    val createdAt: Long,
)