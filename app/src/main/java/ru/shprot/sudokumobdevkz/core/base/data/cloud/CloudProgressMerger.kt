package ru.shprot.sudokumobdevkz.core.base.data.cloud

import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.CloudProgress
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.DailyChallengeDto
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SavedGameDto
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.StatisticDto
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.UnlockedAchievementDto

object CloudProgressMerger {

    fun merge(local: CloudProgress, cloud: CloudProgress): CloudProgress = CloudProgress(
        schemaVersion = CloudProgress.SCHEMA_VERSION,
        statistics = mergeStatistics(local.statistics, cloud.statistics),
        unlockedAchievements = mergeAchievements(local.unlockedAchievements, cloud.unlockedAchievements),
        dailyChallenges = mergeDailies(local.dailyChallenges, cloud.dailyChallenges),
        savedGame = mergeSavedGame(local.savedGame, cloud.savedGame),
        lastSyncTimestamp = System.currentTimeMillis(),
    )

    private fun mergeStatistics(
        local: Map<Int, StatisticDto>,
        cloud: Map<Int, StatisticDto>,
    ): Map<Int, StatisticDto> =
        (local.keys + cloud.keys).associateWith { diff ->
            mergeStatistic(local[diff], cloud[diff])
        }

    private fun mergeStatistic(l: StatisticDto?, c: StatisticDto?): StatisticDto {
        if (l == null) return c!!
        if (c == null) return l
        val gamesStarted = maxOf(l.gamesStarted, c.gamesStarted)
        val gamesWon = maxOf(l.gamesWon, c.gamesWon)
        val allTime = maxOf(l.allTime, c.allTime)
        return StatisticDto(
            bestTime = minOfNonZero(l.bestTime, c.bestTime),
            bestWinsLine = maxOf(l.bestWinsLine, c.bestWinsLine),
            winsWithoutErrors = maxOf(l.winsWithoutErrors, c.winsWithoutErrors),
            gamesStarted = gamesStarted,
            gamesWon = gamesWon,
            allTime = allTime,
            averageTime = if (gamesWon > 0) (allTime / gamesWon).toInt() else 0,
            percentOfWins = if (gamesStarted > 0) (100 * gamesWon) / gamesStarted else 0,
            currentWinsLine = maxOf(l.currentWinsLine, c.currentWinsLine),
            casualGamesPlayed = maxOf(l.casualGamesPlayed, c.casualGamesPlayed),
        )
    }

    private fun mergeAchievements(
        local: List<UnlockedAchievementDto>,
        cloud: List<UnlockedAchievementDto>,
    ): List<UnlockedAchievementDto> {
        val merged = mutableMapOf<String, UnlockedAchievementDto>()
        (local + cloud).forEach { ach ->
            val existing = merged[ach.id]
            merged[ach.id] = if (existing == null || ach.unlockedAt < existing.unlockedAt) ach else existing
        }
        return merged.values.toList()
    }

    private fun mergeDailies(
        local: List<DailyChallengeDto>,
        cloud: List<DailyChallengeDto>,
    ): List<DailyChallengeDto> {
        val merged = mutableMapOf<String, DailyChallengeDto>()
        (local + cloud).forEach { daily ->
            val existing = merged[daily.dateKey]
            merged[daily.dateKey] = when {
                existing == null -> daily
                daily.isCompleted && !existing.isCompleted -> daily
                !daily.isCompleted && existing.isCompleted -> existing
                daily.completedAt >= existing.completedAt -> daily
                else -> existing
            }
        }
        return merged.values.toList()
    }

    private fun mergeSavedGame(local: SavedGameDto?, cloud: SavedGameDto?): SavedGameDto? = when {
        local == null -> cloud
        cloud == null -> local
        local.timestamp >= cloud.timestamp -> local
        else -> cloud
    }

    private fun minOfNonZero(a: Int, b: Int): Int = when {
        a <= 0 -> b
        b <= 0 -> a
        else -> minOf(a, b)
    }
}
