package ru.shprot.sudokumobdevkz.feature.game.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.shprot.sudokumobdevkz.core.base.presentation.viewmodel.BaseViewModel
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.CellData
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameEffect
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameEvent
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameUiState
import ru.shprot.sudokumobdevkz.model.generator.SudokuGenerator
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<GameEvent, GameUiState, GameEffect>(GameUiState()) {

    private val difficulty: Int = savedStateHandle.get<Int>("difficulty") ?: 0
    private val undoStack = mutableListOf<UndoEntry>()
    private var timerJob: Job? = null

    init {
        startNewGame()
    }

    override fun handleUIEvent(event: GameEvent) {
        when (event) {
            is GameEvent.CellClicked -> onCellClicked(event.row, event.col)
            is GameEvent.NumberClicked -> onNumberClicked(event.number)
            is GameEvent.EraseClicked -> onErase()
            is GameEvent.UndoClicked -> onUndo()
            is GameEvent.NotesToggled -> onNotesToggled()
            is GameEvent.HintClicked -> onHint()
            is GameEvent.PauseClicked -> onPause()
            is GameEvent.ResumeClicked -> onResume()
        }
    }

    private fun startNewGame() {
        viewModelScope.launch(exceptionHandler) {
            setState(currentState.copy(isGenerating = true, difficulty = difficulty))

            val puzzle = SudokuGenerator.generate(difficulty)

            val cells = Array(9) { row ->
                Array(9) { col ->
                    val value = puzzle.puzzle[row][col]
                    CellData(
                        value = value,
                        isGiven = value != 0,
                    )
                }
            }

            setState(
                currentState.copy(
                    cells = cells,
                    solution = puzzle.solution,
                    isGenerating = false,
                    availableNumbers = calcAvailableNumbers(cells),
                )
            )

            startTimer()
        }
    }

    private fun onCellClicked(row: Int, col: Int) {
        if (currentState.isGenerating || currentState.isGameOver) return
        setState(currentState.copy(selectedRow = row, selectedCol = col))
    }

    private fun onNumberClicked(number: Int) {
        val state = currentState
        val row = state.selectedRow
        val col = state.selectedCol
        if (row < 0 || col < 0 || state.isGenerating || state.isGameOver) return

        val cell = state.cells[row][col]
        if (cell.isGiven) return

        if (state.isNotesEnabled) {
            val newNotes = cell.notes.toMutableSet()
            if (number in newNotes) newNotes.remove(number) else newNotes.add(number)

            undoStack.add(UndoEntry(row, col, cell))
            val newCells = state.cells.map { it.copyOf() }.toTypedArray()
            newCells[row][col] = cell.copy(notes = newNotes)

            setState(state.copy(cells = newCells))
            return
        }

        val correctValue = state.solution[row][col]
        val isCorrect = number == correctValue

        undoStack.add(UndoEntry(row, col, cell))
        val newCells = state.cells.map { it.copyOf() }.toTypedArray()

        if (isCorrect) {
            newCells[row][col] = CellData(value = number, isGiven = false, isError = false)
            clearNotesForNumber(newCells, row, col, number)
        } else {
            newCells[row][col] = CellData(value = number, isGiven = false, isError = true)
        }

        val newErrors = if (isCorrect) state.errors else state.errors + 1
        val available = calcAvailableNumbers(newCells)

        setState(
            state.copy(
                cells = newCells,
                errors = newErrors,
                availableNumbers = available,
            )
        )

        if (newErrors >= state.maxErrors) {
            gameOver(isWin = false)
        } else if (isBoardComplete(newCells)) {
            gameOver(isWin = true)
        }
    }

    private fun onErase() {
        val state = currentState
        val row = state.selectedRow
        val col = state.selectedCol
        if (row < 0 || col < 0) return

        val cell = state.cells[row][col]
        if (cell.isGiven) return

        undoStack.add(UndoEntry(row, col, cell))
        val newCells = state.cells.map { it.copyOf() }.toTypedArray()
        newCells[row][col] = CellData()

        setState(
            state.copy(
                cells = newCells,
                availableNumbers = calcAvailableNumbers(newCells),
            )
        )
    }

    private fun onUndo() {
        if (undoStack.isEmpty()) return
        val entry = undoStack.removeAt(undoStack.size - 1)
        val newCells = currentState.cells.map { it.copyOf() }.toTypedArray()
        newCells[entry.row][entry.col] = entry.previousCell

        setState(
            currentState.copy(
                cells = newCells,
                selectedRow = entry.row,
                selectedCol = entry.col,
                availableNumbers = calcAvailableNumbers(newCells),
            )
        )
    }

    private fun onNotesToggled() {
        setState(currentState.copy(isNotesEnabled = !currentState.isNotesEnabled))
    }

    private fun onHint() {
        val state = currentState
        if (state.hintsRemaining <= 0 || state.isGameOver) return

        val emptyCells = mutableListOf<Pair<Int, Int>>()
        for (r in 0 until 9) {
            for (c in 0 until 9) {
                val cell = state.cells[r][c]
                if (cell.value == 0 || cell.isError) {
                    emptyCells.add(r to c)
                }
            }
        }
        if (emptyCells.isEmpty()) return

        val (row, col) = emptyCells.random()
        val correctValue = state.solution[row][col]

        val newCells = state.cells.map { it.copyOf() }.toTypedArray()
        newCells[row][col] = CellData(value = correctValue, isGiven = true)
        clearNotesForNumber(newCells, row, col, correctValue)

        setState(
            state.copy(
                cells = newCells,
                hintsRemaining = state.hintsRemaining - 1,
                availableNumbers = calcAvailableNumbers(newCells),
            )
        )

        if (isBoardComplete(newCells)) {
            gameOver(isWin = true)
        }
    }

    private fun onPause() {
        timerJob?.cancel()
        setState(currentState.copy(isPaused = true))
    }

    private fun onResume() {
        setState(currentState.copy(isPaused = false))
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                if (!currentState.isPaused && !currentState.isGameOver) {
                    val newTime = currentState.timeSeconds + 1
                    val minutes = newTime / 60
                    val seconds = newTime % 60
                    setState(
                        currentState.copy(
                            timeSeconds = newTime,
                            timer = "%02d:%02d".format(minutes, seconds),
                        )
                    )
                }
            }
        }
    }

    private fun gameOver(isWin: Boolean) {
        timerJob?.cancel()
        setState(currentState.copy(isGameOver = true, isWin = isWin))
        setEffect(
            GameEffect.NavigateToGameOver(
                isWin = isWin,
                time = currentState.timer,
                errors = currentState.errors,
            )
        )
    }

    private fun clearNotesForNumber(cells: Array<Array<CellData>>, row: Int, col: Int, number: Int) {
        for (i in 0 until 9) {
            cells[row][i] = cells[row][i].copy(notes = cells[row][i].notes - number)
            cells[i][col] = cells[i][col].copy(notes = cells[i][col].notes - number)
        }
        val regionStartRow = (row / 3) * 3
        val regionStartCol = (col / 3) * 3
        for (r in regionStartRow until regionStartRow + 3) {
            for (c in regionStartCol until regionStartCol + 3) {
                cells[r][c] = cells[r][c].copy(notes = cells[r][c].notes - number)
            }
        }
    }

    private fun calcAvailableNumbers(cells: Array<Array<CellData>>): Set<Int> {
        val counts = IntArray(10)
        for (row in cells) {
            for (cell in row) {
                if (cell.value in 1..9 && !cell.isError) {
                    counts[cell.value]++
                }
            }
        }
        return (1..9).filter { counts[it] < 9 }.toSet()
    }

    private fun isBoardComplete(cells: Array<Array<CellData>>): Boolean {
        for (row in cells) {
            for (cell in row) {
                if (cell.value == 0 || cell.isError) return false
            }
        }
        return true
    }

    private data class UndoEntry(val row: Int, val col: Int, val previousCell: CellData)
}
