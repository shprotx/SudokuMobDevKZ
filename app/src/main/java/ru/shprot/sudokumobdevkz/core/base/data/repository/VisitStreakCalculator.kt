package ru.shprot.sudokumobdevkz.core.base.data.repository

import java.time.LocalDate

internal object VisitStreakCalculator {

    fun nextCurrentStreak(today: String, lastVisitDate: String?, currentStreak: Int): Int {
        if (lastVisitDate == today) return currentStreak
        val yesterday = LocalDate.parse(today).minusDays(1).toString()
        return if (lastVisitDate == yesterday) currentStreak + 1 else 1
    }

    fun mergedCurrentStreak(
        localCurrent: Int,
        localLastVisitDate: String?,
        cloudCurrent: Int,
        cloudLastVisitDate: String?,
    ): Pair<Int, String?> {
        val useCloud = cloudLastVisitDate != null &&
            (localLastVisitDate == null || cloudLastVisitDate > localLastVisitDate)
        return if (useCloud) cloudCurrent to cloudLastVisitDate else localCurrent to localLastVisitDate
    }
}