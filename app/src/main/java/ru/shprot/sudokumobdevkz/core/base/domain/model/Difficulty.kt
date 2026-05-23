package ru.shprot.sudokumobdevkz.core.base.domain.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import ru.shprot.sudokumobdevkz.R

fun Difficulty.dotColor(): Color = when (this) {
    Difficulty.EASY -> Color(0xFF34C759)
    Difficulty.MEDIUM -> Color(0xFFFF9500)
    Difficulty.HARD -> Color(0xFFFF3B30)
}

enum class Difficulty(
    val firebaseKey: Int,
    val visibleCells: Int,
    val emoji: String,
    val dotCount: Int,
    @StringRes val titleRes: Int,
    @StringRes val subtitleRes: Int,
    val leaderboardId: String,
) {
    EASY(
        firebaseKey = 1,
        visibleCells = 40,
        emoji = "\uD83C\uDF3F",
        dotCount = 1,
        titleRes = R.string.difficulty_easy,
        subtitleRes = R.string.for_beginners,
        leaderboardId = "CgkIqffM1tUYEAIQZQ",
    ),
    MEDIUM(
        firebaseKey = 2,
        visibleCells = 30,
        emoji = "☀\uFE0F",
        dotCount = 2,
        titleRes = R.string.difficulty_middle,
        subtitleRes = R.string.for_experienced,
        leaderboardId = "CgkIqffM1tUYEAIQZg",
    ),
    HARD(
        firebaseKey = 3,
        visibleCells = 27,
        emoji = "\uD83D\uDC51",
        dotCount = 3,
        titleRes = R.string.difficulty_expert,
        subtitleRes = R.string.for_experts,
        leaderboardId = "CgkIqffM1tUYEAIQZw",
    );

    companion object {
        fun fromFirebaseKey(key: Int): Difficulty? = entries.find { it.firebaseKey == key }
        fun fromOrdinal(ordinal: Int): Difficulty = entries.getOrElse(ordinal) { EASY }
    }
}
