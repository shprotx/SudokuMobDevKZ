package ru.shprot.sudokumobdevkz.feature.achievements.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEffect

sealed interface AchievementsUIEffect : UIEffect {
    data object NavigateBack : AchievementsUIEffect
    data object NavigateToSettings : AchievementsUIEffect
}