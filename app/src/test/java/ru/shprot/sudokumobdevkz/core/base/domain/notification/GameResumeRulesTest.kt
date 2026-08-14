package ru.shprot.sudokumobdevkz.core.base.domain.notification

import org.junit.Assert.assertEquals
import org.junit.Test

class GameResumeRulesTest {

    @Test
    fun `skips when there is no saved game`() {
        val decision = GameResumeRules.evaluate(
            hasSavedGame = false,
            savedGameTimestamp = null,
            alreadyNotifiedTimestamp = null,
            difficultyOrdinal = 0,
            visitedToday = false,
            remainingCapSlots = 2,
        )

        assertEquals(GameResumeRules.Decision.Skip, decision)
    }

    @Test
    fun `skips when the saved game timestamp is missing`() {
        val decision = GameResumeRules.evaluate(
            hasSavedGame = true,
            savedGameTimestamp = null,
            alreadyNotifiedTimestamp = null,
            difficultyOrdinal = 0,
            visitedToday = false,
            remainingCapSlots = 2,
        )

        assertEquals(GameResumeRules.Decision.Skip, decision)
    }

    @Test
    fun `skips when this exact saved game was already notified`() {
        val decision = GameResumeRules.evaluate(
            hasSavedGame = true,
            savedGameTimestamp = 1_000L,
            alreadyNotifiedTimestamp = 1_000L,
            difficultyOrdinal = 1,
            visitedToday = false,
            remainingCapSlots = 2,
        )

        assertEquals(GameResumeRules.Decision.Skip, decision)
    }

    @Test
    fun `postpones by twenty four hours when user already visited today`() {
        val decision = GameResumeRules.evaluate(
            hasSavedGame = true,
            savedGameTimestamp = 1_000L,
            alreadyNotifiedTimestamp = null,
            difficultyOrdinal = 1,
            visitedToday = true,
            remainingCapSlots = 2,
        )

        assertEquals(GameResumeRules.Decision.Postpone(afterHours = 24), decision)
    }

    @Test
    fun `postpones by twenty four hours when the daily cap has no remaining slots`() {
        val decision = GameResumeRules.evaluate(
            hasSavedGame = true,
            savedGameTimestamp = 1_000L,
            alreadyNotifiedTimestamp = null,
            difficultyOrdinal = 1,
            visitedToday = false,
            remainingCapSlots = 0,
        )

        assertEquals(GameResumeRules.Decision.Postpone(afterHours = 24), decision)
    }

    @Test
    fun `sends with the saved game difficulty when eligible`() {
        val decision = GameResumeRules.evaluate(
            hasSavedGame = true,
            savedGameTimestamp = 1_000L,
            alreadyNotifiedTimestamp = null,
            difficultyOrdinal = 2,
            visitedToday = false,
            remainingCapSlots = 1,
        )

        assertEquals(GameResumeRules.Decision.Send(difficultyOrdinal = 2), decision)
    }

    @Test
    fun `sends again once a newer saved game replaces the previously notified one`() {
        val decision = GameResumeRules.evaluate(
            hasSavedGame = true,
            savedGameTimestamp = 2_000L,
            alreadyNotifiedTimestamp = 1_000L,
            difficultyOrdinal = 0,
            visitedToday = false,
            remainingCapSlots = 2,
        )

        assertEquals(GameResumeRules.Decision.Send(difficultyOrdinal = 0), decision)
    }
}
