package ru.shprot.sudokumobdevkz.feature.menu.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEffect

sealed interface MenuUIEffect : UIEffect {
    data object NavigateToContinueGame : MenuUIEffect
    data object NavigateToStatistic : MenuUIEffect
    data object NavigateToSettings : MenuUIEffect
    data object NavigateToHowToPlay : MenuUIEffect

    class NavigateToGame(val difficulty: Int) : MenuUIEffect
}
