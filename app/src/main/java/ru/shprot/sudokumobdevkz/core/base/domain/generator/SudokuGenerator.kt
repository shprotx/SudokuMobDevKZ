package ru.shprot.sudokumobdevkz.core.base.domain.generator

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.shprot.sudokumobdevkz.core.base.domain.generator.solver.DancingLinksAlgorithm
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import kotlin.random.Random

data class GeneratedPuzzle(
    val solution: Array<IntArray>,
    val puzzle: Array<IntArray>,
    val visibleCount: Int,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GeneratedPuzzle) return false
        return solution.contentDeepEquals(other.solution) &&
                puzzle.contentDeepEquals(other.puzzle) &&
                visibleCount == other.visibleCount
    }

    override fun hashCode(): Int {
        var result = solution.contentDeepHashCode()
        result = 31 * result + puzzle.contentDeepHashCode()
        result = 31 * result + visibleCount
        return result
    }
}

object SudokuGenerator {

    private const val SIZE = 9
    private const val CELLS = 81

    suspend fun generate(difficulty: Difficulty): GeneratedPuzzle =
        generateInternal(difficulty, Random.Default)

    suspend fun generate(difficulty: Difficulty, seed: Long): GeneratedPuzzle =
        generateInternal(difficulty, Random(seed))

    private suspend fun generateInternal(
        difficulty: Difficulty,
        random: Random,
    ): GeneratedPuzzle = withContext(Dispatchers.Default) {
        val grid = Array(SIZE) { IntArray(SIZE) }
        val available = Array(CELLS) { (1..9).toMutableList() }

        generateGrid(grid, available, random)

        val solution = Array(SIZE) { row -> grid[row].copyOf() }

        val emptyCells = dig(grid, difficulty, random)

        val visibleCount = CELLS - emptyCells

        GeneratedPuzzle(
            solution = solution,
            puzzle = Array(SIZE) { row -> grid[row].copyOf() },
            visibleCount = visibleCount,
        )
    }

    private fun generateGrid(
        grid: Array<IntArray>,
        available: Array<MutableList<Int>>,
        random: Random,
    ) {
        var count = 0
        while (count < CELLS) {
            if (available[count].isNotEmpty()) {
                val randomIndex = random.nextInt(available[count].size)
                val value = available[count][randomIndex]
                val row = count / SIZE
                val col = count % SIZE

                if (!hasConflict(grid, row, col, value, count)) {
                    grid[row][col] = value
                    available[count].removeAt(randomIndex)
                    count++
                } else {
                    available[count].removeAt(randomIndex)
                }
            } else {
                available[count] = (1..9).toMutableList()
                count--
                grid[count / SIZE][count % SIZE] = 0
            }
        }
    }

    private fun hasConflict(
        grid: Array<IntArray>,
        row: Int,
        col: Int,
        value: Int,
        filledCount: Int,
    ): Boolean {
        for (i in 0 until filledCount) {
            val r = i / SIZE
            val c = i % SIZE
            if (grid[r][c] == value) {
                if (r == row || c == col) return true
                if (r / 3 == row / 3 && c / 3 == col / 3) return true
            }
        }
        return false
    }

    private fun dig(grid: Array<IntArray>, difficulty: Difficulty, random: Random): Int {
        val zeros = mutableListOf<Int>()
        var emptyCells = digForExpert(grid, difficulty, zeros, random)

        if (difficulty != Difficulty.HARD) {
            emptyCells = openSomeCells(grid, difficulty, emptyCells, zeros, random)
        }

        return emptyCells
    }

    private fun digForExpert(
        grid: Array<IntArray>,
        difficulty: Difficulty,
        zeros: MutableList<Int>,
        random: Random,
    ): Int {
        val positions = (0 until CELLS).toMutableList().apply { shuffle(random) }
        var emptyCells = 0

        for (pos in positions) {
            val row = pos / SIZE
            val col = pos % SIZE
            val temp = grid[row][col]
            grid[row][col] = 0

            val solutions = DancingLinksAlgorithm.countSolutions(grid)

            if (solutions != 1) {
                grid[row][col] = temp
            } else {
                emptyCells++
                if (difficulty != Difficulty.HARD) zeros.add(pos)
            }
        }

        return emptyCells
    }

    private fun openSomeCells(
        grid: Array<IntArray>,
        difficulty: Difficulty,
        emptyCells: Int,
        zeros: MutableList<Int>,
        random: Random,
    ): Int {
        zeros.shuffle(random)
        val targetVisible = difficulty.visibleCells
        var haveToOpen = targetVisible - (CELLS - emptyCells)
        var currentEmpty = emptyCells
        var idx = zeros.size - 1

        while (haveToOpen > 0 && idx >= 0) {
            val pos = zeros[idx]
            val row = pos / SIZE
            val col = pos % SIZE

            if (grid[row][col] == 0) {
                for (v in 1..9) {
                    grid[row][col] = v
                    if (DancingLinksAlgorithm.countSolutions(grid) == 1) break
                }
                haveToOpen--
                currentEmpty--
            }

            idx--
        }

        return currentEmpty
    }
}