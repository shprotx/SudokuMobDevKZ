package ru.shprot.sudokumobdevkz.core.base.domain.notification

import org.junit.Assert.assertEquals
import org.junit.Test

class DailyReminderRulesTest {

    @Test
    fun `skips when user already visited today`() {
        val decision = DailyReminderRules.evaluate(
            visitedToday = true,
            isDailyChallengeCompleted = false,
            currentStreak = 0,
            remainingCapSlots = 2,
        )

        assertEquals(DailyReminderRules.Decision.Skip, decision)
    }

    @Test
    fun `skips when daily challenge is already completed`() {
        val decision = DailyReminderRules.evaluate(
            visitedToday = false,
            isDailyChallengeCompleted = true,
            currentStreak = 4,
            remainingCapSlots = 2,
        )

        assertEquals(DailyReminderRules.Decision.Skip, decision)
    }

    @Test
    fun `skips when the daily notification cap has no remaining slots`() {
        val decision = DailyReminderRules.evaluate(
            visitedToday = false,
            isDailyChallengeCompleted = false,
            currentStreak = 0,
            remainingCapSlots = 0,
        )

        assertEquals(DailyReminderRules.Decision.Skip, decision)
    }

    @Test
    fun `sends with zero streak when there is no streak yet`() {
        val decision = DailyReminderRules.evaluate(
            visitedToday = false,
            isDailyChallengeCompleted = false,
            currentStreak = 0,
            remainingCapSlots = 2,
        )

        assertEquals(DailyReminderRules.Decision.Send(streak = 0), decision)
    }

    @Test
    fun `sends with the current streak when the user has an active streak`() {
        val decision = DailyReminderRules.evaluate(
            visitedToday = false,
            isDailyChallengeCompleted = false,
            currentStreak = 7,
            remainingCapSlots = 1,
        )

        assertEquals(DailyReminderRules.Decision.Send(streak = 7), decision)
    }
}
