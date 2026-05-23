package ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudGameServices
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInState
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud.LoadLeaderboardUseCase
import ru.shprot.sudokumobdevkz.core.base.presentation.viewmodel.BaseViewModel
import ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.contract.LeaderboardsUIEffect
import ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.contract.LeaderboardsUIEvent
import ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.contract.LeaderboardsUIState
import ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.navigation.LeaderboardsRoutes
import javax.inject.Inject

@HiltViewModel
class LeaderboardsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val cloud: CloudGameServices,
    private val loadLeaderboard: LoadLeaderboardUseCase,
) : BaseViewModel<LeaderboardsUIEvent, LeaderboardsUIState, LeaderboardsUIEffect>(LeaderboardsUIState()) {

    private val route = savedStateHandle.toRoute<LeaderboardsRoutes.LeaderboardsScreen>()
    private var loadJob: Job? = null

    init {
        updateState { copy(selectedTab = route.difficultyOrdinal) }
        observeSignInState()
        load(Difficulty.fromOrdinal(route.difficultyOrdinal), isRefresh = false)
    }

    override fun handleUIEvent(event: LeaderboardsUIEvent) =
        when (event) {
            LeaderboardsUIEvent.BackClicked ->
                setEffect(LeaderboardsUIEffect.NavigateBack)

            LeaderboardsUIEvent.Refresh ->
                load(Difficulty.fromOrdinal(currentState.selectedTab), isRefresh = true)

            LeaderboardsUIEvent.SignInCtaClicked ->
                setEffect(LeaderboardsUIEffect.NavigateToSettings)

            is LeaderboardsUIEvent.TabSelected ->
                handleTabSelected(event.index)
        }

    private fun handleTabSelected(index: Int) {
        updateState { copy(selectedTab = index, data = null) }
        load(Difficulty.fromOrdinal(index), isRefresh = false)
    }

    private fun observeSignInState() {
        if (!cloud.isAvailable) {
            updateState { copy(isSignedIn = false) }
            return
        }
        viewModelScope.launch {
            cloud.signInState.collect { state ->
                val signedIn = state is SignInState.SignedIn
                val wasSignedIn = currentState.isSignedIn
                updateState { copy(isSignedIn = signedIn) }
                if (signedIn && !wasSignedIn) {
                    load(Difficulty.fromOrdinal(currentState.selectedTab), isRefresh = false)
                }
            }
        }
    }

    private fun load(difficulty: Difficulty, isRefresh: Boolean) {
        if (!cloud.isAvailable || cloud.signInState.value !is SignInState.SignedIn) {
            updateState {
                copy(
                    isLoading = false,
                    isRefreshing = false,
                    data = null,
                )
            }
            return
        }
        loadJob?.cancel()
        loadJob = viewModelScope.launch(exceptionHandler) {
            updateState {
                copy(
                    isLoading = !isRefresh,
                    isRefreshing = isRefresh,
                    errorMessageRes = null,
                )
            }
            val data = runCatching { loadLeaderboard(difficulty) }
            data.fold(
                onSuccess = { result ->
                    updateState {
                        copy(
                            data = result,
                            isLoading = false,
                            isRefreshing = false,
                        )
                    }
                },
                onFailure = {
                    updateState {
                        copy(
                            data = null,
                            isLoading = false,
                            isRefreshing = false,
                            errorMessageRes = R.string.leaderboard_load_error,
                        )
                    }
                },
            )
        }
    }
}
