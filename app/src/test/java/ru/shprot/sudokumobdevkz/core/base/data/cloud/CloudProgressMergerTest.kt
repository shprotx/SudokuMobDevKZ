package ru.shprot.sudokumobdevkz.core.base.data.cloud

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.CloudProgress
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.DailyChallengeDto
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SavedGameDto
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.StatisticDto
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.UnlockedAchievementDto

class CloudProgressMergerTest {

    @Test
    fun `empty + empty = empty`() {
        val merged = CloudProgressMerger.merge(CloudProgress(), CloudProgress())
        assertTrue(merged.statistics.isEmpty())
        assertTrue(merged.unlockedAchievements.isEmpty())
        assertTrue(merged.dailyChallenges.isEmpty())
        assertNull(merged.savedGame)
    }

    @Test
    fun `statistics — bestTime takes minimum non-zero`() {
        val local = CloudProgress(statistics = mapOf(0 to StatisticDto(bestTime = 300)))
        val cloud = CloudProgress(statistics = mapOf(0 to StatisticDto(bestTime = 250)))
        val merged = CloudProgressMerger.merge(local, cloud)
        assertEquals(250, merged.statistics[0]?.bestTime)
    }

    @Test
    fun `statistics — bestTime falls back when one side is zero`() {
        val local = CloudProgress(statistics = mapOf(0 to StatisticDto(bestTime = 0)))
        val cloud = CloudProgress(statistics = mapOf(0 to StatisticDto(bestTime = 250)))
        val merged = CloudProgressMerger.merge(local, cloud)
        assertEquals(250, merged.statistics[0]?.bestTime)
    }

    @Test
    fun `statistics — counters take max`() {
        val local = CloudProgress(statistics = mapOf(0 to StatisticDto(gamesStarted = 20, gamesWon = 15, allTime = 1000L)))
        val cloud = CloudProgress(statistics = mapOf(0 to StatisticDto(gamesStarted = 30, gamesWon = 18, allTime = 1500L)))
        val merged = CloudProgressMerger.merge(local, cloud)
        assertEquals(30, merged.statistics[0]?.gamesStarted)
        assertEquals(18, merged.statistics[0]?.gamesWon)
        assertEquals(1500L, merged.statistics[0]?.allTime)
        assertEquals(60, merged.statistics[0]?.percentOfWins)
        assertEquals(83, merged.statistics[0]?.averageTime)
    }

    @Test
    fun `statistics — only-local difficulty is preserved`() {
        val local = CloudProgress(statistics = mapOf(0 to StatisticDto(gamesWon = 10)))
        val cloud = CloudProgress(statistics = mapOf(1 to StatisticDto(gamesWon = 5)))
        val merged = CloudProgressMerger.merge(local, cloud)
        assertEquals(10, merged.statistics[0]?.gamesWon)
        assertEquals(5, merged.statistics[1]?.gamesWon)
    }

    @Test
    fun `achievements — union by id, earliest unlockedAt wins`() {
        val local = CloudProgress(unlockedAchievements = listOf(
            UnlockedAchievementDto("a", 100L),
            UnlockedAchievementDto("b", 200L),
        ))
        val cloud = CloudProgress(unlockedAchievements = listOf(
            UnlockedAchievementDto("a", 50L),
            UnlockedAchievementDto("c", 300L),
        ))
        val merged = CloudProgressMerger.merge(local, cloud).unlockedAchievements
        assertEquals(3, merged.size)
        assertEquals(50L, merged.first { it.id == "a" }.unlockedAt)
        assertEquals(200L, merged.first { it.id == "b" }.unlockedAt)
        assertEquals(300L, merged.first { it.id == "c" }.unlockedAt)
    }

    @Test
    fun `dailies — completed wins over incomplete`() {
        val local = CloudProgress(dailyChallenges = listOf(
            DailyChallengeDto("2026-05-01", 1, isCompleted = false, completionTimeSeconds = 0, errors = 0, completedAt = 0L),
        ))
        val cloud = CloudProgress(dailyChallenges = listOf(
            DailyChallengeDto("2026-05-01", 1, isCompleted = true, completionTimeSeconds = 250, errors = 1, completedAt = 1700_000_000L),
        ))
        val merged = CloudProgressMerger.merge(local, cloud).dailyChallenges.single()
        assertTrue(merged.isCompleted)
        assertEquals(250, merged.completionTimeSeconds)
    }

    @Test
    fun `savedGame — freshest timestamp wins`() {
        val older = SavedGameDto(0, 30, 0, 3, 3, false, "[]", "[]", true, 100L)
        val newer = SavedGameDto(1, 60, 1, 3, 2, false, "[x]", "[y]", true, 200L)
        val mergedNewerOnCloud = CloudProgressMerger.merge(
            CloudProgress(savedGame = older), CloudProgress(savedGame = newer),
        )
        assertEquals(newer, mergedNewerOnCloud.savedGame)

        val mergedNewerOnLocal = CloudProgressMerger.merge(
            CloudProgress(savedGame = newer), CloudProgress(savedGame = older),
        )
        assertEquals(newer, mergedNewerOnLocal.savedGame)
    }

    @Test
    fun `savedGame — only-local is preserved`() {
        val local = CloudProgress(savedGame = SavedGameDto(0, 30, 0, 3, 3, false, "[]", "[]", true, 100L))
        val merged = CloudProgressMerger.merge(local, CloudProgress())
        assertNotNull(merged.savedGame)
        assertEquals(local.savedGame, merged.savedGame)
    }

    @Test
    fun `lastSyncTimestamp is updated to current time`() {
        val before = System.currentTimeMillis()
        val merged = CloudProgressMerger.merge(CloudProgress(), CloudProgress())
        val after = System.currentTimeMillis()
        assertTrue(merged.lastSyncTimestamp in before..after)
    }

    @Test
    fun `visitStreak — bestVisitStreak takes max`() {
        val local = CloudProgress(bestVisitStreak = 20)
        val cloud = CloudProgress(bestVisitStreak = 35)
        val merged = CloudProgressMerger.merge(local, cloud)
        assertEquals(35, merged.bestVisitStreak)
    }

    @Test
    fun `visitStreak — currentVisitStreak takes side with the later lastVisitDate`() {
        val local = CloudProgress(currentVisitStreak = 3, lastVisitDate = "2026-08-10")
        val cloud = CloudProgress(currentVisitStreak = 9, lastVisitDate = "2026-08-13")
        val merged = CloudProgressMerger.merge(local, cloud)
        assertEquals(9, merged.currentVisitStreak)
        assertEquals("2026-08-13", merged.lastVisitDate)
    }

    @Test
    fun `visitStreak — local wins when its lastVisitDate is more recent`() {
        val local = CloudProgress(currentVisitStreak = 9, lastVisitDate = "2026-08-13")
        val cloud = CloudProgress(currentVisitStreak = 3, lastVisitDate = "2026-08-10")
        val merged = CloudProgressMerger.merge(local, cloud)
        assertEquals(9, merged.currentVisitStreak)
        assertEquals("2026-08-13", merged.lastVisitDate)
    }
}
