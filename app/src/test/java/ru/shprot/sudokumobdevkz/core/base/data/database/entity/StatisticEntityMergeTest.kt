package ru.shprot.sudokumobdevkz.core.base.data.database.entity

import org.junit.Assert.assertEquals
import org.junit.Test

class StatisticEntityMergeTest {

    @Test
    fun merge_keepsLocalProgress_whenRemoteIsStale() {
        val local = StatisticEntity(
            difficulty = 1,
            allTime = 1_200L,
            bestTime = 180,
            averageTime = 240,
            gamesStarted = 12,
            gamesWon = 5,
            percentOfWins = 41,
            winsWithoutErrors = 3,
            bestWinsLine = 4,
            currentWinsLine = 2,
            casualGamesPlayed = 7,
        )
        val staleRemote = StatisticEntity(
            difficulty = 1,
            allTime = 400L,
            bestTime = 300,
            averageTime = 400,
            gamesStarted = 4,
            gamesWon = 1,
            percentOfWins = 25,
            winsWithoutErrors = 1,
            bestWinsLine = 1,
        )

        val merged = local.mergedWith(staleRemote)

        assertEquals(12, merged.gamesStarted)
        assertEquals(5, merged.gamesWon)
        assertEquals(180, merged.bestTime)
        assertEquals(3, merged.winsWithoutErrors)
        assertEquals(4, merged.bestWinsLine)
        assertEquals(7, merged.casualGamesPlayed)
        assertEquals(1_200L, merged.allTime)
    }

    @Test
    fun merge_picksUpRemoteProgress_whenRemoteIsAhead() {
        val local = StatisticEntity(difficulty = 2, gamesStarted = 2, gamesWon = 1, allTime = 300L, bestTime = 300)
        val remote = StatisticEntity(
            difficulty = 2,
            gamesStarted = 20,
            gamesWon = 15,
            allTime = 4_500L,
            bestTime = 120,
            winsWithoutErrors = 6,
            bestWinsLine = 5,
        )

        val merged = local.mergedWith(remote)

        assertEquals(20, merged.gamesStarted)
        assertEquals(15, merged.gamesWon)
        assertEquals(120, merged.bestTime)
        assertEquals(6, merged.winsWithoutErrors)
        assertEquals(5, merged.bestWinsLine)
        assertEquals(4_500L, merged.allTime)
        assertEquals(300, merged.averageTime)
    }

    @Test
    fun merge_ignoresZeroBestTime_onEitherSide() {
        val fresh = StatisticEntity(difficulty = 3, bestTime = 0)
        val withBest = StatisticEntity(difficulty = 3, bestTime = 250)

        assertEquals(250, fresh.mergedWith(withBest).bestTime)
        assertEquals(250, withBest.mergedWith(fresh).bestTime)
    }

    @Test
    fun merge_keepsLocalCurrentWinsLine() {
        val local = StatisticEntity(difficulty = 1, currentWinsLine = 0, bestWinsLine = 6)
        val remote = StatisticEntity(difficulty = 1, currentWinsLine = 6, bestWinsLine = 6)

        assertEquals(0, local.mergedWith(remote).currentWinsLine)
        assertEquals(6, local.mergedWith(remote).bestWinsLine)
    }

    @Test
    fun merge_recalculatesPercentOfWins() {
        val local = StatisticEntity(difficulty = 1, gamesStarted = 10, gamesWon = 2)
        val remote = StatisticEntity(difficulty = 1, gamesStarted = 8, gamesWon = 6)

        assertEquals(60, local.mergedWith(remote).percentOfWins)
    }

    @Test
    fun merge_isIdempotent() {
        val local = StatisticEntity(
            difficulty = 1,
            allTime = 900L,
            bestTime = 150,
            averageTime = 180,
            gamesStarted = 9,
            gamesWon = 5,
            winsWithoutErrors = 2,
            bestWinsLine = 3,
            currentWinsLine = 1,
            casualGamesPlayed = 4,
        )
        val remote = StatisticEntity(difficulty = 1, gamesStarted = 3, gamesWon = 2, bestTime = 400)

        val once = local.mergedWith(remote)
        val twice = once.mergedWith(remote)

        assertEquals(once, twice)
    }
}