package ru.shprot.sudokumobdevkz.core.base.domain.generator

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.shprot.sudokumobdevkz.core.base.domain.generator.solver.DancingLinksAlgorithm
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import kotlin.time.Duration.Companion.minutes

class UltraGeneratorTest {

    @Test
    fun `ultra puzzles are uniquely solvable for 20 random seeds`() = runTest(timeout = 10.minutes) {
        repeat(20) { i ->
            val puzzle = SudokuGenerator.generate(Difficulty.ULTRA, seed = i.toLong())
            val solutions = DancingLinksAlgorithm.countSolutions(puzzle.puzzle)
            assertEquals(
                "seed $i: expected unique solution but got $solutions",
                1,
                solutions,
            )
        }
    }

    @Test
    fun `ultra visible cell distribution and timing diagnostic for 20 seeds`() = runTest(timeout = 10.minutes) {
        val distribution = mutableMapOf<Int, Int>()
        val timings = mutableListOf<Long>()
        repeat(20) { i ->
            val start = System.currentTimeMillis()
            val puzzle = SudokuGenerator.generate(Difficulty.ULTRA, seed = i.toLong())
            val elapsed = System.currentTimeMillis() - start
            timings.add(elapsed)
            distribution[puzzle.visibleCount] = (distribution[puzzle.visibleCount] ?: 0) + 1
            println("seed $i: visible=${puzzle.visibleCount}, time=${elapsed}ms")
        }
        println("Ultra visible cell distribution: ${distribution.toSortedMap()}")
        println("Ultra timings: min=${timings.min()}ms, max=${timings.max()}ms, avg=${timings.average().toLong()}ms")
        val outOfTarget = distribution.entries
            .filter { it.key !in 17..20 }
            .sumOf { it.value }
        if (outOfTarget > 0) {
            println("WARNING: $outOfTarget/20 seeds produced visibleCount outside target 17..20.")
        }
        assertTrue(
            "All puzzles must have at least 17 visible cells (minimum for unique Sudoku is 17)",
            distribution.keys.all { it >= 17 },
        )
    }
}
