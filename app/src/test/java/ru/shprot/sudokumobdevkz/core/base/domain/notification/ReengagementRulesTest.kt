package ru.shprot.sudokumobdevkz.core.base.domain.notification

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class ReengagementRulesTest {

    private val today = LocalDate.of(2026, 8, 13)

    @Test
    fun `sends with no streak when never visited and cap is available`() {
        val decision = ReengagementRules.evaluate(
            today = today,
            lastVisitDate = null,
            currentStreak = 0,
            consecutiveSentCount = 0,
            remainingCapSlots = 2,
        )

        assertEquals(ReengagementRules.Decision.Send(streak = 0, consecutiveCount = 1, rescheduleAfterDays = 3), decision)
    }

    @Test
    fun `sends with streak included when streak is positive`() {
        val decision = ReengagementRules.evaluate(
            today = today,
            lastVisitDate = today.minusDays(3),
            currentStreak = 5,
            consecutiveSentCount = 0,
            remainingCapSlots = 2,
        )

        assertEquals(ReengagementRules.Decision.Send(streak = 5, consecutiveCount = 1, rescheduleAfterDays = 3), decision)
    }

    @Test
    fun `postpones by one day when user already visited today`() {
        val decision = ReengagementRules.evaluate(
            today = today,
            lastVisitDate = today,
            currentStreak = 0,
            consecutiveSentCount = 0,
            remainingCapSlots = 2,
        )

        assertEquals(ReengagementRules.Decision.Postpone(afterDays = 1), decision)
    }

    @Test
    fun `postpones by one day when daily cap has no remaining slots`() {
        val decision = ReengagementRules.evaluate(
            today = today,
            lastVisitDate = today.minusDays(3),
            currentStreak = 0,
            consecutiveSentCount = 0,
            remainingCapSlots = 0,
        )

        assertEquals(ReengagementRules.Decision.Postpone(afterDays = 1), decision)
    }

    @Test
    fun `stops once three consecutive pushes were already sent`() {
        val decision = ReengagementRules.evaluate(
            today = today,
            lastVisitDate = today.minusDays(3),
            currentStreak = 0,
            consecutiveSentCount = 3,
            remainingCapSlots = 2,
        )

        assertEquals(ReengagementRules.Decision.Stop, decision)
    }

    @Test
    fun `second consecutive send reschedules a third attempt`() {
        val decision = ReengagementRules.evaluate(
            today = today,
            lastVisitDate = today.minusDays(3),
            currentStreak = 0,
            consecutiveSentCount = 1,
            remainingCapSlots = 2,
        )

        assertEquals(ReengagementRules.Decision.Send(streak = 0, consecutiveCount = 2, rescheduleAfterDays = 3), decision)
    }

    @Test
    fun `third consecutive send does not reschedule a fourth attempt`() {
        val decision = ReengagementRules.evaluate(
            today = today,
            lastVisitDate = today.minusDays(3),
            currentStreak = 0,
            consecutiveSentCount = 2,
            remainingCapSlots = 2,
        )

        assertEquals(ReengagementRules.Decision.Send(streak = 0, consecutiveCount = 3, rescheduleAfterDays = null), decision)
    }

    @Test
    fun `visiting yesterday still allows sending today`() {
        val decision = ReengagementRules.evaluate(
            today = today,
            lastVisitDate = today.minusDays(1),
            currentStreak = 0,
            consecutiveSentCount = 0,
            remainingCapSlots = 2,
        )

        assertEquals(ReengagementRules.Decision.Send(streak = 0, consecutiveCount = 1, rescheduleAfterDays = 3), decision)
    }
}
