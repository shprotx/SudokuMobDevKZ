package ru.shprot.sudokumobdevkz.feature.statistic.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import ru.shprot.sudokumobdevkz.core.base.presentation.viewmodel.BaseViewModel
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.contract.StatisticUIEffect
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.contract.StatisticUIEvent
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.contract.StatisticUIState
import ru.shprot.sudokumobdevkz.core.base.data.repository.SudokuRepository
import ru.shprot.sudokumobdevkz.core.base.data.util.DateTimeUtils
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
    private val repository: SudokuRepository,
) : BaseViewModel<StatisticUIEvent, StatisticUIState, StatisticUIEffect>(StatisticUIState()) {

    private var observeJob: Job? = null

    init {
        observeDifficulty(Difficulty.EASY)
    }

    override fun handleUIEvent(event: StatisticUIEvent) =
        when (event) {
            StatisticUIEvent.ShowResetDialog ->
                setState(currentState.copy(showResetDialog = true))

            StatisticUIEvent.DismissResetDialog ->
                setState(currentState.copy(showResetDialog = false))

            StatisticUIEvent.BackClicked ->
                setEffect(StatisticUIEffect.NavigateBack)

            StatisticUIEvent.ResetClicked ->
                setState(currentState.copy(showResetDialog = true))

            is StatisticUIEvent.TabSelected ->
                handleTabSelected(event.index)

            is StatisticUIEvent.ResetRequested ->
                handleResetRequested(event.tabIndex)
        }

    private fun handleTabSelected(index: Int) {
        setState(currentState.copy(selectedTab = index))
        observeDifficulty(Difficulty.fromOrdinal(index))
    }

    private fun handleResetRequested(tabIndex: Int) {
        viewModelScope.launch(exceptionHandler) {
            repository.resetStatistic(Difficulty.fromOrdinal(tabIndex))
        }
    }

    private fun observeDifficulty(difficulty: Difficulty) {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            combine(
                repository.observeStatistic(difficulty),
                repository.observeRecentGames(difficulty),
            ) { stat, history ->
                stat to history
            }.collectLatest { (stat, history) ->
                setState(
                    currentState.copy(
                        bestTime = stat?.bestTime?.let { if (it <= 0) "--:--" else DateTimeUtils.formatTimer(it) } ?: "--:--",
                        averageTime = stat?.averageTime?.let { if (it <= 0) "--:--" else DateTimeUtils.formatTimer(it) } ?: "--:--",
                        percentOfWins = "${stat?.percentOfWins ?: 0}%",
                        winsWithoutErrors = "${stat?.winsWithoutErrors ?: 0}",
                        gamesStarted = "${stat?.gamesStarted ?: 0}",
                        gamesWon = "${stat?.gamesWon ?: 0}",
                        bestWinsLine = "${stat?.bestWinsLine ?: 0}",
                        currentWinsLine = "${stat?.currentWinsLine ?: 0}",
                        casualGamesPlayed = "${stat?.casualGamesPlayed ?: 0}",
                        recentGames = history,
                    )
                )
                fetchPercentile(difficulty, stat?.averageTime ?: 0)
            }
        }
    }

    private fun fetchPercentile(difficulty: Difficulty, averageTime: Int) {
        if (averageTime <= 0) {
            setState(currentState.copy(percentile = -1, totalPlayers = 0))
            return
        }
        viewModelScope.launch(exceptionHandler) {
            val result = repository.fetchPercentile(difficulty, averageTime)
            setState(currentState.copy(percentile = result.percentile, totalPlayers = result.totalPlayers))
        }
    }

}
