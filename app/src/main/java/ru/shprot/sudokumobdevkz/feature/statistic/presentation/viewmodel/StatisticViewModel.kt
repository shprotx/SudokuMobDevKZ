package ru.shprot.sudokumobdevkz.feature.statistic.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ru.shprot.sudokumobdevkz.core.base.presentation.viewmodel.BaseViewModel
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.contract.StatisticEffect
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.contract.StatisticEvent
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.contract.StatisticUiState
import ru.shprot.sudokumobdevkz.model.repository.SudokuRepository
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
    private val repository: SudokuRepository,
) : BaseViewModel<StatisticEvent, StatisticUiState, StatisticEffect>(StatisticUiState()) {

    private var observeJob: Job? = null

    init {
        observeDifficulty(0)
    }

    override fun handleUIEvent(event: StatisticEvent) {
        when (event) {
            is StatisticEvent.TabSelected -> {
                setState(currentState.copy(selectedTab = event.index))
                observeDifficulty(event.index)
            }
            is StatisticEvent.ResetRequested -> {
                viewModelScope.launch(exceptionHandler) {
                    repository.resetStatistic(event.difficulty)
                }
            }
        }
    }

    private fun observeDifficulty(difficulty: Int) {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            combine(
                repository.observeStatistic(difficulty),
                repository.observeRecentWins(difficulty),
            ) { stat, history ->
                stat to history
            }.collectLatest { (stat, history) ->
                setState(
                    currentState.copy(
                        bestTime = stat?.bestTime?.toTimeString() ?: "--:--",
                        averageTime = stat?.averageTime?.toTimeString() ?: "--:--",
                        percentOfWins = "${stat?.percentOfWins ?: 0}%",
                        winsWithoutErrors = "${stat?.winsWithoutErrors ?: 0}",
                        gamesStarted = "${stat?.gamesStarted ?: 0}",
                        gamesWon = "${stat?.gamesWon ?: 0}",
                        bestWinsLine = "${stat?.bestWinsLine ?: 0}",
                        currentWinsLine = "${stat?.currentWinsLine ?: 0}",
                        recentGames = history,
                    )
                )
            }
        }
    }

    private fun Int.toTimeString(): String {
        if (this <= 0) return "--:--"
        val m = this / 60
        val s = this % 60
        return "%02d:%02d".format(m, s)
    }
}
