package ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.shprot.sudokumobdevkz.core.base.data.repository.DailyChallengeRepository
import ru.shprot.sudokumobdevkz.core.base.data.util.DateTimeUtils
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import ru.shprot.sudokumobdevkz.core.base.presentation.viewmodel.BaseViewModel
import ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.contract.DailyChallengeUIEffect
import ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.contract.DailyChallengeUIEvent
import ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.contract.DailyChallengeUIState
import javax.inject.Inject

@HiltViewModel
class DailyChallengeViewModel @Inject constructor(
    private val repository: DailyChallengeRepository,
) : BaseViewModel<DailyChallengeUIEvent, DailyChallengeUIState, DailyChallengeUIEffect>(
    DailyChallengeUIState(),
) {

    init {
        loadChallenge()
    }

    override fun handleUIEvent(event: DailyChallengeUIEvent) =
        when (event) {
            DailyChallengeUIEvent.BackClicked ->
                setEffect(DailyChallengeUIEffect.NavigateBack)

            DailyChallengeUIEvent.PlayClicked ->
                handlePlayClicked()
        }

    private fun handlePlayClicked() {
        if (currentState.isCompletedToday) return
        setEffect(DailyChallengeUIEffect.NavigateToGame(currentState.difficulty.ordinal))
    }

    private fun loadChallenge() {
        viewModelScope.launch(exceptionHandler) {
            val challenge = repository.getTodayChallenge()
            val current = repository.getCurrentStreak()
            val longest = repository.getLongestStreak()
            updateState {
                copy(
                    dateLabel = DateTimeUtils.formatLocalizedDate(challenge.dateKey),
                    difficulty = Difficulty.fromOrdinal(challenge.difficultyOrdinal),
                    currentStreak = current,
                    longestStreak = maxOf(longest, current),
                    isCompletedToday = challenge.isCompleted,
                    completionTimeSeconds = challenge.completionTimeSeconds,
                    errors = challenge.errors,
                    isLoading = false,
                )
            }
        }
    }
}