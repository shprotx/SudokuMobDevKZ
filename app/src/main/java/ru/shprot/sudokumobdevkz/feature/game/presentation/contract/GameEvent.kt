package ru.shprot.sudokumobdevkz.feature.game.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEvent

sealed interface GameEvent : UIEvent {
    data class CellClicked(val row: Int, val col: Int) : GameEvent
    data class NumberClicked(val number: Int) : GameEvent
    data object UndoClicked : GameEvent
    data object EraseClicked : GameEvent
    data object NotesToggled : GameEvent
    data object HintClicked : GameEvent
    data object DeselectClicked : GameEvent
    data object PauseClicked : GameEvent
    data object ResumeClicked : GameEvent
}
