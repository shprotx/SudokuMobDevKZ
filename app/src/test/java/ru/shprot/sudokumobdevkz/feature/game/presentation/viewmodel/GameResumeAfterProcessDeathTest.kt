package ru.shprot.sudokumobdevkz.feature.game.presentation.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GameResumeAfterProcessDeathTest {

    @Test
    fun restoresSavedGame_whenScreenWasRecreatedAfterProcessDeath() {
        val restores = GameViewModel.shouldRestoreSavedGame(
            continueGame = false,
            gameAlreadyStarted = true,
        )

        assertTrue("recreated game screen must restore the save, not generate a new board", restores)
    }

    @Test
    fun restoresSavedGame_whenOpenedViaContinue() {
        assertTrue(GameViewModel.shouldRestoreSavedGame(continueGame = true, gameAlreadyStarted = false))
    }

    @Test
    fun generatesNewGame_whenScreenOpenedFreshFromMenu() {
        assertFalse(GameViewModel.shouldRestoreSavedGame(continueGame = false, gameAlreadyStarted = false))
    }

    @Test
    fun abandonedHintsUsed_countsSpentHints() {
        assertEquals(2, GameViewModel.abandonedHintsUsed(hintsRemaining = 1))
        assertEquals(0, GameViewModel.abandonedHintsUsed(hintsRemaining = GameViewModel.HINTS_INITIAL))
    }

    @Test
    fun abandonedHintsUsed_isZero_forUnlimitedHints() {
        assertEquals(0, GameViewModel.abandonedHintsUsed(hintsRemaining = Int.MAX_VALUE))
    }

    @Test
    fun abandonedHintsUsed_neverNegative() {
        assertEquals(0, GameViewModel.abandonedHintsUsed(hintsRemaining = 99))
    }
}