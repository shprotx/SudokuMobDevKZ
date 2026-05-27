package ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty

class RatingCalculatorTest {

    @Test
    fun `zero time returns zero`() {
        assertEquals(0L, RatingCalculator.scoreForWin(Difficulty.EASY, 0, 0))
    }

    @Test
    fun `easy at exactly target with errors`() {
        // 100 base × 1.0 speed × 1.0 clean = 100
        assertEquals(100L, RatingCalculator.scoreForWin(Difficulty.EASY, 300, 1))
    }

    @Test
    fun `easy at exactly target without errors gets clean bonus`() {
        // 100 × 1.0 × 1.3 = 130
        assertEquals(130L, RatingCalculator.scoreForWin(Difficulty.EASY, 300, 0))
    }

    @Test
    fun `hard at exactly target without errors`() {
        // 500 × 1.0 × 1.3 = 650
        assertEquals(650L, RatingCalculator.scoreForWin(Difficulty.HARD, 1200, 0))
    }

    @Test
    fun `speed multiplier capped at 2x for super fast solves`() {
        // 1 second easy: 300 / 1 = 300, capped to 2.0
        // 100 × 2.0 × 1.3 = 260
        assertEquals(260L, RatingCalculator.scoreForWin(Difficulty.EASY, 1, 0))
    }

    @Test
    fun `speed multiplier floored at 0_5x for slow solves`() {
        // 10000 seconds easy: 300 / 10000 → floored to 0.5
        // 100 × 0.5 × 1.3 = 65
        assertEquals(65L, RatingCalculator.scoreForWin(Difficulty.EASY, 10000, 0))
    }

    @Test
    fun `hard is significantly bigger than easy at same relative speed`() {
        val easy = RatingCalculator.scoreForWin(Difficulty.EASY, 300, 0)
        val hard = RatingCalculator.scoreForWin(Difficulty.HARD, 1200, 0)
        assertEquals(5L, hard / easy)
    }

    @Test
    fun `one hint applies 10 percent penalty`() {
        // 500 × 1.0 × 1.3 × 0.9 = 585
        assertEquals(585L, RatingCalculator.scoreForWin(Difficulty.HARD, 1200, 0, hintsUsed = 1))
    }

    @Test
    fun `three hints apply 30 percent penalty`() {
        // 500 × 1.0 × 1.3 × 0.7 = 455
        assertEquals(455L, RatingCalculator.scoreForWin(Difficulty.HARD, 1200, 0, hintsUsed = 3))
    }

    @Test
    fun `hints multiplier floored at 0_3`() {
        // 10 hints would be -100%, capped at 0.3
        // 500 × 1.0 × 1.3 × 0.3 = 195
        assertEquals(195L, RatingCalculator.scoreForWin(Difficulty.HARD, 1200, 0, hintsUsed = 10))
    }

    @Test
    fun `daily applies 1_5 multiplier`() {
        // 500 × 1.0 × 1.3 × 1.0 × 1.5 = 975
        assertEquals(
            975L,
            RatingCalculator.scoreForWin(Difficulty.HARD, 1200, 0, hintsUsed = 0, isDaily = true),
        )
    }

    @Test
    fun `daily with hints and errors stacks correctly`() {
        // 250 × 1.0 × 1.0 × 0.8 × 1.5 = 300
        assertEquals(
            300L,
            RatingCalculator.scoreForWin(Difficulty.MEDIUM, 600, 1, hintsUsed = 2, isDaily = true),
        )
    }

    @Test
    fun `ultra base is 1000`() {
        assertEquals(1000, RatingCalculator.base(Difficulty.ULTRA))
    }

    @Test
    fun `ultra target time is 2400 seconds`() {
        assertEquals(2400, RatingCalculator.targetTimeSeconds(Difficulty.ULTRA))
    }

    @Test
    fun `ultra at exactly target without errors`() {
        // 1000 × 1.0 × 1.3 = 1300
        assertEquals(1300L, RatingCalculator.scoreForWin(Difficulty.ULTRA, 2400, 0))
    }

    @Test
    fun `ultra at exactly target with errors`() {
        // 1000 × 1.0 × 1.0 = 1000
        assertEquals(1000L, RatingCalculator.scoreForWin(Difficulty.ULTRA, 2400, 1))
    }

    @Test
    fun `ultra is double hard base`() {
        assertEquals(
            2 * RatingCalculator.base(Difficulty.HARD),
            RatingCalculator.base(Difficulty.ULTRA),
        )
    }

    @Test
    fun `ultra is double hard target time`() {
        assertEquals(
            2 * RatingCalculator.targetTimeSeconds(Difficulty.HARD),
            RatingCalculator.targetTimeSeconds(Difficulty.ULTRA),
        )
    }
}
