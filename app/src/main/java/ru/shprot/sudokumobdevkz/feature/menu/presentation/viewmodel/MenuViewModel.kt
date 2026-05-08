package ru.shprot.sudokumobdevkz.feature.menu.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.shprot.sudokumobdevkz.core.base.data.StatisticSync
import ru.shprot.sudokumobdevkz.core.base.data.repository.AchievementsRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.DailyChallengeRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.ReviewRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.SettingsRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.SudokuRepository
import ru.shprot.sudokumobdevkz.core.base.domain.usecase.ShouldRequestReviewUseCase
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
    private val achievementsRepository: AchievementsRepository,
    private val statisticSync: StatisticSync,
    private val reviewRepository: ReviewRepository,
    private val shouldRequestReviewUseCase: ShouldRequestReviewUseCase,
) : BaseViewModel<MenuUIEvent, MenuUIState, MenuUIEffect>(MenuUIState()) {

    init {
        statisticSync.ensureStarted()
        checkSavedGame()
        loadDailyChallenge()
        updateState {
            copy(selectedDifficulty = settingsRepository.currentSettings.selectedDifficultyOrdinal)
        }
        checkRetroactiveAchievements()
        checkReviewRequest()
    }

    override fun handleUIEvent(event: MenuUIEvent) =
        when (event) {
            MenuUIEvent.ContinueGameClicked ->
                setEffect(MenuUIEffect.NavigateToContinueGame)

            MenuUIEvent.NavigateToStatistic ->
                setEffect(MenuUIEffect.NavigateToStatistic)

            MenuUIEvent.NavigateToAchievements ->
                setEffect(MenuUIEffect.NavigateToAchievements)

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

    private fun checkRetroactiveAchievements() {
        viewModelScope.launch(exceptionHandler) {
            val unlocked = achievementsRepository.checkAndUnlock(emitToFlow = false)
            when {
                unlocked.isEmpty() -> Unit
                unlocked.size > 3 -> achievementsRepository.emitRetroactiveBatch(unlocked.size)
                else -> achievementsRepository.emitUnlockedToFlow(unlocked)
            }
        }
    }

    private fun checkReviewRequest() {
        viewModelScope.launch(exceptionHandler) {
            if (shouldRequestReviewUseCase()) {
                reviewRepository.markReviewRequested()
                setEffect(MenuUIEffect.RequestInAppReview)
            }
            reviewRepository.clearSessionWon()
        }
    }
}