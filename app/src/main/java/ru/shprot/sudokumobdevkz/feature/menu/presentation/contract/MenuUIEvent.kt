package ru.shprot.sudokumobdevkz.feature.menu.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEvent

sealed interface MenuUIEvent : UIEvent {
    data class NewGameClicked(val difficulty: Int) : MenuUIEvent
    data object ContinueGameClicked : MenuUIEvent
    data class DifficultySelected(val difficulty: Int) : MenuUIEvent
    data object NavigateToStatistic : MenuUIEvent
    data object NavigateToSettings : MenuUIEvent
    data object NavigateToHowToPlay : MenuUIEvent
}
