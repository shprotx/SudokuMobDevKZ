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

        val targetVisible = random.nextInt(difficulty.visibleCells.first, difficulty.visibleCells.last + 1)
        val emptyCells = dig(grid, solution, difficulty, targetVisible, random)

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

    private fun dig(
        grid: Array<IntArray>,
        solution: Array<IntArray>,
        difficulty: Difficulty,
        targetVisible: Int,
        random: Random,
    ): Int =
        when (difficulty) {
            Difficulty.EASY, Difficulty.MEDIUM -> {
                val zeros = mutableListOf<Int>()
                var emptyCells = digForExpert(grid, difficulty, zeros, random)
                emptyCells = openSomeCells(grid, targetVisible, emptyCells, zeros, random)
                emptyCells
            }
            Difficulty.HARD, Difficulty.ULTRA ->
                digAggressive(grid, solution, difficulty, targetVisible, random)
        }

    private fun digAggressive(
        grid: Array<IntArray>,
        solution: Array<IntArray>,
        difficulty: Difficulty,
        targetVisible: Int,
        random: Random,
    ): Int {
        val maxAttempts = if (difficulty == Difficulty.ULTRA) 100 else 30
        var bestEmptyCells = digForExpert(grid, difficulty, mutableListOf(), random)
        var bestPuzzle = Array(SIZE) { row -> grid[row].copyOf() }
        var attempts = 1

        while (attempts < maxAttempts && (CELLS - bestEmptyCells) > targetVisible) {
            for (r in 0 until SIZE) {
                for (c in 0 until SIZE) {
                    grid[r][c] = solution[r][c]
                }
            }
            val attemptEmpty = digForExpert(grid, difficulty, mutableListOf(), random)
            if (attemptEmpty > bestEmptyCells) {
                bestEmptyCells = attemptEmpty
                bestPuzzle = Array(SIZE) { row -> grid[row].copyOf() }
            }
            attempts++
        }

        for (r in 0 until SIZE) {
            for (c in 0 until SIZE) {
                grid[r][c] = bestPuzzle[r][c]
            }
        }
        return bestEmptyCells
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
        targetVisible: Int,
        emptyCells: Int,
        zeros: MutableList<Int>,
        random: Random,
    ): Int {
        zeros.shuffle(random)
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