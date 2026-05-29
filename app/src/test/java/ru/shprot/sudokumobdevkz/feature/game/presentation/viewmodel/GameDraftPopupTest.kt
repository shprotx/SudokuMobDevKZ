package ru.shprot.sudokumobdevkz.feature.game.presentation.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.shprot.sudokumobdevkz.feature.game.domain.model.CellData
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameUIState

class GameDraftPopupTest {

    private fun makeBoard(
        emptyRow: Int = 0,
        emptyCol: Int = 0,
    ): List<List<CellData>> =
        List(9) { r ->
            List(9) { c ->
                if (r == emptyRow && c == emptyCol) CellData(value = 0, isGiven = false)
                else CellData(value = (r * 9 + c) % 9 + 1, isGiven = true)
            }
        }

    private fun baseState(cells: List<List<CellData>> = makeBoard()) =
        GameUIState(cells = cells, isGenerating = false)

    private fun openPopup(
        state: GameUIState,
        row: Int,
        col: Int,
    ): GameUIState {
        val cell = state.cells[row][col]
        if (state.isGenerating || state.isGameOver || state.isPaused) return state
        if (cell.isGiven || cell.value != 0) return state
        return state.copy(
            draftPopupVisible = true,
            draftPopupRow = row,
            draftPopupCol = col,
            selectedRow = row,
            selectedCol = col,
        )
    }

    private fun toggleNote(
        state: GameUIState,
        number: Int,
    ): GameUIState {
        if (!state.draftPopupVisible) return state
        val row = state.draftPopupRow
        val col = state.draftPopupCol
        val cell = state.cells[row][col]
        if (cell.isGiven || cell.value != 0) return state
        val newNotes = if (number in cell.notes) cell.notes - number else cell.notes + number
        val newCells = state.cells.map { it.toMutableList() }.toMutableList()
        newCells[row][col] = cell.copy(notes = newNotes)
        return state.copy(cells = newCells.map { it.toList() })
    }

    @Test
    fun longPress_emptyCell_opensPopup() {
        val state = baseState(cells = makeBoard(emptyRow = 2, emptyCol = 3))
        val result = openPopup(state, row = 2, col = 3)
        assertTrue(result.draftPopupVisible)
        assertEquals(2, result.draftPopupRow)
        assertEquals(3, result.draftPopupCol)
        assertEquals(2, result.selectedRow)
        assertEquals(3, result.selectedCol)
    }

    @Test
    fun longPress_givenCell_popupStaysClosed() {
        val state = baseState(cells = makeBoard(emptyRow = 0, emptyCol = 0))
        val givenRow = 1
        val givenCol = 1
        assertTrue(state.cells[givenRow][givenCol].isGiven)
        val result = openPopup(state, row = givenRow, col = givenCol)
        assertFalse(result.draftPopupVisible)
    }

    @Test
    fun longPress_cellWithValue_popupStaysClosed() {
        val cells = List(9) { r ->
            List(9) { c ->
                if (r == 0 && c == 0) CellData(value = 5, isGiven = false, isError = false)
                else CellData(value = (r * 9 + c) % 9 + 1, isGiven = true)
            }
        }
        val state = baseState(cells = cells)
        val result = openPopup(state, row = 0, col = 0)
        assertFalse(result.draftPopupVisible)
    }

    @Test
    fun longPress_whilePaused_popupStaysClosed() {
        val state = baseState().copy(isPaused = true)
        val result = openPopup(state, row = 0, col = 0)
        assertFalse(result.draftPopupVisible)
    }

    @Test
    fun longPress_whileGameOver_popupStaysClosed() {
        val state = baseState().copy(isGameOver = true)
        val result = openPopup(state, row = 0, col = 0)
        assertFalse(result.draftPopupVisible)
    }

    @Test
    fun toggleNote_addsNoteWhenAbsent() {
        val state = baseState().let { openPopup(it, row = 0, col = 0) }
        val result = toggleNote(state, number = 5)
        assertTrue(result.cells[0][0].notes.contains(5))
    }

    @Test
    fun toggleNote_removesNoteWhenPresent() {
        val cells = List(9) { r ->
            List(9) { c ->
                if (r == 0 && c == 0) CellData(value = 0, isGiven = false, notes = setOf(3, 7))
                else CellData(value = (r * 9 + c) % 9 + 1, isGiven = true)
            }
        }
        val state = baseState(cells = cells).let { openPopup(it, row = 0, col = 0) }
        val result = toggleNote(state, number = 3)
        val notes = result.cells[0][0].notes
        assertFalse(notes.contains(3))
        assertTrue(notes.contains(7))
    }

    @Test
    fun multipleToggles_popupRemainsOpen() {
        var state = baseState().let { openPopup(it, row = 0, col = 0) }
        state = toggleNote(state, number = 1)
        state = toggleNote(state, number = 2)
        state = toggleNote(state, number = 3)
        assertTrue(state.draftPopupVisible)
        assertEquals(setOf(1, 2, 3), state.cells[0][0].notes)
    }

    @Test
    fun dismissPopup_closesPopup() {
        val state = baseState()
            .let { openPopup(it, row = 0, col = 0) }
            .copy(draftPopupVisible = false)
        assertFalse(state.draftPopupVisible)
    }

    @Test
    fun toggleNote_independentOfIsNotesEnabled_whenFalse() {
        val state = baseState().copy(isNotesEnabled = false).let { openPopup(it, row = 0, col = 0) }
        val result = toggleNote(state, number = 4)
        assertTrue(result.cells[0][0].notes.contains(4))
    }

    @Test
    fun toggleNote_independentOfIsNotesEnabled_whenTrue() {
        val state = baseState().copy(isNotesEnabled = true).let { openPopup(it, row = 0, col = 0) }
        val result = toggleNote(state, number = 6)
        assertTrue(result.cells[0][0].notes.contains(6))
    }

    @Test
    fun toggleNote_whenPopupClosed_doesNotModifyBoard() {
        val state = baseState().copy(draftPopupVisible = false)
        val result = toggleNote(state, number = 5)
        assertTrue(result.cells[0][0].notes.isEmpty())
    }
}