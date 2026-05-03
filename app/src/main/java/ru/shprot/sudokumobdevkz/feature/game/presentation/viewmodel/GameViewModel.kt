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
import ru.shprot.sudokumobdevkz.core.base.domain.generator.SudokuGenerator
import ru.shprot.sudokumobdevkz.feature.game.presentation.navigation.GameRoutes
import ru.shprot.sudokumobdevkz.core.base.data.repository.GameSaveData
import ru.shprot.sudokumobdevkz.core.base.data.repository.SettingsRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.SudokuRepository
import androidx.navigation.toRoute
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: SudokuRepository,
    private val settingsRepository: SettingsRepository,
) : BaseViewModel<GameEvent, GameUiState, GameEffect>(GameUiState()) {

    private val route = savedStateHandle.toRoute<GameRoutes.GameScreen>()
    private val difficulty: Int = route.difficulty
    private val continueGame: Boolean = route.continueGame
    private val undoStack = mutableListOf<UndoEntry>()
    private var timerJob: Job? = null

    init {
        viewModelScope.launch(exceptionHandler) {
            if (continueGame) {
                val saved = repository.loadSavedGame()
                if (saved != null) {
                    restoreGame(saved)
                    return@launch
                }
            }
            startNewGame()
        }
    }

    override fun handleUIEvent(event: GameEvent) {
        when (event) {
            is GameEvent.CellClicked -> onCellClicked(event.row, event.col)
            is GameEvent.NumberClicked -> onNumberClicked(event.number)
            is GameEvent.EraseClicked -> onErase()
            is GameEvent.UndoClicked -> onUndo()
            is GameEvent.NotesToggled -> onNotesToggled()
            is GameEvent.HintClicked -> onHint()
            is GameEvent.DeselectClicked -> onDeselect()
            is GameEvent.PauseClicked -> onPause()
            is GameEvent.ResumeClicked -> onResume()
        }
    }

    private suspend fun startNewGame() {
        val settings = settingsRepository.currentSettings
        val maxErrors = if (settings.unlimitedErrors) Int.MAX_VALUE else 3
        val hints = if (settings.unlimitedHints) Int.MAX_VALUE else 3

        setState(currentState.copy(isGenerating = true, difficulty = difficulty, maxErrors = maxErrors, hintsRemaining = hints))
        repository.deleteSavedGame()

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

    private fun restoreGame(data: GameSaveData) {
        val cells = Array(9) { row ->
            Array(9) { col ->
                val s = data.cells[row][col]
                CellData(s.value, s.isGiven, s.isError, s.notes)
            }
        }
        val solution = Array(9) { row -> data.solution[row].toIntArray() }

        setState(
            currentState.copy(
                cells = cells,
                solution = solution,
                difficulty = data.difficulty,
                timeSeconds = data.timeSeconds,
                timer = "%02d:%02d".format(data.timeSeconds / 60, data.timeSeconds % 60),
                errors = data.errors,
                maxErrors = data.maxErrors,
                hintsRemaining = data.hintsRemaining,
                isNotesEnabled = data.isNotesEnabled,
                isGenerating = false,
                availableNumbers = calcAvailableNumbers(cells),
            )
        )

        startTimer()
    }

    private fun saveGameState() {
        val state = currentState
        if (state.isGenerating || state.isGameOver) return

        viewModelScope.launch(exceptionHandler) {
            repository.saveGame(
                GameSaveData(
                    difficulty = state.difficulty,
                    timeSeconds = state.timeSeconds,
                    errors = state.errors,
                    maxErrors = state.maxErrors,
                    hintsRemaining = state.hintsRemaining,
                    isNotesEnabled = state.isNotesEnabled,
                    cells = state.cells.map { row ->
                        row.map { c -> GameSaveData.CellSave(c.value, c.isGiven, c.isError, c.notes) }
                    },
                    solution = state.solution.map { it.toList() },
                )
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        saveGameState()
    }

    private fun onCellClicked(row: Int, col: Int) {
        if (currentState.isGenerating || currentState.isGameOver) return
        val cell = currentState.cells[row][col]
        val highlighted = if (cell.value != 0 && !cell.isError) cell.value else 0
        setState(currentState.copy(selectedRow = row, selectedCol = col, highlightedNumber = highlighted))
    }

    private fun onDeselect() {
        setState(currentState.copy(selectedRow = -1, selectedCol = -1, highlightedNumber = 0))
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

        val selectedRow = state.selectedRow
        val selectedCol = state.selectedCol
        val selectedCell = if (selectedRow >= 0 && selectedCol >= 0) state.cells[selectedRow][selectedCol] else null
        val useSelected = selectedCell != null && (selectedCell.value == 0 || selectedCell.isError)

        val row: Int
        val col: Int

        if (useSelected) {
            row = selectedRow
            col = selectedCol
        } else {
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
            val (r, c) = emptyCells.random()
            row = r
            col = c
        }

        val correctValue = state.solution[row][col]

        val newCells = state.cells.map { it.copyOf() }.toTypedArray()
        newCells[row][col] = CellData(value = correctValue, isGiven = true)
        clearNotesForNumber(newCells, row, col, correctValue)

        setState(
            state.copy(
                cells = newCells,
                selectedRow = row,
                selectedCol = col,
                hintsRemaining = state.hintsRemaining - 1,
                availableNumbers = calcAvailableNumbers(newCells),
                highlightedNumber = correctValue,
            )
        )

        if (isBoardComplete(newCells)) {
            gameOver(isWin = true)
        }
    }

    private fun onPause() {
        timerJob?.cancel()
        setState(currentState.copy(isPaused = true))
        saveGameState()
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

        viewModelScope.launch(exceptionHandler) {
            repository.deleteSavedGame()

            if (settingsRepository.currentSettings.effectiveTrackStatistics) {
                repository.updateStatistic(
                    difficulty = difficulty,
                    isWin = isWin,
                    timeSeconds = currentState.timeSeconds,
                    errorCount = currentState.errors,
                )
            }

            repository.saveGameResult(
                difficulty = difficulty,
                timeSeconds = currentState.timeSeconds,
                errors = currentState.errors,
                isWin = isWin,
            )
        }

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
