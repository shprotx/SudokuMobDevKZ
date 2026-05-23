package ru.shprot.sudokumobdevkz.feature.statistic.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudGameServices
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInState
import ru.shprot.sudokumobdevkz.core.base.data.repository.DailyChallengeRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.SudokuRepository
import ru.shprot.sudokumobdevkz.core.base.data.util.DateTimeUtils
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud.LoadLeaderboardUseCase
import ru.shprot.sudokumobdevkz.core.base.presentation.viewmodel.BaseViewModel
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.contract.StatisticUIEffect
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.contract.StatisticUIEvent
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.contract.StatisticUIState
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
    private val repository: SudokuRepository,
    private val dailyChallengeRepository: DailyChallengeRepository,
    private val cloud: CloudGameServices,
    private val loadLeaderboard: LoadLeaderboardUseCase,
) : BaseViewModel<StatisticUIEvent, StatisticUIState, StatisticUIEffect>(StatisticUIState()) {

    private var observeJob: Job? = null
    private var leaderboardJob: Job? = null

    init {
        updateState { copy(isCloudAvailable = cloud.isAvailable) }
        observeDifficulty(Difficulty.EASY)
        observeDailyPlaytime()
        loadDailyStreaks()
        observeSignInState()
        loadLeaderboardPreview()
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

            StatisticUIEvent.OpenLeaderboardClicked ->
                setEffect(StatisticUIEffect.NavigateToLeaderboard)

            StatisticUIEvent.SignInCtaClicked ->
                setEffect(StatisticUIEffect.NavigateToSettings)

            is StatisticUIEvent.TabSelected ->
                handleTabSelected(event.index)

            is StatisticUIEvent.ResetRequested ->
                handleResetRequested(event.tabIndex)
        }

    private fun handleTabSelected(index: Int) {
        setState(currentState.copy(selectedTab = index))
        val difficulty = Difficulty.fromOrdinal(index)
        observeDifficulty(difficulty)
    }

    private fun handleResetRequested(tabIndex: Int) {
        viewModelScope.launch(exceptionHandler) {
            repository.resetStatistic(Difficulty.fromOrdinal(tabIndex))
        }
    }

    private fun loadDailyStreaks() {
        viewModelScope.launch(exceptionHandler) {
            val current = dailyChallengeRepository.getCurrentStreak()
            val longest = dailyChallengeRepository.getLongestStreak()
            setState(
                currentState.copy(
                    dailyCurrentStreak = current,
                    dailyBestStreak = maxOf(longest, current),
                )
            )
        }
    }

    private fun observeDifficulty(difficulty: Difficulty) {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            repository.observeStatistic(difficulty).collectLatest { stat ->
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
                    )
                )
                fetchPercentile(difficulty, stat?.averageTime ?: 0)
            }
        }
    }

    private fun observeDailyPlaytime() {
        viewModelScope.launch {
            repository.observeDailyPlaytime().collectLatest { dailyPlaytimes ->
                setState(currentState.copy(dailyPlaytimes = dailyPlaytimes))
            }
        }
    }

    private fun observeSignInState() {
        if (!cloud.isAvailable) return
        viewModelScope.launch {
            cloud.signInState.collect { state ->
                val signedIn = state is SignInState.SignedIn
                val wasSignedIn = currentState.isSignedIn
                updateState { copy(isSignedIn = signedIn) }
                if (signedIn && !wasSignedIn) {
                    loadLeaderboardPreview()
                }
            }
        }
    }

    private fun loadLeaderboardPreview() {
        if (!cloud.isAvailable || cloud.signInState.value !is SignInState.SignedIn) {
            updateState { copy(leaderboardPreview = null, isLeaderboardLoading = false) }
            return
        }
        leaderboardJob?.cancel()
        leaderboardJob = viewModelScope.launch(exceptionHandler) {
            updateState { copy(isLeaderboardLoading = true) }
            val data = loadLeaderboard()
            updateState {
                copy(
                    leaderboardPreview = data,
                    isLeaderboardLoading = false,
                )
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
