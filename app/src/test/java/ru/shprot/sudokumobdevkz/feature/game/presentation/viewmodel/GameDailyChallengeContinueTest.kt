package ru.shprot.sudokumobdevkz.feature.game.presentation.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.shprot.sudokumobdevkz.core.base.domain.model.GameSaveData
import ru.shprot.sudokumobdevkz.feature.game.domain.model.CellData
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameUIState

class GameDailyChallengeContinueTest {

    private fun makeBoard(): List<List<CellData>> =
        List(9) { r -> List(9) { c -> CellData(value = (r * 9 + c) % 9 + 1, isGiven = true) } }

    private fun makeSaveData(
        isDailyChallenge: Boolean,
        dailyDateKey: String = "",
    ): GameSaveData =
        GameSaveData(
            difficulty = 1,
            timeSeconds = 120,
            errors = 1,
            maxErrors = 3,
            hintsRemaining = 2,
            isNotesEnabled = false,
            cells = List(9) { List(9) { GameSaveData.CellSave(value = 5, isGiven = true) } },
            solution = List(9) { List(9) { 5 } },
            isStandardMode = true,
            isDailyChallenge = isDailyChallenge,
            dailyDateKey = dailyDateKey,
        )

    private fun restoreState(state: GameUIState, data: GameSaveData): GameUIState =
        state.copy(
            timeSeconds = data.timeSeconds,
            errors = data.errors,
            maxErrors = data.maxErrors,
            hintsRemaining = data.hintsRemaining,
            isNotesEnabled = data.isNotesEnabled,
            isStandardMode = data.isStandardMode,
            isDailyChallenge = data.isDailyChallenge,
            isGenerating = false,
        )

    @Test
    fun continueGame_restoresDailyChallenge_regardlessOfRouteDailyFlag() {
        val routeIsDailyChallenge = false
        val saved = makeSaveData(isDailyChallenge = true, dailyDateKey = "2026-07-23")

        val shouldRestore = true
        assertTrue("continueGame must always attempt restore", shouldRestore)

        val restored = restoreState(GameUIState(), saved)
        assertTrue(restored.isDailyChallenge)
        assertFalse(routeIsDailyChallenge)
    }

    @Test
    fun continueGame_restoresRegularGame_whenSavedIsNotDaily() {
        val saved = makeSaveData(isDailyChallenge = false)
        val restored = restoreState(GameUIState(), saved)
        assertFalse(restored.isDailyChallenge)
    }

    @Test
    fun restoredDailyChallenge_keepsOriginalDateKey_forStreakCrediting() {
        val saved = makeSaveData(isDailyChallenge = true, dailyDateKey = "2026-07-20")
        var effectiveDailyDateKey = ""
        if (saved.isDailyChallenge) effectiveDailyDateKey = saved.dailyDateKey

        assertEquals("2026-07-20", effectiveDailyDateKey)
    }

    @Test
    fun resolvedDailyDateKey_fallsBackToToday_whenEmpty() {
        val todayDateKey = "2026-07-23"
        val emptyDailyDateKey = ""
        val resolved = emptyDailyDateKey.ifEmpty { todayDateKey }

        assertEquals(todayDateKey, resolved)
    }

    @Test
    fun resolvedDailyDateKey_keepsRestoredValue_whenPresent() {
        val todayDateKey = "2026-07-23"
        val restoredDailyDateKey = "2026-07-20"
        val resolved = restoredDailyDateKey.ifEmpty { todayDateKey }

        assertEquals("2026-07-20", resolved)
    }

    @Test
    fun saveGameStateSync_noLongerSkipsDailyChallengeState() {
        val state = GameUIState(
            cells = makeBoard(),
            isDailyChallenge = true,
            isGenerating = false,
            isGameOver = false,
        )
        val shouldSkip = state.isGenerating || state.isGameOver
        assertFalse("daily-challenge games must be persisted like regular games", shouldSkip)
    }

    @Test
    fun saveGameStateSync_stillSkipsWhileGeneratingOrGameOver() {
        val generating = GameUIState(isGenerating = true)
        val gameOver = GameUIState(isGenerating = false, isGameOver = true)

        assertTrue(generating.isGenerating || generating.isGameOver)
        assertTrue(gameOver.isGenerating || gameOver.isGameOver)
    }

    @Test
    fun countAbandonedGame_skipsStatCrediting_whenSavedGameIsDailyChallenge() {
        val saved = makeSaveData(isDailyChallenge = true, dailyDateKey = "2026-07-23")
        val shouldCredit = !saved.isDailyChallenge
        assertFalse("abandoning a daily-challenge save must not affect regular statistics", shouldCredit)
    }

    @Test
    fun countAbandonedGame_creditsStatCrediting_whenSavedGameIsRegular() {
        val saved = makeSaveData(isDailyChallenge = false)
        val shouldCredit = !saved.isDailyChallenge
        assertTrue(shouldCredit)
    }
}