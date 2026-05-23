package ru.shprot.sudokumobdevkz.core.base.data.cloud

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.CloudProgress
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.DailyChallengeDto
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SavedGameDto
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.StatisticDto
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.UnlockedAchievementDto

class CloudProgressSerializerTest {

    @Test
    fun `empty progress round-trip`() {
        val original = CloudProgress()
        val bytes = CloudProgressSerializer.encode(original)
        val decoded = CloudProgressSerializer.decode(bytes)
        assertEquals(original, decoded)
    }

    @Test
    fun `full progress round-trip`() {
        val original = CloudProgress(
            schemaVersion = 1,
            statistics = mapOf(
                0 to StatisticDto(allTime = 1000L, bestTime = 120, gamesStarted = 20, gamesWon = 15),
                1 to StatisticDto(allTime = 2000L, bestTime = 200, gamesStarted = 30, gamesWon = 20),
            ),
            unlockedAchievements = listOf(
                UnlockedAchievementDto(id = "wins_first", unlockedAt = 1_700_000_000L),
                UnlockedAchievementDto(id = "wins_10", unlockedAt = 1_700_000_100L),
            ),
            dailyChallenges = listOf(
                DailyChallengeDto(
                    dateKey = "2026-05-23",
                    difficultyOrdinal = 1,
                    isCompleted = true,
                    completionTimeSeconds = 300,
                    errors = 0,
                    completedAt = 1_700_000_000L,
                ),
            ),
            savedGame = SavedGameDto(
                difficulty = 0,
                timeSeconds = 60,
                errors = 1,
                maxErrors = 3,
                hintsRemaining = 2,
                isNotesEnabled = false,
                cellsJson = "[]",
                solutionJson = "[]",
                isStandardMode = true,
                timestamp = 1_700_000_500L,
            ),
            lastSyncTimestamp = 1_700_001_000L,
        )
        val bytes = CloudProgressSerializer.encode(original)
        val decoded = CloudProgressSerializer.decode(bytes)
        assertEquals(original, decoded)
    }

    @Test
    fun `decode of invalid bytes returns null`() {
        val invalid = "not a valid json {{{".toByteArray()
        assertNull(CloudProgressSerializer.decode(invalid))
    }

    @Test
    fun `decode of empty bytes returns null`() {
        assertNull(CloudProgressSerializer.decode(byteArrayOf()))
    }

    @Test
    fun `default schema version is preserved`() {
        val progress = CloudProgress()
        val bytes = CloudProgressSerializer.encode(progress)
        val decoded = CloudProgressSerializer.decode(bytes)
        assertNotNull(decoded)
        assertEquals(CloudProgress.SCHEMA_VERSION, decoded!!.schemaVersion)
    }
}
