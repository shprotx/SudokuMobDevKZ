package ru.shprot.sudokumobdevkz.core.base.data.cloud.model

import kotlinx.serialization.Serializable

@Serializable
data class SavedGameDto(
    val difficulty: Int,
    val timeSeconds: Int,
    val errors: Int,
    val maxErrors: Int,
    val hintsRemaining: Int,
    val isNotesEnabled: Boolean,
    val cellsJson: String,
    val solutionJson: String,
    val isStandardMode: Boolean,
    val timestamp: Long,
    val isDailyChallenge: Boolean = false,
    val dailyDateKey: String = "",
)
