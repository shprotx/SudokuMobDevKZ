package ru.shprot.sudokumobdevkz.feature.menu.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEffect

sealed interface MenuUIEffect : UIEffect {
    data object NavigateToContinueGame : MenuUIEffect
    data object NavigateToStatistic : MenuUIEffect
    data object NavigateToAchievements : MenuUIEffect
    data object NavigateToSettings : MenuUIEffect
    data object NavigateToHowToPlay : MenuUIEffect
    data object NavigateToDailyChallenge : MenuUIEffect

    class NavigateToGame(val difficultyOrdinal: Int) : MenuUIEffect
}