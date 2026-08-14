package ru.shprot.sudokumobdevkz.core.base.domain.notification

import java.time.LocalDate

object ReengagementRules {

    const val AFTER_DAYS = 3
    const val MAX_CONSECUTIVE_SENDS = 3
    const val POSTPONE_DAYS = 1

    sealed interface Decision {
        data class Send(val streak: Int, val consecutiveCount: Int, val rescheduleAfterDays: Int?) : Decision
        data class Postpone(val afterDays: Int) : Decision
        data object Stop : Decision
    }

    fun evaluate(
        today: LocalDate,
        lastVisitDate: LocalDate?,
        currentStreak: Int,
        consecutiveSentCount: Int,
        remainingCapSlots: Int,
        higherPriorityPending: Boolean,
    ): Decision {
        if (consecutiveSentCount >= MAX_CONSECUTIVE_SENDS) return Decision.Stop
        if (lastVisitDate == today) return Decision.Postpone(POSTPONE_DAYS)
        if (remainingCapSlots <= 0 || higherPriorityPending) return Decision.Postpone(POSTPONE_DAYS)

        val nextConsecutiveCount = consecutiveSentCount + 1
        val rescheduleAfterDays = if (nextConsecutiveCount < MAX_CONSECUTIVE_SENDS) AFTER_DAYS else null
        return Decision.Send(
            streak = currentStreak,
            consecutiveCount = nextConsecutiveCount,
            rescheduleAfterDays = rescheduleAfterDays,
        )
    }
}
