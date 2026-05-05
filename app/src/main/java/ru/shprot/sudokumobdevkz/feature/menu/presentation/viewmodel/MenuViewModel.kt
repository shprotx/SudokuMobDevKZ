package ru.shprot.sudokumobdevkz.feature.menu.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.shprot.sudokumobdevkz.core.base.data.repository.DailyChallengeRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.SettingsRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.SudokuRepository
import ru.shprot.sudokumobdevkz.core.base.presentation.viewmodel.BaseViewModel
import ru.shprot.sudokumobdevkz.feature.menu.presentation.contract.MenuUIEffect
import ru.shprot.sudokumobdevkz.feature.menu.presentation.contract.MenuUIEvent
import ru.shprot.sudokumobdevkz.feature.menu.presentation.contract.MenuUIState
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val repository: SudokuRepository,
    private val settingsRepository: SettingsRepository,
    private val dailyChallengeRepository: DailyChallengeRepository,
) : BaseViewModel<MenuUIEvent, MenuUIState, MenuUIEffect>(MenuUIState()) {

    init {
        checkSavedGame()
        loadDailyChallenge()
        updateState {
            copy(selectedDifficulty = settingsRepository.currentSettings.selectedDifficultyOrdinal)
        }
    }

    override fun handleUIEvent(event: MenuUIEvent) =
        when (event) {
            MenuUIEvent.ContinueGameClicked ->
                setEffect(MenuUIEffect.NavigateToContinueGame)

            MenuUIEvent.NavigateToStatistic ->
                setEffect(MenuUIEffect.NavigateToStatistic)

            MenuUIEvent.NavigateToSettings ->
                setEffect(MenuUIEffect.NavigateToSettings)

            MenuUIEvent.NavigateToHowToPlay ->
                setEffect(MenuUIEffect.NavigateToHowToPlay)

            MenuUIEvent.DailyChallengeClicked ->
                setEffect(MenuUIEffect.NavigateToDailyChallenge)

            MenuUIEvent.ScreenResumed ->
                onScreenResumed()

            is MenuUIEvent.NewGameClicked ->
                setEffect(MenuUIEffect.NavigateToGame(event.difficultyOrdinal))

            is MenuUIEvent.DifficultySelected ->
                handleDifficultySelected(event.difficultyOrdinal)
        }

    private fun handleDifficultySelected(difficultyOrdinal: Int) {
        updateState { copy(selectedDifficulty = difficultyOrdinal) }
        settingsRepository.update { copy(selectedDifficultyOrdinal = difficultyOrdinal) }
    }

    private fun onScreenResumed() {
        checkSavedGame()
        loadDailyChallenge()
    }

    private fun checkSavedGame() {
        viewModelScope.launch(exceptionHandler) {
            val hasSaved = repository.hasSavedGame()
            updateState { copy(hasSavedGame = hasSaved) }
        }
    }

    private fun loadDailyChallenge() {
        viewModelScope.launch(exceptionHandler) {
            val challenge = dailyChallengeRepository.getTodayChallenge()
            val streak = dailyChallengeRepository.getCurrentStreak()
            updateState {
                copy(
                    dailyChallengeStreak = streak,
                    isDailyChallengeCompleted = challenge.isCompleted,
                )
            }
        }
    }
}