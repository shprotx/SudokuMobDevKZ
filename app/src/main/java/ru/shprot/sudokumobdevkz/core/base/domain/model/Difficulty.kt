package ru.shprot.sudokumobdevkz.core.base.domain.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import ru.shprot.sudokumobdevkz.R

fun Difficulty.dotColor(): Color = when (this) {
    Difficulty.EASY -> Color(0xFF34C759)
    Difficulty.MEDIUM -> Color(0xFFFF9500)
    Difficulty.HARD -> Color(0xFFFF3B30)
    Difficulty.ULTRA -> Color(0xFF8B5CF6)
}

enum class Difficulty(
    val firebaseKey: Int,
    val visibleCells: IntRange,
    val emoji: String,
    val dotCount: Int,
    @StringRes val titleRes: Int,
    @StringRes val subtitleRes: Int,
) {
    EASY(
        firebaseKey = 1,
        visibleCells = 40..40,
        emoji = "🌿",
        dotCount = 1,
        titleRes = R.string.difficulty_easy,
        subtitleRes = R.string.for_beginners,
    ),
    MEDIUM(
        firebaseKey = 2,
        visibleCells = 30..30,
        emoji = "☀️",
        dotCount = 2,
        titleRes = R.string.difficulty_middle,
        subtitleRes = R.string.for_experienced,
    ),
    HARD(
        firebaseKey = 3,
        visibleCells = 21..24,
        emoji = "👑",
        dotCount = 3,
        titleRes = R.string.difficulty_expert,
        subtitleRes = R.string.for_experts,
    ),
    ULTRA(
        firebaseKey = 4,
        visibleCells = 17..20,
        emoji = "💎",
        dotCount = 4,
        titleRes = R.string.difficulty_ultra,
        subtitleRes = R.string.for_hardcore,
    );

    companion object {
        fun fromFirebaseKey(key: Int): Difficulty? = entries.find { it.firebaseKey == key }
        fun fromOrdinal(ordinal: Int): Difficulty = entries.getOrElse(ordinal) { EASY }
    }
}