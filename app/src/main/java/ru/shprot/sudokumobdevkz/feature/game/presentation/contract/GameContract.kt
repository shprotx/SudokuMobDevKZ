package ru.shprot.sudokumobdevkz.feature.game.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEffect
import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEvent
import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIState

data class CellData(
    val value: Int = 0,
    val isGiven: Boolean = false,
    val isError: Boolean = false,
    val notes: Set<Int> = emptySet(),
)

data class GameUiState(
    val cells: Array<Array<CellData>> = Array(9) { Array(9) { CellData() } },
    val solution: Array<IntArray> = Array(9) { IntArray(9) },
    val selectedRow: Int = -1,
    val selectedCol: Int = -1,
    val difficulty: Int = 0,
    val errors: Int = 0,
    val maxErrors: Int = 3,
    val timer: String = "00:00",
    val timeSeconds: Int = 0,
    val isNotesEnabled: Boolean = false,
    val hintsRemaining: Int = 3,
    val isGenerating: Boolean = true,
    val isPaused: Boolean = false,
    val isGameOver: Boolean = false,
    val isWin: Boolean = false,
    val availableNumbers: Set<Int> = (1..9).toSet(),
    val highlightedNumber: Int = 0,
) : UIState {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GameUiState) return false
        return cells.contentDeepEquals(other.cells) &&
                solution.contentDeepEquals(other.solution) &&
                selectedRow == other.selectedRow &&
                selectedCol == other.selectedCol &&
                difficulty == other.difficulty &&
                errors == other.errors &&
                maxErrors == other.maxErrors &&
                timer == other.timer &&
                timeSeconds == other.timeSeconds &&
                isNotesEnabled == other.isNotesEnabled &&
                hintsRemaining == other.hintsRemaining &&
                isGenerating == other.isGenerating &&
                isPaused == other.isPaused &&
                isGameOver == other.isGameOver &&
                isWin == other.isWin &&
                availableNumbers == other.availableNumbers &&
                highlightedNumber == other.highlightedNumber
    }

    override fun hashCode(): Int = cells.contentDeepHashCode()
}

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

sealed interface GameEffect : UIEffect {
    data class NavigateToGameOver(
        val isWin: Boolean,
        val time: String,
        val errors: Int,
    ) : GameEffect
}
