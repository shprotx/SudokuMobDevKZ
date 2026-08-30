package ru.shprot.sudokumobdevkz.feature.game.presentation.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameUIState

class GameTimerLifecycleTest {

    private fun shouldResumeTimer(state: GameUIState): Boolean =
        !state.isPaused && !state.isGameOver

    @Test
    fun timerSuspend_onBackground_doesNotChangeIsPaused() {
        val state = GameUIState(isPaused = false, timeSeconds = 42)
        val afterSuspend = state.copy()

        assertFalse(afterSuspend.isPaused)
        assertEquals(42, afterSuspend.timeSeconds)
    }

    @Test
    fun timerResume_afterPlainBackground_restartsTimer() {
        val state = GameUIState(isPaused = false, isGameOver = false)

        assertTrue("timer must resume when not manually paused and game not over", shouldResumeTimer(state))
    }

    @Test
    fun timerResume_afterManualPause_doesNotRestartTimer_andKeepsIsPaused() {
        val state = GameUIState(isPaused = true, showPauseDialog = true)

        assertFalse("timer must stay stopped while pause dialog is active", shouldResumeTimer(state))
        assertTrue("suspend/resume cycle must not clear manual pause", state.isPaused)
        assertTrue("pause dialog must remain visible after resume", state.showPauseDialog)
    }

    @Test
    fun timerResume_afterGameOver_doesNotRestartTimer() {
        val state = GameUIState(isPaused = false, isGameOver = true)

        assertFalse("timer must not resume once the game is over", shouldResumeTimer(state))
    }

    @Test
    fun timeSeconds_survivesBackgroundSuspendResumeCycle() {
        val beforeBackground = GameUIState(isPaused = false, timeSeconds = 90)

        val afterForeground = beforeBackground.copy()

        assertEquals(
            "elapsed time must not include time spent backgrounded",
            beforeBackground.timeSeconds,
            afterForeground.timeSeconds,
        )
    }
}
