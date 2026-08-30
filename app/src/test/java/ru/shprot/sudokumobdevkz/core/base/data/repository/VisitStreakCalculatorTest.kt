package ru.shprot.sudokumobdevkz.core.base.data.repository

import org.junit.Assert.assertEquals
import org.junit.Test

class VisitStreakCalculatorTest {

    @Test
    fun `nextCurrentStreak starts at 1 on first ever visit`() {
        assertEquals(
            1,
            VisitStreakCalculator.nextCurrentStreak(
                today = "2026-08-13",
                lastVisitDate = null,
                currentStreak = 0,
            ),
        )
    }

    @Test
    fun `nextCurrentStreak increments when last visit was yesterday`() {
        assertEquals(
            6,
            VisitStreakCalculator.nextCurrentStreak(
                today = "2026-08-13",
                lastVisitDate = "2026-08-12",
                currentStreak = 5,
            ),
        )
    }

    @Test
    fun `nextCurrentStreak resets to 1 after a gap`() {
        assertEquals(
            1,
            VisitStreakCalculator.nextCurrentStreak(
                today = "2026-08-13",
                lastVisitDate = "2026-08-10",
                currentStreak = 7,
            ),
        )
    }

    @Test
    fun `nextCurrentStreak is unchanged when already visited today`() {
        assertEquals(
            5,
            VisitStreakCalculator.nextCurrentStreak(
                today = "2026-08-13",
                lastVisitDate = "2026-08-13",
                currentStreak = 5,
            ),
        )
    }

    @Test
    fun `nextCurrentStreak crosses a month boundary correctly`() {
        assertEquals(
            4,
            VisitStreakCalculator.nextCurrentStreak(
                today = "2026-09-01",
                lastVisitDate = "2026-08-31",
                currentStreak = 3,
            ),
        )
    }

    @Test
    fun `mergedCurrentStreak keeps local when local lastVisitDate is later`() {
        val (current, lastVisitDate) = VisitStreakCalculator.mergedCurrentStreak(
            localCurrent = 9,
            localLastVisitDate = "2026-08-13",
            cloudCurrent = 3,
            cloudLastVisitDate = "2026-08-10",
        )
        assertEquals(9, current)
        assertEquals("2026-08-13", lastVisitDate)
    }

    @Test
    fun `mergedCurrentStreak takes cloud when cloud lastVisitDate is later`() {
        val (current, lastVisitDate) = VisitStreakCalculator.mergedCurrentStreak(
            localCurrent = 3,
            localLastVisitDate = "2026-08-10",
            cloudCurrent = 9,
            cloudLastVisitDate = "2026-08-13",
        )
        assertEquals(9, current)
        assertEquals("2026-08-13", lastVisitDate)
    }

    @Test
    fun `mergedCurrentStreak keeps local when cloud has never visited`() {
        val (current, lastVisitDate) = VisitStreakCalculator.mergedCurrentStreak(
            localCurrent = 4,
            localLastVisitDate = "2026-08-13",
            cloudCurrent = 0,
            cloudLastVisitDate = null,
        )
        assertEquals(4, current)
        assertEquals("2026-08-13", lastVisitDate)
    }

    @Test
    fun `mergedCurrentStreak takes cloud when local has never visited`() {
        val (current, lastVisitDate) = VisitStreakCalculator.mergedCurrentStreak(
            localCurrent = 0,
            localLastVisitDate = null,
            cloudCurrent = 4,
            cloudLastVisitDate = "2026-08-13",
        )
        assertEquals(4, current)
        assertEquals("2026-08-13", lastVisitDate)
    }

    @Test
    fun `mergedCurrentStreak keeps continuity when both visited today after device switch`() {
        val (current, lastVisitDate) = VisitStreakCalculator.mergedCurrentStreak(
            localCurrent = 1,
            localLastVisitDate = "2026-08-13",
            cloudCurrent = 300,
            cloudLastVisitDate = "2026-08-13",
        )
        assertEquals(300, current)
        assertEquals("2026-08-13", lastVisitDate)
    }

    @Test
    fun `mergedCurrentStreak extends streak when cloud chain ends the day before local`() {
        val (current, lastVisitDate) = VisitStreakCalculator.mergedCurrentStreak(
            localCurrent = 1,
            localLastVisitDate = "2026-08-13",
            cloudCurrent = 300,
            cloudLastVisitDate = "2026-08-12",
        )
        assertEquals(301, current)
        assertEquals("2026-08-13", lastVisitDate)
    }
}