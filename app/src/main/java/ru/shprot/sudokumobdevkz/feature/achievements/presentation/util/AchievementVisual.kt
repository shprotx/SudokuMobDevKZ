package ru.shprot.sudokumobdevkz.feature.achievements.presentation.util

import androidx.compose.ui.graphics.Color

enum class AchievementDecoration {
    RAYS,
    SPARKLES,
    STARS,
    FLAMES,
    HALO,
    BOLTS,
    NONE,
}

data class AchievementVisual(
    val emoji: String,
    val gradientStart: Color,
    val gradientEnd: Color,
    val accentColor: Color,
    val ringColor: Color,
    val decoration: AchievementDecoration,
)