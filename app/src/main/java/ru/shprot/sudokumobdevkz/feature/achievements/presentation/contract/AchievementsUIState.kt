package ru.shprot.sudokumobdevkz.feature.achievements.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.domain.achievement.AchievementState
import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIState

data class AchievementsUIState(
    val isLoading: Boolean = true,
    val totalUnlocked: Int = 0,
    val totalCount: Int = 0,
    val unlocked: List<AchievementState> = emptyList(),
    val locked: List<AchievementState> = emptyList(),
    val selected: AchievementState? = null,
) : UIState