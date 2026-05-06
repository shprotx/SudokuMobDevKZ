package ru.shprot.sudokumobdevkz.core.base.domain.achievement

data class AchievementProgress(
    val current: Int,
    val target: Int,
) {
    val isUnlocked: Boolean get() = current >= target

    val ratio: Float
        get() = if (target <= 0) 0f else (current.toFloat() / target).coerceIn(0f, 1f)
}