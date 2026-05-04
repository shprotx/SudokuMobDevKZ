package ru.shprot.sudokumobdevkz.feature.game.presentation.viewmodel

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
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
import ru.shprot.sudokumobdevkz.core.base.domain.generator.SudokuGenerator
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameUIEffect
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameUIEvent
import ru.shprot.sudokumobdevkz.testutil.MainDispatcherRule
import ru.shprot.sudokumobdevkz.testutil.TEST_GENERATED_PUZZLE
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
        mockkObject(SudokuGenerator)
        coEvery { SudokuGenerator.generate(any()) } returns TEST_GENERATED_PUZZLE
        every { settingsRepository.currentSettings } returns AppSettings()
        coEvery { repository.loadSavedGame() } returns null
    }

    @After
    fun tearDown() {
        unmockkObject(SudokuGenerator)
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
    fun `standard mode generates puzzle and starts timer`() = runTest(mainDispatcherRule.testDispatcher) {
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

        fillAllCorrectly(vm)
        advanceUntilIdle()

        assertTrue(vm.uiState.value.isGameOver)
        assertTrue(vm.uiState.value.isWin)
        coVerify { repository.updateStatistic(Difficulty.EASY, true, any(), any()) }
        coVerify { repository.deleteSavedGame() }
    }

    @Test
    fun `standard mode loss on 3 errors`() = runTest(mainDispatcherRule.testDispatcher) {
        val vm = createViewModel()
        advanceUntilIdle()

        makeWrongMoves(vm, 3)
        advanceUntilIdle()

        assertTrue(vm.uiState.value.isGameOver)
        assertFalse(vm.uiState.value.isWin)
        coVerify { repository.updateStatistic(Difficulty.EASY, false, any(), any()) }
    }

    @Test
    fun `casual mode with unlimited hints increments casual counter`() = runTest(mainDispatcherRule.testDispatcher) {
        val vm = createViewModel(settings = AppSettings(unlimitedHints = true))
        advanceUntilIdle()

        assertFalse(vm.uiState.value.isStandardMode)
        assertEquals(Int.MAX_VALUE, vm.uiState.value.hintsRemaining)

        fillAllCorrectly(vm)
        advanceUntilIdle()

        coVerify { repository.incrementCasualGames(Difficulty.EASY) }
        coVerify(exactly = 0) { repository.updateStatistic(any(), any(), any(), any()) }
    }

    @Test
    fun `casual mode with unlimited errors`() = runTest(mainDispatcherRule.testDispatcher) {
        val vm = createViewModel(settings = AppSettings(unlimitedErrors = true))
        advanceUntilIdle()

        assertFalse(vm.uiState.value.isStandardMode)
        assertEquals(Int.MAX_VALUE, vm.uiState.value.maxErrors)
    }

    @Test
    fun `checkErrors false does not mark wrong numbers`() = runTest(mainDispatcherRule.testDispatcher) {
        val vm = createViewModel(settings = AppSettings(checkErrors = false))
        advanceUntilIdle()

        val emptyCell = findFirstEmptyCell(vm)
        val correct = vm.uiState.value.solution[emptyCell.first][emptyCell.second]
        val wrong = (1..9).first { it != correct }

        vm.setEvent(GameUIEvent.CellClicked(emptyCell.first, emptyCell.second))
        vm.setEvent(GameUIEvent.NumberClicked(wrong))
        advanceUntilIdle()

        val cell = vm.uiState.value.cells[emptyCell.first][emptyCell.second]
        assertEquals(wrong, cell.value)
        assertFalse(cell.isError)
        assertEquals(0, vm.uiState.value.errors)
    }

    @Test
    fun `cannot overwrite correctly placed value`() = runTest(mainDispatcherRule.testDispatcher) {
        val vm = createViewModel()
        advanceUntilIdle()

        val emptyCell = findFirstEmptyCell(vm)
        val (r, c) = emptyCell
        val correct = vm.uiState.value.solution[r][c]

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
    fun `abandoned standard game counts as loss`() = runTest(mainDispatcherRule.testDispatcher) {
        coEvery { repository.loadSavedGame() } returns createSavedGame(isStandardMode = true)

        val vm = createViewModel()
        advanceUntilIdle()

        coVerify { repository.updateStatistic(Difficulty.EASY, false, 120, 1) }
        coVerify { repository.deleteSavedGame() }
    }

    @Test
    fun `abandoned casual game increments casual counter`() = runTest(mainDispatcherRule.testDispatcher) {
        coEvery { repository.loadSavedGame() } returns createSavedGame(
            isStandardMode = false,
            difficulty = Difficulty.MEDIUM.firebaseKey,
        )

        val vm = createViewModel()
        advanceUntilIdle()

        coVerify { repository.incrementCasualGames(Difficulty.MEDIUM) }
    }

    @Test
    fun `continue game restores saved state`() = runTest(mainDispatcherRule.testDispatcher) {
        val saved = createSavedGame(
            difficulty = Difficulty.HARD.firebaseKey,
            timeSeconds = 300,
            errors = 2,
        )
        coEvery { repository.loadSavedGame() } returns saved

        val vm = createViewModel(difficultyOrdinal = 2, continueGame = true)
        advanceUntilIdle()

        val state = vm.uiState.value
        assertFalse(state.isGenerating)
        assertEquals(Difficulty.HARD, state.difficulty)
        assertEquals(300, state.timeSeconds)
        assertEquals(2, state.errors)
    }

    @Test
    fun `game over deletes saved game`() = runTest(mainDispatcherRule.testDispatcher) {
        val vm = createViewModel()
        advanceUntilIdle()

        fillAllCorrectly(vm)
        advanceUntilIdle()

        coVerify { repository.deleteSavedGame() }
    }

    @Test
    fun `navigate effect emitted on win`() = runTest(mainDispatcherRule.testDispatcher) {
        val vm = createViewModel()
        advanceUntilIdle()

        vm.effect.test {
            fillAllCorrectly(vm)
            advanceUntilIdle()

            val effect = awaitItem()
            assertTrue(effect is GameUIEffect.NavigateToGameOver)
            assertTrue((effect as GameUIEffect.NavigateToGameOver).isWin)
        }
    }

    @Test
    fun `difficulty from route`() = runTest(mainDispatcherRule.testDispatcher) {
        val vm = createViewModel(difficultyOrdinal = 2)
        advanceUntilIdle()

        assertEquals(Difficulty.HARD, vm.uiState.value.difficulty)
    }

    @Test
    fun `board filled wrong with checkErrors off is loss`() = runTest(mainDispatcherRule.testDispatcher) {
        val vm = createViewModel(settings = AppSettings(checkErrors = false))
        advanceUntilIdle()

        fillAllWrongly(vm)
        advanceUntilIdle()

        assertTrue(vm.uiState.value.isGameOver)
        assertFalse(vm.uiState.value.isWin)
    }

    private fun fillAllCorrectly(vm: GameViewModel) {
        val state = vm.uiState.value
        for (r in 0 until 9) {
            for (c in 0 until 9) {
                if (!vm.uiState.value.cells[r][c].isGiven && vm.uiState.value.cells[r][c].value == 0) {
                    vm.setEvent(GameUIEvent.CellClicked(r, c))
                    vm.setEvent(GameUIEvent.NumberClicked(state.solution[r][c]))
                }
            }
        }
    }

    private fun fillAllWrongly(vm: GameViewModel) {
        val state = vm.uiState.value
        for (r in 0 until 9) {
            for (c in 0 until 9) {
                if (!vm.uiState.value.cells[r][c].isGiven && vm.uiState.value.cells[r][c].value == 0) {
                    val correct = state.solution[r][c]
                    val wrong = (1..9).first { it != correct }
                    vm.setEvent(GameUIEvent.CellClicked(r, c))
                    vm.setEvent(GameUIEvent.NumberClicked(wrong))
                }
            }
        }
    }

    private fun makeWrongMoves(vm: GameViewModel, count: Int) {
        val state = vm.uiState.value
        var given = 0
        for (r in 0 until 9) {
            if (given >= count) break
            for (c in 0 until 9) {
                if (given >= count) break
                val cell = state.cells[r][c]
                if (cell.isGiven || cell.value != 0) continue
                val correct = state.solution[r][c]
                val wrong = (1..9).first { it != correct }
                vm.setEvent(GameUIEvent.CellClicked(r, c))
                vm.setEvent(GameUIEvent.NumberClicked(wrong))
                given++
            }
        }
    }

    private fun findFirstEmptyCell(vm: GameViewModel): Pair<Int, Int> {
        val state = vm.uiState.value
        for (r in 0 until 9) {
            for (c in 0 until 9) {
                if (!state.cells[r][c].isGiven && state.cells[r][c].value == 0) return r to c
            }
        }
        error("No empty cell found")
    }

    private fun createSavedGame(
        difficulty: Int = Difficulty.EASY.firebaseKey,
        timeSeconds: Int = 120,
        errors: Int = 1,
        isStandardMode: Boolean = true,
    ) = GameSaveData(
        difficulty = difficulty,
        timeSeconds = timeSeconds,
        errors = errors,
        maxErrors = 3,
        hintsRemaining = 2,
        isNotesEnabled = false,
        isStandardMode = isStandardMode,
        cells = List(9) { List(9) { GameSaveData.CellSave() } },
        solution = List(9) { List(9) { 0 } },
    )
}
