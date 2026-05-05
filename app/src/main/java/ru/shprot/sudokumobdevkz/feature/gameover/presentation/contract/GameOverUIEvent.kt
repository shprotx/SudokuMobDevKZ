package ru.shprot.sudokumobdevkz.feature.gameover.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEvent

sealed interface GameOverUIEvent : UIEvent {
    data object PlayAgainClicked : GameOverUIEvent
    data object BackToMenuClicked : GameOverUIEvent
}
