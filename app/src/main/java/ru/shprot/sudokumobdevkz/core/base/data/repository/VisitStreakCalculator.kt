package ru.shprot.sudokumobdevkz.core.base.data.repository

import java.time.LocalDate
import java.time.temporal.ChronoUnit

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
        if (localLastVisitDate == null) return cloudCurrent to cloudLastVisitDate
        if (cloudLastVisitDate == null) return localCurrent to localLastVisitDate

        val localLast = LocalDate.parse(localLastVisitDate)
        val cloudLast = LocalDate.parse(cloudLastVisitDate)
        val localStart = localLast.minusDays((localCurrent - 1).toLong())
        val cloudStart = cloudLast.minusDays((cloudCurrent - 1).toLong())

        val chainsConnect = maxOf(localStart, cloudStart) <= minOf(localLast, cloudLast).plusDays(1)
        if (!chainsConnect) {
            return if (localLast >= cloudLast) localCurrent to localLastVisitDate else cloudCurrent to cloudLastVisitDate
        }

        val mergedStart = minOf(localStart, cloudStart)
        val mergedLast = maxOf(localLast, cloudLast)
        val mergedCurrent = ChronoUnit.DAYS.between(mergedStart, mergedLast).toInt() + 1
        return mergedCurrent to mergedLast.toString()
    }
}