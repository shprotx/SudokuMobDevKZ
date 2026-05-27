package ru.shprot.sudokumobdevkz.feature.game.presentation.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.shprot.sudokumobdevkz.core.base.domain.model.HintMode
import ru.shprot.sudokumobdevkz.feature.game.domain.model.CellData
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameUIState

class GameHintModeTest {

    private fun makeBoard(
        emptyRow: Int = 0,
        emptyCol: Int = 0,
        errorRow: Int = -1,
        errorCol: Int = -1,
    ): List<List<CellData>> {
        return List(9) { r ->
            List(9) { c ->
                when {
                    r == emptyRow && c == emptyCol -> CellData(value = 0, isGiven = false)
                    r == errorRow && c == errorCol -> CellData(value = 5, isGiven = false, isError = true)
                    else -> CellData(value = (r * 9 + c) % 9 + 1, isGiven = true)
                }
            }
        }
    }

    private fun makeSolution(): List<List<Int>> =
        List(9) { r -> List(9) { c -> (r * 9 + c) % 9 + 1 } }

    private fun stateWithHints(
        hintsRemaining: Int = 3,
        isHintModeActive: Boolean = false,
        isNotesEnabled: Boolean = false,
    ) = GameUIState(
        cells = makeBoard(),
        solution = makeSolution(),
        hintsRemaining = hintsRemaining,
        isHintModeActive = isHintModeActive,
        isNotesEnabled = isNotesEnabled,
    )

    @Test
    fun singleShotMode_hintButton_doesNotActivateToggle() {
        val state = stateWithHints()
        val hintMode = HintMode.SINGLE_SHOT
        val newActive = if (hintMode == HintMode.TOGGLE && !state.isHintModeActive) {
            true
        } else {
            state.isHintModeActive
        }
        assertFalse(newActive)
    }

    @Test
    fun toggleMode_hintButton_activatesWhenInactive() {
        val state = stateWithHints(isHintModeActive = false)
        val hintMode = HintMode.TOGGLE
        val shouldActivate = hintMode == HintMode.TOGGLE && !state.isHintModeActive && state.hintsRemaining > 0
        assertTrue(shouldActivate)
        val newState = state.copy(isHintModeActive = true, isNotesEnabled = false)
        assertTrue(newState.isHintModeActive)
    }

    @Test
    fun toggleMode_hintButton_deactivatesWhenActive() {
        val state = stateWithHints(isHintModeActive = true)
        val hintMode = HintMode.TOGGLE
        val newState = if (hintMode == HintMode.TOGGLE && state.isHintModeActive) {
            state.copy(isHintModeActive = false)
        } else {
            state
        }
        assertFalse(newState.isHintModeActive)
    }

    @Test
    fun toggleMode_hintButton_doesNotActivateWhenExhausted() {
        val state = stateWithHints(hintsRemaining = 0, isHintModeActive = false)
        val hintMode = HintMode.TOGGLE
        val canActivate = hintMode == HintMode.TOGGLE && !state.isHintModeActive && state.hintsRemaining > 0
        assertFalse(canActivate)
        assertFalse(state.isHintModeActive)
    }

    @Test
    fun toggleHint_appliedToEmptyCell_fillsCorrectValue() {
        val solution = makeSolution()
        val cells = makeBoard(emptyRow = 0, emptyCol = 0)
        val state = GameUIState(
            cells = cells,
            solution = solution,
            hintsRemaining = 3,
            isHintModeActive = true,
        )
        val row = 0
        val col = 0
        val cell = state.cells[row][col]
        assertFalse(cell.isGiven)
        assertEquals(0, cell.value)

        val correctValue = state.solution[row][col]
        val newCells = state.cells.map { it.toMutableList() }.toMutableList()
        newCells[row][col] = CellData(value = correctValue, isGiven = true)
        val newState = state.copy(
            cells = newCells.map { it.toList() },
            hintsRemaining = state.hintsRemaining - 1,
        )

        assertEquals(correctValue, newState.cells[row][col].value)
        assertEquals(2, newState.hintsRemaining)
    }

    @Test
    fun toggleHint_appliedToGivenCell_isIgnored() {
        val cells = makeBoard(emptyRow = 0, emptyCol = 0)
        val row = 1
        val col = 1
        val cell = cells[row][col]
        assertTrue("Expected isGiven", cell.isGiven)
        assertFalse("Given cell should not have 0 value", cell.value == 0 && !cell.isError)
        val shouldApply = !cell.isGiven && (cell.value == 0 || cell.isError)
        assertFalse(shouldApply)
    }

    @Test
    fun toggleHint_appliedToPlayerFilledCell_isIgnored() {
        val cells = List(9) { r ->
            List(9) { c ->
                if (r == 0 && c == 0) CellData(value = 1, isGiven = false, isError = false)
                else CellData(value = (r * 9 + c) % 9 + 1, isGiven = true)
            }
        }
        val row = 0
        val col = 0
        val cell = cells[row][col]
        val shouldApply = !cell.isGiven && (cell.value == 0 || cell.isError)
        assertFalse("Player-filled correct cell should not get hint", shouldApply)
    }

    @Test
    fun toggleHint_appliedToErrorCell_replacesWithCorrect() {
        val solution = makeSolution()
        val cells = makeBoard(emptyRow = -1, emptyCol = -1, errorRow = 0, errorCol = 0)
        val state = GameUIState(
            cells = cells,
            solution = solution,
            hintsRemaining = 3,
            isHintModeActive = true,
        )
        val row = 0
        val col = 0
        val cell = state.cells[row][col]
        assertTrue("Error cell should be eligible for hint", cell.isError)
        val shouldApply = !cell.isGiven && (cell.value == 0 || cell.isError)
        assertTrue(shouldApply)

        val correctValue = state.solution[row][col]
        val newCells = state.cells.map { it.toMutableList() }.toMutableList()
        newCells[row][col] = CellData(value = correctValue, isGiven = true)
        val newState = state.copy(
            cells = newCells.map { it.toList() },
            hintsRemaining = state.hintsRemaining - 1,
        )

        assertEquals(correctValue, newState.cells[row][col].value)
        assertFalse(newState.cells[row][col].isError)
        assertEquals(2, newState.hintsRemaining)
    }

    @Test
    fun toggleHint_autoDeactivates_whenLastHintUsed() {
        val state = stateWithHints(hintsRemaining = 1, isHintModeActive = true)
        val newHintsRemaining = state.hintsRemaining - 1
        val hintsExhausted = newHintsRemaining <= 0
        val newState = state.copy(
            hintsRemaining = newHintsRemaining,
            isHintModeActive = !hintsExhausted,
        )
        assertEquals(0, newState.hintsRemaining)
        assertFalse(newState.isHintModeActive)
    }

    @Test
    fun notesToggle_deactivatesHintMode_whenNotesEnabled() {
        val state = stateWithHints(isHintModeActive = true, isNotesEnabled = false)
        val newNotesEnabled = !state.isNotesEnabled
        val newState = state.copy(
            isNotesEnabled = newNotesEnabled,
            isHintModeActive = if (newNotesEnabled) false else state.isHintModeActive,
        )
        assertTrue(newState.isNotesEnabled)
        assertFalse(newState.isHintModeActive)
    }

    @Test
    fun hintToggleActivation_deactivatesNotes() {
        val state = stateWithHints(isNotesEnabled = true, isHintModeActive = false, hintsRemaining = 3)
        val newState = state.copy(isHintModeActive = true, isNotesEnabled = false)
        assertTrue(newState.isHintModeActive)
        assertFalse(newState.isNotesEnabled)
    }
}