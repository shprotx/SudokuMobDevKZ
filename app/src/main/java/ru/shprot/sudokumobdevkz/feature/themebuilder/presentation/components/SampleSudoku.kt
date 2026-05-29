package ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.components

import ru.shprot.sudokumobdevkz.feature.game.domain.model.CellData

internal object SampleSudoku {

    const val SELECTED_ROW = 4
    const val SELECTED_COL = 2
    const val HIGHLIGHTED_NUMBER = 8

    fun cells(): List<List<CellData>> {
        fun given(value: Int) = CellData(value = value, isGiven = true)
        fun user(value: Int) = CellData(value = value)
        fun error(value: Int) = CellData(value = value, isError = true)
        fun notes(vararg n: Int) = CellData(notes = n.toSet())
        val empty = CellData()

        return listOf(
            listOf(given(5), given(3), user(4), empty, given(7), empty, empty, empty, given(8)),
            listOf(given(6), empty, notes(1, 2, 4), given(1), given(9), given(5), empty, empty, empty),
            listOf(empty, given(9), given(8), empty, empty, empty, empty, given(6), empty),
            listOf(given(8), empty, empty, empty, given(6), empty, empty, empty, given(3)),
            listOf(given(4), empty, empty, given(8), empty, given(3), notes(2, 5, 9), empty, given(1)),
            listOf(given(7), error(1), empty, empty, given(2), empty, empty, empty, given(6)),
            listOf(empty, given(6), empty, empty, empty, empty, given(2), given(8), empty),
            listOf(empty, empty, empty, given(4), given(1), given(9), empty, empty, given(5)),
            listOf(empty, empty, empty, empty, given(8), empty, empty, given(7), given(9)),
        )
    }
}
