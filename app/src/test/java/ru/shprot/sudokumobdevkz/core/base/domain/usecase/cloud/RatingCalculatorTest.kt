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
}
