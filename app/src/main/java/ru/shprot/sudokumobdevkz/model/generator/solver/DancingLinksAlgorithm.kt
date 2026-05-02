package ru.shprot.sudokumobdevkz.model.generator.solver

internal object DancingLinksAlgorithm {

    private const val BOARD_SIZE = 9
    private const val SUBSECTION_SIZE = 3
    private const val NO_VALUE = 0
    private const val CONSTRAINTS = 4
    private const val MIN_VALUE = 1
    private const val MAX_VALUE = 9
    private const val COVER_START_INDEX = 1

    fun countSolutions(board: Array<IntArray>): Int {
        val cover = initializeExactCoverBoard(board)
        val dlx = DancingLinks(cover)
        dlx.runSolver()
        return dlx.solutionsCount
    }

    private fun getIndex(row: Int, column: Int, num: Int): Int =
        (row - 1) * BOARD_SIZE * BOARD_SIZE + (column - 1) * BOARD_SIZE + (num - 1)

    private fun createExactCoverBoard(): BooleanArray2D {
        val coverBoard = BooleanArray2D(
            BOARD_SIZE * BOARD_SIZE * MAX_VALUE,
            BOARD_SIZE * BOARD_SIZE * CONSTRAINTS,
        )
        var hBase = 0
        hBase = checkCellConstraint(coverBoard, hBase)
        hBase = checkRowConstraint(coverBoard, hBase)
        hBase = checkColumnConstraint(coverBoard, hBase)
        checkSubsectionConstraint(coverBoard, hBase)
        return coverBoard
    }

    private fun checkSubsectionConstraint(coverBoard: BooleanArray2D, hBase: Int): Int {
        var h = hBase
        for (row in COVER_START_INDEX..BOARD_SIZE step SUBSECTION_SIZE) {
            for (column in COVER_START_INDEX..BOARD_SIZE step SUBSECTION_SIZE) {
                for (n in COVER_START_INDEX..BOARD_SIZE) {
                    for (rowDelta in 0 until SUBSECTION_SIZE) {
                        for (columnDelta in 0 until SUBSECTION_SIZE) {
                            coverBoard[getIndex(row + rowDelta, column + columnDelta, n), h] = true
                        }
                    }
                    h++
                }
            }
        }
        return h
    }

    private fun checkColumnConstraint(coverBoard: BooleanArray2D, hBase: Int): Int {
        var h = hBase
        for (column in COVER_START_INDEX..BOARD_SIZE) {
            for (n in COVER_START_INDEX..BOARD_SIZE) {
                for (row in COVER_START_INDEX..BOARD_SIZE) {
                    coverBoard[getIndex(row, column, n), h] = true
                }
                h++
            }
        }
        return h
    }

    private fun checkRowConstraint(coverBoard: BooleanArray2D, hBase: Int): Int {
        var h = hBase
        for (row in COVER_START_INDEX..BOARD_SIZE) {
            for (n in COVER_START_INDEX..BOARD_SIZE) {
                for (column in COVER_START_INDEX..BOARD_SIZE) {
                    coverBoard[getIndex(row, column, n), h] = true
                }
                h++
            }
        }
        return h
    }

    private fun checkCellConstraint(coverBoard: BooleanArray2D, hBase: Int): Int {
        var h = hBase
        for (row in COVER_START_INDEX..BOARD_SIZE) {
            for (column in COVER_START_INDEX..BOARD_SIZE) {
                for (n in COVER_START_INDEX..BOARD_SIZE) {
                    coverBoard[getIndex(row, column, n), h] = true
                }
                h++
            }
        }
        return h
    }

    private fun initializeExactCoverBoard(board: Array<IntArray>): BooleanArray2D {
        val coverBoard = createExactCoverBoard()
        for (row in COVER_START_INDEX..BOARD_SIZE) {
            for (column in COVER_START_INDEX..BOARD_SIZE) {
                val n = board[row - 1][column - 1]
                if (n != NO_VALUE) {
                    for (num in MIN_VALUE..MAX_VALUE) {
                        if (num != n) {
                            coverBoard.fillRow(getIndex(row, column, num), false)
                        }
                    }
                }
            }
        }
        return coverBoard
    }
}
