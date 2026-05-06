package ru.shprot.sudokumobdevkz.core.base.domain.achievement

data class UnlockedAchievement(
    val achievement: Achievement,
    val unlockedAt: Long,
)