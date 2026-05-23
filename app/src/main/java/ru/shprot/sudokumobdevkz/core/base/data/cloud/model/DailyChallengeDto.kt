package ru.shprot.sudokumobdevkz.core.base.data.cloud.model

import kotlinx.serialization.Serializable

@Serializable
data class DailyChallengeDto(
    val dateKey: String,
    val difficultyOrdinal: Int,
    val isCompleted: Boolean,
    val completionTimeSeconds: Int,
    val errors: Int,
    val completedAt: Long,
)
