package ru.shprot.sudokumobdevkz.core.base.domain.notification

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DailyReminderRulesTest {

    @Test
    fun `skips when user already visited today`() {
        val decision = DailyReminderRules.evaluate(
            visitedToday = true,
            isDailyChallengeCompleted = false,
            currentStreak = 25,
            remainingCapSlots = 1,
        )

        assertEquals(DailyReminderRules.Decision.Skip, decision)
    }

    @Test
    fun `skips when daily challenge is already completed`() {
        val decision = DailyReminderRules.evaluate(
            visitedToday = false,
            isDailyChallengeCompleted = true,
            currentStreak = 25,
            remainingCapSlots = 1,
        )

        assertEquals(DailyReminderRules.Decision.Skip, decision)
    }

    @Test
    fun `skips when the daily notification cap has no remaining slots`() {
        val decision = DailyReminderRules.evaluate(
            visitedToday = false,
            isDailyChallengeCompleted = false,
            currentStreak = 25,
            remainingCapSlots = 0,
        )

        assertEquals(DailyReminderRules.Decision.Skip, decision)
    }

    @Test
    fun `skips when the daily streak is below the loyalty threshold`() {
        val decision = DailyReminderRules.evaluate(
            visitedToday = false,
            isDailyChallengeCompleted = false,
            currentStreak = 19,
            remainingCapSlots = 1,
        )

        assertEquals(DailyReminderRules.Decision.Skip, decision)
    }

    @Test
    fun `sends when the daily streak matches the loyalty threshold exactly`() {
        val decision = DailyReminderRules.evaluate(
            visitedToday = false,
            isDailyChallengeCompleted = false,
            currentStreak = 20,
            remainingCapSlots = 1,
        )

        assertEquals(DailyReminderRules.Decision.Send(streak = 20), decision)
    }

    @Test
    fun `sends with the current streak when the user has a loyal streak`() {
        val decision = DailyReminderRules.evaluate(
            visitedToday = false,
            isDailyChallengeCompleted = false,
            currentStreak = 30,
            remainingCapSlots = 1,
        )

        assertEquals(DailyReminderRules.Decision.Send(streak = 30), decision)
    }

    @Test
    fun `isEligibleIgnoringCap is false below the loyalty threshold`() {
        assertFalse(
            DailyReminderRules.isEligibleIgnoringCap(
                visitedToday = false,
                isDailyChallengeCompleted = false,
                currentStreak = 5,
            )
        )
    }

    @Test
    fun `isEligibleIgnoringCap is true for a loyal user who has not visited today`() {
        assertTrue(
            DailyReminderRules.isEligibleIgnoringCap(
                visitedToday = false,
                isDailyChallengeCompleted = false,
                currentStreak = 20,
            )
        )
    }
}