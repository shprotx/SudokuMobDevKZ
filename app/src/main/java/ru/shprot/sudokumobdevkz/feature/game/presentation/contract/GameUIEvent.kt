package ru.shprot.sudokumobdevkz.feature.game.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEvent

sealed interface GameUIEvent : UIEvent {
    data object UndoClicked : GameUIEvent
    data object EraseClicked : GameUIEvent
    data object NotesToggled : GameUIEvent
    data object HintClicked : GameUIEvent
    data object DeselectClicked : GameUIEvent
    data object PauseClicked : GameUIEvent
    data object ResumeClicked : GameUIEvent
    data object BackClicked : GameUIEvent
    data object NewGameClicked : GameUIEvent
    data object ShowPauseDialog : GameUIEvent
    data object DismissPauseDialog : GameUIEvent
    data object ShowNewGameDialog : GameUIEvent
    data object DismissNewGameDialog : GameUIEvent
    data object ExitGame : GameUIEvent
    data object SaveState : GameUIEvent
    data object SettingsClicked : GameUIEvent
    data object PaletteClicked : GameUIEvent
    data object DismissThemePopup : GameUIEvent

    data object DismissDraftPopup : GameUIEvent

    class CellClicked(val row: Int, val col: Int) : GameUIEvent
    class NumberClicked(val number: Int) : GameUIEvent
    class StartNewGame(val difficultyOrdinal: Int) : GameUIEvent
    class ThemeSelected(val themeId: String) : GameUIEvent
    class CellLongPressed(val row: Int, val col: Int) : GameUIEvent
    class DraftNoteToggled(val number: Int) : GameUIEvent
}
