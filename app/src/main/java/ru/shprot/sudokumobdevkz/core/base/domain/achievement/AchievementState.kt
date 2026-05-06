package ru.shprot.sudokumobdevkz.core.base.domain.achievement

data class AchievementState(
    val achievement: Achievement,
    val progress: AchievementProgress,
    val unlockedAt: Long?,
)