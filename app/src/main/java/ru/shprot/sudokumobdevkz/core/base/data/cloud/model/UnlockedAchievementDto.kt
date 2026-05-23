package ru.shprot.sudokumobdevkz.core.base.data.cloud.model

import kotlinx.serialization.Serializable

@Serializable
data class UnlockedAchievementDto(
    val id: String,
    val unlockedAt: Long,
)
