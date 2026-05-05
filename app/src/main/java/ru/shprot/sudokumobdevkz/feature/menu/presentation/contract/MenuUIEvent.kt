package ru.shprot.sudokumobdevkz.feature.menu.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEvent

sealed interface MenuUIEvent : UIEvent {
    data object ContinueGameClicked : MenuUIEvent
    data object NavigateToStatistic : MenuUIEvent
    data object NavigateToSettings : MenuUIEvent
    data object NavigateToHowToPlay : MenuUIEvent
    data object ScreenResumed : MenuUIEvent

    class NewGameClicked(val difficultyOrdinal: Int) : MenuUIEvent
    class DifficultySelected(val difficultyOrdinal: Int) : MenuUIEvent
}
