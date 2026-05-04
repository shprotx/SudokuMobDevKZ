package ru.shprot.sudokumobdevkz.feature.game.presentation.viewmodel

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import ru.shprot.sudokumobdevkz.core.base.data.repository.AppSettings
import ru.shprot.sudokumobdevkz.core.base.data.repository.GameSaveData
import ru.shprot.sudokumobdevkz.core.base.data.repository.SettingsRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.SudokuRepository
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameUIEffect
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameUIEvent
import ru.shprot.sudokumobdevkz.testutil.MainDispatcherRule
import ru.shprot.sudokumobdevkz.testutil.createGameSavedStateHandle

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class GameViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: SudokuRepository = mockk(relaxed = true)
    private val settingsRepository: SettingsRepository = mockk(relaxed = true)

    @Before
    fun setup() {
        every { settingsRepository.currentSettings } returns AppSettings()
        coEvery { repository.loadSavedGame() } returns null
        coEvery { repository.hasSavedGame() } returns false
    }

    private fun createViewModel(
        difficultyOrdinal: Int = 0,
        continueGame: Boolean = false,
        settings: AppSettings = AppSettings(),
    ): GameViewModel {
        every { settingsRepository.currentSettings } returns settings
        return GameViewModel(
            savedStateHandle = createGameSavedStateHandle(difficultyOrdinal, continueGame),
            repository = repository,
            settingsRepository = settingsRepository,
        )
    }

    @Test
    fun `standard mode game generates puzzle and starts timer`() = runTest(mainDispatcherRule.testDispatcher) {
        val vm = createViewModel()
        advanceUntilIdle()

        val state = vm.uiState.value
        assertFalse(state.isGenerating)
        assertTrue(state.isStandardMode)
        assertEquals(Difficulty.EASY, state.difficulty)
        assertTrue(state.cells.flatten().any { it.isGiven })
    }

    @Test
    fun `standard mode win updates full statistics`() = runTest(mainDispatcherRule.testDispatcher) {
        val vm = createViewModel()
        advanceUntilIdle()

        val state = vm.uiState.value
        val solution = state.solution

        for (r in 0 until 9) {
            for (c in 0 until 9) {
                if (!state.cells[r][c].isGiven) {
                    vm.setEvent(GameUIEvent.CellClicked(r, c))
                    vm.setEvent(GameUIEvent.NumberClicked(solution[r][c]))
                    advanceUntilIdle()
                }
            }
        }
        advanceUntilIdle()

        assertTrue(vm.uiState.value.isGameOver)
        assertTrue(vm.uiState.value.isWin)
        coVerify { repository.updateStatistic(Difficulty.EASY, true, any(), any()) }
        coVerify { repository.deleteSavedGame() }
    }

    @Test
    fun `standard mode loss on 3 errors updates statistics`() = runTest(mainDispatcherRule.testDispatcher) {
        val vm = createViewModel()
        advanceUntilIdle()

        val state = vm.uiState.value
        var errorsGiven = 0

        for (r in 0 until 9) {
            if (errorsGiven >= 3) break
            for (c in 0 until 9) {
                if (errorsGiven >= 3) break
                val cell = state.cells[r][c]
                if (cell.isGiven || cell.value != 0) continue
                val correct = state.solution[r][c]
                val wrong = (1..9).first { it != correct }
                vm.setEvent(GameUIEvent.CellClicked(r, c))
                vm.setEvent(GameUIEvent.NumberClicked(wrong))
                advanceUntilIdle()
                errorsGiven++
            }
        }
        advanceUntilIdle()

        assertTrue(vm.uiState.value.isGameOver)
        assertFalse(vm.uiState.value.isWin)
        coVerify { repository.updateStatistic(Difficulty.EASY, false, any(), any()) }
    }

    @Test
    fun `casual mode with unlimited hints increments casual counter`() = runTest(mainDispatcherRule.testDispatcher) {
        val settings = AppSettings(unlimitedHints = true)
        val vm = createViewModel(settings = settings)
        advanceUntilIdle()

        assertFalse(vm.uiState.value.isStandardMode)
        assertEquals(Int.MAX_VALUE, vm.uiState.value.hintsRemaining)

        val state = vm.uiState.value
        val solution = state.solution
        for (r in 0 until 9) {
            for (c in 0 until 9) {
                if (!state.cells[r][c].isGiven) {
                    vm.setEvent(GameUIEvent.CellClicked(r, c))
                    vm.setEvent(GameUIEvent.NumberClicked(solution[r][c]))
                    advanceUntilIdle()
                }
            }
        }
        advanceUntilIdle()

        assertTrue(vm.uiState.value.isGameOver)
        coVerify { repository.incrementCasualGames(Difficulty.EASY) }
        coVerify(exactly = 0) { repository.updateStatistic(any(), any(), any(), any()) }
    }

    @Test
    fun `casual mode with unlimited errors does not count errors`() = runTest(mainDispatcherRule.testDispatcher) {
        val settings = AppSettings(unlimitedErrors = true)
        val vm = createViewModel(settings = settings)
        advanceUntilIdle()

        assertFalse(vm.uiState.value.isStandardMode)
        assertEquals(Int.MAX_VALUE, vm.uiState.value.maxErrors)
    }

    @Test
    fun `checkErrors false does not mark wrong numbers as errors`() = runTest(mainDispatcherRule.testDispatcher) {
        val settings = AppSettings(checkErrors = false)
        val vm = createViewModel(settings = settings)
        advanceUntilIdle()

        val state = vm.uiState.value
        val emptyCell = (0 until 9).flatMap { r -> (0 until 9).map { c -> r to c } }
            .first { (r, c) -> !state.cells[r][c].isGiven }
        val (r, c) = emptyCell
        val correct = state.solution[r][c]
        val wrong = (1..9).first { it != correct }

        vm.setEvent(GameUIEvent.CellClicked(r, c))
        vm.setEvent(GameUIEvent.NumberClicked(wrong))
        advanceUntilIdle()

        val updatedCell = vm.uiState.value.cells[r][c]
        assertEquals(wrong, updatedCell.value)
        assertFalse(updatedCell.isError)
        assertEquals(0, vm.uiState.value.errors)
    }

    @Test
    fun `cannot overwrite correctly placed value`() = runTest(mainDispatcherRule.testDispatcher) {
        val vm = createViewModel()
        advanceUntilIdle()

        val state = vm.uiState.value
        val emptyCell = (0 until 9).flatMap { r -> (0 until 9).map { c -> r to c } }
            .first { (r, c) -> !state.cells[r][c].isGiven }
        val (r, c) = emptyCell
        val correct = state.solution[r][c]

        vm.setEvent(GameUIEvent.CellClicked(r, c))
        vm.setEvent(GameUIEvent.NumberClicked(correct))
        advanceUntilIdle()

        val other = (1..9).first { it != correct }
        vm.setEvent(GameUIEvent.CellClicked(r, c))
        vm.setEvent(GameUIEvent.NumberClicked(other))
        advanceUntilIdle()

        assertEquals(correct, vm.uiState.value.cells[r][c].value)
    }

    @Test
    fun `abandoned game counts as loss`() = runTest(mainDispatcherRule.testDispatcher) {
        val savedGame = GameSaveData(
            difficulty = Difficulty.EASY.firebaseKey,
            timeSeconds = 120,
            errors = 1,
            maxErrors = 3,
            hintsRemaining = 2,
            isNotesEnabled = false,
            isStandardMode = true,
            cells = List(9) { List(9) { GameSaveData.CellSave() } },
            solution = List(9) { List(9) { 0 } },
        )
        coEvery { repository.loadSavedGame() } returns savedGame

        val vm = createViewModel()
        advanceUntilIdle()

        coVerify { repository.updateStatistic(Difficulty.EASY, false, 120, 1) }
        coVerify { repository.deleteSavedGame() }
    }

    @Test
    fun `abandoned casual game increments casual counter`() = runTest(mainDispatcherRule.testDispatcher) {
        val savedGame = GameSaveData(
            difficulty = Difficulty.MEDIUM.firebaseKey,
            timeSeconds = 60,
            errors = 0,
            maxErrors = Int.MAX_VALUE,
            hintsRemaining = Int.MAX_VALUE,
            isNotesEnabled = false,
            isStandardMode = false,
            cells = List(9) { List(9) { GameSaveData.CellSave() } },
            solution = List(9) { List(9) { 0 } },
        )
        coEvery { repository.loadSavedGame() } returns savedGame

        val vm = createViewModel()
        advanceUntilIdle()

        coVerify { repository.incrementCasualGames(Difficulty.MEDIUM) }
    }

    @Test
    fun `continue game restores saved state`() = runTest(mainDispatcherRule.testDispatcher) {
        val savedGame = GameSaveData(
            difficulty = Difficulty.HARD.firebaseKey,
            timeSeconds = 300,
            errors = 2,
            maxErrors = 3,
            hintsRemaining = 1,
            isNotesEnabled = true,
            isStandardMode = true,
            cells = List(9) { r -> List(9) { c -> GameSaveData.CellSave(value = if (r == 0 && c == 0) 5 else 0) } },
            solution = List(9) { List(9) { 1 } },
        )
        coEvery { repository.loadSavedGame() } returns savedGame

        val vm = createViewModel(difficultyOrdinal = 2, continueGame = true)
        advanceUntilIdle()

        val state = vm.uiState.value
        assertFalse(state.isGenerating)
        assertEquals(Difficulty.HARD, state.difficulty)
        assertEquals(300, state.timeSeconds)
        assertEquals(2, state.errors)
        assertEquals(1, state.hintsRemaining)
        assertTrue(state.isNotesEnabled)
        assertEquals(5, state.cells[0][0].value)
    }

    @Test
    fun `game over deletes saved game`() = runTest(mainDispatcherRule.testDispatcher) {
        val vm = createViewModel()
        advanceUntilIdle()

        val state = vm.uiState.value
        val solution = state.solution
        for (r in 0 until 9) {
            for (c in 0 until 9) {
                if (!vm.uiState.value.cells[r][c].isGiven && vm.uiState.value.cells[r][c].value == 0) {
                    vm.setEvent(GameUIEvent.CellClicked(r, c))
                    vm.setEvent(GameUIEvent.NumberClicked(solution[r][c]))
                    advanceUntilIdle()
                }
            }
        }
        advanceUntilIdle()

        coVerify { repository.deleteSavedGame() }
    }

    @Test
    fun `navigate to game over effect on win`() = runTest(mainDispatcherRule.testDispatcher) {
        val vm = createViewModel()
        advanceUntilIdle()

        vm.effect.test {
            val state = vm.uiState.value
            val solution = state.solution
            for (r in 0 until 9) {
                for (c in 0 until 9) {
                    if (!vm.uiState.value.cells[r][c].isGiven && vm.uiState.value.cells[r][c].value == 0) {
                        vm.setEvent(GameUIEvent.CellClicked(r, c))
                        vm.setEvent(GameUIEvent.NumberClicked(solution[r][c]))
                        advanceUntilIdle()
                    }
                }
            }
            advanceUntilIdle()

            val effect = awaitItem()
            assertTrue(effect is GameUIEffect.NavigateToGameOver)
            assertTrue((effect as GameUIEffect.NavigateToGameOver).isWin)
        }
    }

    @Test
    fun `difficulty remembered from settings`() = runTest(mainDispatcherRule.testDispatcher) {
        val vm = createViewModel(difficultyOrdinal = 2)
        advanceUntilIdle()

        assertEquals(Difficulty.HARD, vm.uiState.value.difficulty)
    }

    @Test
    fun `board filled incorrectly with checkErrors off results in loss`() = runTest(mainDispatcherRule.testDispatcher) {
        val settings = AppSettings(checkErrors = false)
        val vm = createViewModel(settings = settings)
        advanceUntilIdle()

        val state = vm.uiState.value
        val solution = state.solution

        for (r in 0 until 9) {
            for (c in 0 until 9) {
                if (!vm.uiState.value.cells[r][c].isGiven && vm.uiState.value.cells[r][c].value == 0) {
                    val wrong = (1..9).first { it != solution[r][c] }
                    vm.setEvent(GameUIEvent.CellClicked(r, c))
                    vm.setEvent(GameUIEvent.NumberClicked(wrong))
                    advanceUntilIdle()
                }
            }
        }
        advanceUntilIdle()

        assertTrue(vm.uiState.value.isGameOver)
        assertFalse(vm.uiState.value.isWin)
    }
}
