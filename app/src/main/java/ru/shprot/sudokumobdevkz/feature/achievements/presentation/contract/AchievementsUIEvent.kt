package ru.shprot.sudokumobdevkz.feature.achievements.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEvent

sealed interface AchievementsUIEvent : UIEvent {
    data object BackClicked : AchievementsUIEvent
    data object SettingsClicked : AchievementsUIEvent
}