package ru.shprot.sudokumobdevkz.core.base.domain.notification

object DailyReminderRules {

    sealed interface Decision {
        data class Send(val streak: Int) : Decision
        data object Skip : Decision
    }

    fun evaluate(
        visitedToday: Boolean,
        isDailyChallengeCompleted: Boolean,
        currentStreak: Int,
        remainingCapSlots: Int,
    ): Decision {
        if (!isEligibleIgnoringCap(visitedToday, isDailyChallengeCompleted, currentStreak)) return Decision.Skip
        if (remainingCapSlots <= 0) return Decision.Skip
        return Decision.Send(streak = currentStreak)
    }

    fun isEligibleIgnoringCap(
        visitedToday: Boolean,
        isDailyChallengeCompleted: Boolean,
        currentStreak: Int,
    ): Boolean {
        if (visitedToday) return false
        if (isDailyChallengeCompleted) return false
        if (currentStreak < LoyaltyGate.STREAK_THRESHOLD) return false
        return true
    }
}