package ru.shprot.sudokumobdevkz.core.base.domain.notification

object GameResumeRules {

    const val POSTPONE_HOURS = 24

    sealed interface Decision {
        data class Send(val difficultyOrdinal: Int) : Decision
        data class Postpone(val afterHours: Int) : Decision
        data object Skip : Decision
    }

    fun evaluate(
        hasSavedGame: Boolean,
        savedGameTimestamp: Long?,
        alreadyNotifiedTimestamp: Long?,
        difficultyOrdinal: Int,
        visitedToday: Boolean,
        visitStreak: Int,
        remainingCapSlots: Int,
        higherPriorityPending: Boolean,
    ): Decision {
        if (!hasSavedGame || savedGameTimestamp == null) return Decision.Skip
        if (savedGameTimestamp == alreadyNotifiedTimestamp) return Decision.Skip
        if (visitStreak < LoyaltyGate.STREAK_THRESHOLD) return Decision.Skip
        if (visitedToday) return Decision.Postpone(POSTPONE_HOURS)
        if (remainingCapSlots <= 0 || higherPriorityPending) return Decision.Postpone(POSTPONE_HOURS)
        return Decision.Send(difficultyOrdinal = difficultyOrdinal)
    }

    fun isEligibleIgnoringCap(
        hasSavedGame: Boolean,
        savedGameTimestamp: Long?,
        alreadyNotifiedTimestamp: Long?,
        visitedToday: Boolean,
        visitStreak: Int,
    ): Boolean {
        if (!hasSavedGame || savedGameTimestamp == null) return false
        if (savedGameTimestamp == alreadyNotifiedTimestamp) return false
        if (visitStreak < LoyaltyGate.STREAK_THRESHOLD) return false
        if (visitedToday) return false
        return true
    }
}