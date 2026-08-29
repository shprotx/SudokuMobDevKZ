package ru.shprot.sudokumobdevkz.feature.game.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.domain.model.ActionButtonId
import ru.shprot.sudokumobdevkz.core.base.domain.model.GameBlockId
import ru.shprot.sudokumobdevkz.core.base.domain.model.GameSaveData
import ru.shprot.sudokumobdevkz.core.base.domain.model.StatusItemId
import ru.shprot.sudokumobdevkz.core.base.domain.model.HintMode
import ru.shprot.sudokumobdevkz.core.base.data.util.DateTimeUtils
import ru.shprot.sudokumobdevkz.core.base.data.util.safeRunCatching
import ru.shprot.sudokumobdevkz.core.base.data.repository.AchievementsRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.DailyChallengeRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.ReviewRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.SettingsRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.SudokuRepository
import ru.shprot.sudokumobdevkz.core.base.domain.generator.SudokuGenerator
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import ru.shprot.sudokumobdevkz.core.base.presentation.viewmodel.BaseViewModel
import ru.shprot.sudokumobdevkz.feature.game.domain.model.CellData
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameUIEffect
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameUIEvent
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameUIState
import ru.shprot.sudokumobdevkz.feature.game.presentation.navigation.GameRoutes
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: SudokuRepository,
    private val settingsRepository: SettingsRepository,
    private val dailyChallengeRepository: DailyChallengeRepository,
    private val achievementsRepository: AchievementsRepository,
    private val reviewRepository: ReviewRepository,
) : BaseViewModel<GameUIEvent, GameUIState, GameUIEffect>(GameUIState()) {

    private val route = savedStateHandle.toRoute<GameRoutes.GameScreen>()
    private val difficulty: Difficulty = Difficulty.fromOrdinal(route.difficultyOrdinal)
    private val continueGame: Boolean = route.continueGame
    private var isDailyChallenge: Boolean = route.isDailyChallenge
    private var dailyDateKey: String = route.dailyDateKey
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
        viewModelScope.launch {
            settingsRepository.settings.collectLatest { settings ->
                updateState {
                    copy(
                        compactNumberPadPreference = settings.compactNumberPad,
                        selectedThemeId = settings.themeModeId,
                        blockOrder = settings.gameBlockOrder,
                        actionButtonOrder = settings.actionButtonOrder,
                        statusItemOrder = settings.statusItemOrder,
                    )
                }
            }
        }
    }

    override fun handleUIEvent(event: GameUIEvent) =
        when (event) {
            GameUIEvent.UndoClicked ->
                onUndo()

            GameUIEvent.EraseClicked ->
                onErase()

            GameUIEvent.NotesToggled ->
                onNotesToggled()

            GameUIEvent.HintClicked ->
                onHint()

            GameUIEvent.DeselectClicked ->
                onDeselect()

            GameUIEvent.PauseClicked ->
                onPause()

            GameUIEvent.ResumeClicked ->
                onResume()

            GameUIEvent.BackClicked ->
                setEffect(GameUIEffect.NavigateBack)

            GameUIEvent.NewGameClicked ->
                setState(currentState.copy(showNewGameDialog = true))

            GameUIEvent.ShowPauseDialog ->
                handleShowPauseDialog()

            GameUIEvent.DismissPauseDialog ->
                setState(currentState.copy(showPauseDialog = false))

            GameUIEvent.LayoutEditClicked ->
                handleLayoutEditToggle()

            GameUIEvent.LayoutEditExitRequested ->
                handleLayoutEditExit()

            GameUIEvent.LayoutResetClicked ->
                handleLayoutReset()

            is GameUIEvent.BlockMoved ->
                handleBlockMoved(event.from, event.to)

            is GameUIEvent.EditBlockTapped ->
                handleEditBlockTapped(event.blockId)

            is GameUIEvent.InnerItemMoved ->
                handleInnerItemMoved(event.blockId, event.from, event.to)

            GameUIEvent.ShowNewGameDialog ->
                setState(currentState.copy(showNewGameDialog = true, showPauseDialog = false))

            GameUIEvent.DismissNewGameDialog ->
                setState(currentState.copy(showNewGameDialog = false))

            GameUIEvent.ExitGame ->
                handleExitGame()

            GameUIEvent.SaveState ->
                autoSave()

            GameUIEvent.SettingsClicked ->
                setEffect(GameUIEffect.NavigateToSettings)

            GameUIEvent.PaletteClicked ->
                updateState { copy(themePopupExpanded = true) }

            GameUIEvent.DismissThemePopup ->
                updateState { copy(themePopupExpanded = false) }

            GameUIEvent.DismissDraftPopup ->
                updateState { copy(draftPopupVisible = false) }

            GameUIEvent.TimerSuspend ->
                onTimerSuspend()

            GameUIEvent.TimerResume ->
                onTimerResume()

            is GameUIEvent.CellClicked ->
                onCellClicked(event.row, event.col)

            is GameUIEvent.NumberClicked ->
                onNumberClicked(event.number)

            is GameUIEvent.StartNewGame ->
                handleStartNewGame(event.difficultyOrdinal)

            is GameUIEvent.ThemeSelected ->
                handleThemeSelected(event.themeId)

            is GameUIEvent.CellLongPressed ->
                onCellLongPressed(event.row, event.col)

            is GameUIEvent.DraftNoteToggled ->
                onDraftNoteToggled(event.number)
        }

    private fun handleThemeSelected(themeId: String) {
        settingsRepository.update { copy(themeModeId = themeId) }
    }

    private fun handleShowPauseDialog() {
        onPause()
        setState(currentState.copy(showPauseDialog = true))
    }

    private fun handleLayoutEditToggle() {
        if (currentState.isLayoutEditMode) {
            setState(currentState.copy(isLayoutEditMode = false, expandedEditBlock = null))
            onResume()
        } else {
            onPause()
            setState(currentState.copy(isLayoutEditMode = true))
        }
    }

    private fun handleLayoutEditExit() {
        if (!currentState.isLayoutEditMode) return
        setState(currentState.copy(isLayoutEditMode = false, expandedEditBlock = null))
        onResume()
    }

    private fun handleLayoutReset() {
        setState(
            currentState.copy(
                blockOrder = GameBlockId.DEFAULT_ORDER,
                actionButtonOrder = ActionButtonId.DEFAULT_ORDER,
                statusItemOrder = StatusItemId.DEFAULT_ORDER,
                expandedEditBlock = null,
            ),
        )
        settingsRepository.update {
            copy(
                gameBlockOrder = GameBlockId.DEFAULT_ORDER,
                actionButtonOrder = ActionButtonId.DEFAULT_ORDER,
                statusItemOrder = StatusItemId.DEFAULT_ORDER,
            )
        }
    }

    private fun handleBlockMoved(from: Int, to: Int) {
        val order = currentState.blockOrder.toMutableList()
        if (from !in order.indices || to !in order.indices) return
        order.add(to, order.removeAt(from))
        setState(currentState.copy(blockOrder = order))
        settingsRepository.update { copy(gameBlockOrder = order) }
    }

    private fun handleEditBlockTapped(blockId: GameBlockId) {
        if (blockId != GameBlockId.ACTIONS_BAR && blockId != GameBlockId.STATUS_BAR) return
        val expanded = if (currentState.expandedEditBlock == blockId) null else blockId
        setState(currentState.copy(expandedEditBlock = expanded))
    }

    private fun handleInnerItemMoved(blockId: GameBlockId, from: Int, to: Int) {
        when (blockId) {
            GameBlockId.ACTIONS_BAR -> {
                val order = currentState.actionButtonOrder.toMutableList()
                if (from !in order.indices || to !in order.indices) return
                order.add(to, order.removeAt(from))
                setState(currentState.copy(actionButtonOrder = order))
                settingsRepository.update { copy(actionButtonOrder = order) }
            }

            GameBlockId.STATUS_BAR -> {
                val order = currentState.statusItemOrder.toMutableList()
                if (from !in order.indices || to !in order.indices) return
                order.add(to, order.removeAt(from))
                setState(currentState.copy(statusItemOrder = order))
                settingsRepository.update { copy(statusItemOrder = order) }
            }

            else -> Unit
        }
    }

    private fun handleExitGame() {
        setState(currentState.copy(showPauseDialog = false))
        setEffect(GameUIEffect.NavigateBack)
    }

    private fun handleStartNewGame(difficultyOrdinal: Int) {
        setState(currentState.copy(showNewGameDialog = false))
        setEffect(GameUIEffect.NavigateToNewGame(difficultyOrdinal))
    }

    private suspend fun startNewGame() {
        val settings = settingsRepository.currentSettings
        val maxErrors = if (settings.unlimitedErrors) Int.MAX_VALUE else 3
        val hints = if (settings.unlimitedHints) Int.MAX_VALUE else 3
        val isStandard = settings.isStandardMode

        setState(
            currentState.copy(
                isGenerating = true,
                difficulty = difficulty,
                maxErrors = maxErrors,
                hintsRemaining = hints,
                isStandardMode = isStandard,
                isDailyChallenge = isDailyChallenge,
            )
        )
        countAbandonedGame()
        repository.deleteSavedGame()

        val puzzle = if (isDailyChallenge) {
            val dateKey = resolvedDailyDateKey()
            dailyDateKey = dateKey
            val seed = dailyChallengeRepository.dailySeed(dateKey)
            SudokuGenerator.generate(difficulty, seed)
        } else {
            SudokuGenerator.generate(difficulty)
        }

        val cells = List(9) { row ->
            List(9) { col ->
                val value = puzzle.puzzle[row][col]
                CellData(value = value, isGiven = value != 0)
            }
        }
        val solution = puzzle.solution.map { it.toList() }

        setState(
            currentState.copy(
                cells = cells,
                solution = solution,
                isGenerating = false,
                availableNumbers = calcAvailableNumbers(cells),
            )
        )

        startTimer()
    }

    private suspend fun countAbandonedGame() {
        val saved = repository.loadSavedGame() ?: return
        if (saved.isDailyChallenge) return
        val savedDifficulty = Difficulty.fromFirebaseKey(saved.difficulty) ?: return
        if (saved.isStandardMode) {
            repository.updateStatistic(
                difficulty = savedDifficulty,
                isWin = false,
                timeSeconds = saved.timeSeconds,
                errorCount = saved.errors,
            )
        } else {
            repository.incrementCasualGames(savedDifficulty)
        }
    }

    private fun restoreGame(data: GameSaveData) {
        val cells = data.cells.map { row ->
            row.map { s -> CellData(s.value, s.isGiven, s.isError, s.notes) }
        }
        val restoredDifficulty = Difficulty.fromFirebaseKey(data.difficulty) ?: difficulty
        isDailyChallenge = data.isDailyChallenge
        dailyDateKey = data.dailyDateKey

        setState(
            currentState.copy(
                cells = cells,
                solution = data.solution,
                difficulty = restoredDifficulty,
                timeSeconds = data.timeSeconds,
                timer = DateTimeUtils.formatTimer(data.timeSeconds),
                errors = data.errors,
                maxErrors = data.maxErrors,
                hintsRemaining = data.hintsRemaining,
                isNotesEnabled = data.isNotesEnabled,
                isStandardMode = data.isStandardMode,
                isDailyChallenge = data.isDailyChallenge,
                isGenerating = false,
                availableNumbers = calcAvailableNumbers(cells),
            )
        )

        startTimer()
    }

    private suspend fun saveGameStateSync() {
        val state = currentState
        if (state.isGenerating || state.isGameOver) return

        repository.saveGame(
            GameSaveData(
                difficulty = state.difficulty.firebaseKey,
                timeSeconds = state.timeSeconds,
                errors = state.errors,
                maxErrors = state.maxErrors,
                hintsRemaining = state.hintsRemaining,
                isNotesEnabled = state.isNotesEnabled,
                cells = state.cells.map { row ->
                    row.map { c -> GameSaveData.CellSave(c.value, c.isGiven, c.isError, c.notes) }
                },
                solution = state.solution,
                isStandardMode = state.isStandardMode,
                isDailyChallenge = state.isDailyChallenge,
                dailyDateKey = if (state.isDailyChallenge) resolvedDailyDateKey() else "",
            )
        )
    }

    private fun resolvedDailyDateKey(): String =
        dailyDateKey.ifEmpty { dailyChallengeRepository.todayDateKey() }

    override fun onCleared() {
        super.onCleared()
        kotlinx.coroutines.runBlocking {
            withContext(NonCancellable) {
                if (currentState.isGameOver) {
                    safeRunCatching { repository.deleteSavedGame() }
                } else {
                    safeRunCatching { saveGameStateSync() }
                }
            }
        }
    }

    private fun autoSave() {
        updateState { copy(isHintModeActive = false) }
        viewModelScope.launch(exceptionHandler) { saveGameStateSync() }
    }

    private fun onCellClicked(row: Int, col: Int) {
        if (currentState.isGenerating || currentState.isGameOver) return
        if (currentState.isHintModeActive) {
            applyHintToCell(row, col)
            return
        }
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
        if (cell.value != 0 && !cell.isError) return

        if (state.isNotesEnabled) {
            val newNotes = cell.notes.toMutableSet()
            if (number in newNotes) newNotes.remove(number) else newNotes.add(number)

            undoStack.add(UndoEntry(row, col, cell))
            val newCells = state.cells.toMutableGrid()
            newCells[row][col] = cell.copy(notes = newNotes)

            setState(state.copy(cells = newCells.toImmutableGrid()))
            return
        }

        val correctValue = state.solution[row][col]
        val isCorrect = number == correctValue
        val shouldCheckErrors = settingsRepository.currentSettings.checkErrors

        undoStack.add(UndoEntry(row, col, cell))
        val newCells = state.cells.toMutableGrid()

        if (isCorrect) {
            newCells[row][col] = CellData(value = number, isGiven = false, isError = false)
            clearNotesForNumber(newCells, row, col, number)
        } else if (shouldCheckErrors) {
            newCells[row][col] = CellData(value = number, isGiven = false, isError = true)
        } else {
            newCells[row][col] = CellData(value = number, isGiven = false, isError = false)
        }

        val immutable = newCells.toImmutableGrid()
        val newErrors = if (isCorrect || !shouldCheckErrors) state.errors else state.errors + 1

        setState(
            state.copy(
                cells = immutable,
                errors = newErrors,
                availableNumbers = calcAvailableNumbers(immutable),
                highlightedNumber = if (isCorrect || !shouldCheckErrors) number else 0,
            )
        )

        if (shouldCheckErrors && newErrors >= state.maxErrors) {
            gameOver(isWin = false)
        } else {
            checkBoardCompletion(immutable)
        }
    }

    private fun onErase() {
        val state = currentState
        val row = state.selectedRow
        val col = state.selectedCol
        if (row < 0 || col < 0) return

        val cell = state.cells[row][col]
        if (cell.isGiven) return
        if (cell.value != 0 && !cell.isError) return

        undoStack.add(UndoEntry(row, col, cell))
        val newCells = state.cells.toMutableGrid()
        newCells[row][col] = CellData()
        val immutable = newCells.toImmutableGrid()

        setState(
            state.copy(
                cells = immutable,
                availableNumbers = calcAvailableNumbers(immutable),
            )
        )
    }

    private fun onUndo() {
        if (undoStack.isEmpty()) return
        val entry = undoStack.removeAt(undoStack.size - 1)
        val newCells = currentState.cells.toMutableGrid()
        newCells[entry.row][entry.col] = entry.previousCell
        val immutable = newCells.toImmutableGrid()

        setState(
            currentState.copy(
                cells = immutable,
                selectedRow = entry.row,
                selectedCol = entry.col,
                availableNumbers = calcAvailableNumbers(immutable),
            )
        )
    }

    private fun onNotesToggled() {
        val newNotesEnabled = !currentState.isNotesEnabled
        setState(currentState.copy(
            isNotesEnabled = newNotesEnabled,
            isHintModeActive = if (newNotesEnabled) false else currentState.isHintModeActive,
        ))
    }

    private fun onHint() {
        val state = currentState
        if (state.isGameOver) return

        val hintMode = settingsRepository.currentSettings.hintMode

        if (hintMode == HintMode.TOGGLE) {
            if (state.isHintModeActive) {
                updateState { copy(isHintModeActive = false) }
                return
            }
            if (state.hintsRemaining <= 0) {
                setEffect(GameUIEffect.ShowMessage(R.string.hints_exhausted))
                return
            }
            updateState { copy(isHintModeActive = true, isNotesEnabled = false) }
            return
        }

        if (state.hintsRemaining <= 0) return
        applyHintSingleShot(state)
    }

    private fun applyHintSingleShot(state: GameUIState) {
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
                    if (cell.value == 0 || cell.isError) emptyCells.add(r to c)
                }
            }
            if (emptyCells.isEmpty()) return
            val (r, c) = emptyCells.random()
            row = r
            col = c
        }

        val correctValue = state.solution[row][col]
        val newCells = state.cells.toMutableGrid()
        newCells[row][col] = CellData(value = correctValue, isGiven = true)
        clearNotesForNumber(newCells, row, col, correctValue)
        val immutable = newCells.toImmutableGrid()

        setState(
            state.copy(
                cells = immutable,
                selectedRow = row,
                selectedCol = col,
                hintsRemaining = state.hintsRemaining - 1,
                availableNumbers = calcAvailableNumbers(immutable),
                highlightedNumber = correctValue,
            )
        )

        checkBoardCompletion(immutable)
    }

    private fun applyHintToCell(row: Int, col: Int) {
        val state = currentState
        val cell = state.cells[row][col]
        if (cell.isGiven) return
        if (cell.value != 0 && !cell.isError) return

        val correctValue = state.solution[row][col]
        val newCells = state.cells.toMutableGrid()
        newCells[row][col] = CellData(value = correctValue, isGiven = true)
        clearNotesForNumber(newCells, row, col, correctValue)
        val immutable = newCells.toImmutableGrid()

        val newHintsRemaining = state.hintsRemaining - 1
        val hintsExhausted = newHintsRemaining <= 0

        setState(
            state.copy(
                cells = immutable,
                selectedRow = row,
                selectedCol = col,
                hintsRemaining = newHintsRemaining,
                availableNumbers = calcAvailableNumbers(immutable),
                highlightedNumber = correctValue,
                isHintModeActive = !hintsExhausted,
            )
        )

        if (hintsExhausted) {
            setEffect(GameUIEffect.ShowMessage(R.string.hints_exhausted))
        }

        checkBoardCompletion(immutable)
    }

    private fun onPause() {
        timerJob?.cancel()
        setState(currentState.copy(isPaused = true, isHintModeActive = false))
        viewModelScope.launch(exceptionHandler) { saveGameStateSync() }
    }

    private fun onResume() {
        setState(currentState.copy(isPaused = false))
        startTimer()
    }

    private fun onTimerSuspend() {
        timerJob?.cancel()
    }

    private fun onTimerResume() {
        if (!currentState.isPaused && !currentState.isGameOver && !currentState.isGenerating) {
            startTimer()
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                if (!currentState.isPaused && !currentState.isGameOver) {
                    val newTime = currentState.timeSeconds + 1
                    setState(
                        currentState.copy(
                            timeSeconds = newTime,
                            timer = DateTimeUtils.formatTimer(newTime),
                        )
                    )
                }
            }
        }
    }

    private fun gameOver(isWin: Boolean) {
        timerJob?.cancel()
        setState(currentState.copy(isGameOver = true, isWin = isWin, isHintModeActive = false))

        viewModelScope.launch(exceptionHandler) {
            if (isWin) reviewRepository.markSessionWon()
            repository.deleteSavedGame()

            val newStreak = when {
                isDailyChallenge && isWin ->
                    persistDailyChallengeWin()

                isDailyChallenge ->
                    0

                else ->
                    persistRegularGameResult(isWin)
            }

            val unlocked = achievementsRepository.checkAndUnlock(emitToFlow = false)
            when {
                unlocked.isEmpty() -> Unit
                unlocked.size > 1 -> achievementsRepository.emitRetroactiveBatch(unlocked.size)
                else -> achievementsRepository.emitUnlockedToFlow(unlocked)
            }

            setEffect(
                GameUIEffect.NavigateToGameOver(
                    isWin = isWin,
                    time = currentState.timer,
                    errors = currentState.errors,
                    isDailyChallenge = isDailyChallenge,
                    newStreak = newStreak,
                )
            )
        }
    }

    private suspend fun persistRegularGameResult(isWin: Boolean): Int {
        val resultDifficulty = currentState.difficulty
        if (currentState.isStandardMode) {
            repository.updateStatistic(
                difficulty = resultDifficulty,
                isWin = isWin,
                timeSeconds = currentState.timeSeconds,
                errorCount = currentState.errors,
            )
        } else {
            repository.incrementCasualGames(resultDifficulty)
        }

        repository.saveGameResult(
            difficulty = resultDifficulty,
            timeSeconds = currentState.timeSeconds,
            errors = currentState.errors,
            isWin = isWin,
            hintsUsed = calculateHintsUsed(),
            isDaily = false,
            isStandardMode = currentState.isStandardMode,
        )

        if (isWin && currentState.timeSeconds > 0 && currentState.isStandardMode) {
            repository.submitLeaderboardForWin(
                difficulty = resultDifficulty,
                timeSeconds = currentState.timeSeconds,
                errors = currentState.errors,
                hintsUsed = calculateHintsUsed(),
                isDaily = false,
            )
        }

        return 0
    }

    private suspend fun persistDailyChallengeWin(): Int {
        val resultDifficulty = currentState.difficulty
        val dateKey = resolvedDailyDateKey()
        val streak = dailyChallengeRepository.markCompleted(
            dateKey = dateKey,
            timeSeconds = currentState.timeSeconds,
            errors = currentState.errors,
        )

        repository.saveGameResult(
            difficulty = resultDifficulty,
            timeSeconds = currentState.timeSeconds,
            errors = currentState.errors,
            isWin = true,
            hintsUsed = calculateHintsUsed(),
            isDaily = true,
            isStandardMode = currentState.isStandardMode,
        )

        if (currentState.timeSeconds > 0) {
            repository.submitLeaderboardForWin(
                difficulty = resultDifficulty,
                timeSeconds = currentState.timeSeconds,
                errors = currentState.errors,
                hintsUsed = calculateHintsUsed(),
                isDaily = true,
            )
        }

        return streak
    }

    private fun calculateHintsUsed(): Int {
        val settings = settingsRepository.currentSettings
        val initial = if (settings.unlimitedHints) Int.MAX_VALUE else HINTS_INITIAL
        return (initial - currentState.hintsRemaining).coerceAtLeast(0)
    }

    private fun clearNotesForNumber(cells: MutableList<MutableList<CellData>>, row: Int, col: Int, number: Int) {
        for (i in 0 until 9) {
            cells[row][i] = cells[row][i].copy(notes = cells[row][i].notes - number)
            cells[i][col] = cells[i][col].copy(notes = cells[i][col].notes - number)
        }
        val rStart = (row / 3) * 3
        val cStart = (col / 3) * 3
        for (r in rStart until rStart + 3) {
            for (c in cStart until cStart + 3) {
                cells[r][c] = cells[r][c].copy(notes = cells[r][c].notes - number)
            }
        }
    }

    private fun calcAvailableNumbers(cells: List<List<CellData>>): Set<Int> {
        val counts = IntArray(10)
        for (row in cells) {
            for (cell in row) {
                if (cell.value in 1..9 && !cell.isError) counts[cell.value]++
            }
        }
        return (1..9).filter { counts[it] < 9 }.toSet()
    }

    private fun checkBoardCompletion(cells: List<List<CellData>>) {
        val allFilled = cells.all { row -> row.all { it.value != 0 } }
        if (!allFilled) return

        val state = currentState
        val isCorrect = cells.indices.all { r ->
            cells[r].indices.all { c ->
                cells[r][c].value == state.solution[r][c]
            }
        }
        gameOver(isWin = isCorrect)
    }

    private fun onCellLongPressed(row: Int, col: Int) {
        val state = currentState
        if (state.isGenerating || state.isGameOver || state.isPaused) return
        val cell = state.cells[row][col]
        if (cell.isGiven || cell.value != 0) return
        updateState {
            copy(
                draftPopupVisible = true,
                draftPopupRow = row,
                draftPopupCol = col,
                selectedRow = row,
                selectedCol = col,
            )
        }
    }

    private fun onDraftNoteToggled(number: Int) {
        val state = currentState
        if (!state.draftPopupVisible) return
        val row = state.draftPopupRow
        val col = state.draftPopupCol
        val cell = state.cells[row][col]
        if (cell.isGiven || cell.value != 0) return
        val newNotes = if (number in cell.notes) cell.notes - number else cell.notes + number
        undoStack.add(UndoEntry(row, col, cell))
        val newCells = state.cells.toMutableGrid()
        newCells[row][col] = cell.copy(notes = newNotes)
        setState(state.copy(cells = newCells.toImmutableGrid()))
    }

    private fun List<List<CellData>>.toMutableGrid(): MutableList<MutableList<CellData>> =
        map { it.toMutableList() }.toMutableList()

    private fun MutableList<MutableList<CellData>>.toImmutableGrid(): List<List<CellData>> =
        map { it.toList() }

    private data class UndoEntry(val row: Int, val col: Int, val previousCell: CellData)

    private companion object {
        const val HINTS_INITIAL = 3
    }
}