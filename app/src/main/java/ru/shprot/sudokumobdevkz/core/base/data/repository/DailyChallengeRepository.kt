package ru.shprot.sudokumobdevkz.core.base.data.repository

import ru.shprot.sudokumobdevkz.core.base.data.database.dao.DailyChallengeDao
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.DailyChallengeEntity
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DailyChallengeRepository @Inject constructor(
    private val dao: DailyChallengeDao,
) {

    fun todayDateKey(): String = LocalDate.now().toString()

    fun dailySeed(dateKey: String): Long = dateKey.replace("-", "").toLong()

    fun difficultyForDate(dateKey: String): Difficulty {
        val dayOfYear = LocalDate.parse(dateKey).dayOfYear
        return Difficulty.entries[dayOfYear % Difficulty.entries.size]
    }

    suspend fun getTodayChallenge(): DailyChallengeEntity {
        val dateKey = todayDateKey()
        return dao.getByDate(dateKey) ?: DailyChallengeEntity(
            dateKey = dateKey,
            difficultyOrdinal = difficultyForDate(dateKey).ordinal,
            isCompleted = false,
            completionTimeSeconds = 0,
            errors = 0,
            completedAt = 0L,
        )
    }

    suspend fun markCompleted(dateKey: String, timeSeconds: Int, errors: Int): Int {
        dao.upsert(
            DailyChallengeEntity(
                dateKey = dateKey,
                difficultyOrdinal = difficultyForDate(dateKey).ordinal,
                isCompleted = true,
                completionTimeSeconds = timeSeconds,
                errors = errors,
                completedAt = System.currentTimeMillis(),
            )
        )
        return getCurrentStreak()
    }

    suspend fun getCurrentStreak(): Int {
        var streak = 0
        val today = LocalDate.now()
        val todayEntity = dao.getByDate(today.toString())
        if (todayEntity?.isCompleted == true) streak++

        var date = today.minusDays(1)
        while (true) {
            val entity = dao.getByDate(date.toString())
            if (entity?.isCompleted == true) {
                streak++
                date = date.minusDays(1)
            } else {
                break
            }
        }
        return streak
    }

    suspend fun getLongestStreak(): Int {
        val completed = dao.getAllCompletedAsc()
        if (completed.isEmpty()) return 0

        var longest = 0
        var current = 0
        var prevDate: LocalDate? = null
        for (entity in completed) {
            val date = LocalDate.parse(entity.dateKey)
            current = if (prevDate != null && date == prevDate.plusDays(1)) current + 1 else 1
            if (current > longest) longest = current
            prevDate = date
        }
        return longest
    }
}