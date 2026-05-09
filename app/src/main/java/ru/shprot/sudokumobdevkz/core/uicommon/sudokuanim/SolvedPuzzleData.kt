package ru.shprot.sudokumobdevkz.core.uicommon.sudokuanim

object SolvedPuzzleData {

    val SOLVED_GRID = arrayOf(
        intArrayOf(5, 3, 4, 6, 7, 8, 9, 1, 2),
        intArrayOf(6, 7, 2, 1, 9, 5, 3, 4, 8),
        intArrayOf(1, 9, 8, 3, 4, 2, 5, 6, 7),
        intArrayOf(8, 5, 9, 7, 6, 1, 4, 2, 3),
        intArrayOf(4, 2, 6, 8, 5, 3, 7, 9, 1),
        intArrayOf(7, 1, 3, 9, 2, 4, 8, 5, 6),
        intArrayOf(9, 6, 1, 5, 3, 7, 2, 8, 4),
        intArrayOf(2, 8, 7, 4, 1, 9, 6, 3, 5),
        intArrayOf(3, 4, 5, 2, 8, 6, 1, 7, 9),
    )

    val INITIAL_FILLED = setOf(
        GridPoint(0, 0), GridPoint(0, 1), GridPoint(0, 4), GridPoint(0, 8),
        GridPoint(1, 2), GridPoint(1, 5), GridPoint(1, 7),
        GridPoint(2, 0), GridPoint(2, 3), GridPoint(2, 6),
        GridPoint(3, 1), GridPoint(3, 4), GridPoint(3, 8),
        GridPoint(4, 0), GridPoint(4, 3), GridPoint(4, 5), GridPoint(4, 8),
        GridPoint(5, 0), GridPoint(5, 4), GridPoint(5, 7),
        GridPoint(6, 2), GridPoint(6, 5), GridPoint(6, 8),
        GridPoint(7, 1), GridPoint(7, 3), GridPoint(7, 6),
        GridPoint(8, 0), GridPoint(8, 4), GridPoint(8, 7), GridPoint(8, 8),
    )
}
