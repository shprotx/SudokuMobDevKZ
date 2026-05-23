package ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudGameServices
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInState
import ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud.LoadLeaderboardUseCase
import ru.shprot.sudokumobdevkz.core.base.presentation.viewmodel.BaseViewModel
import ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.contract.LeaderboardsUIEffect
import ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.contract.LeaderboardsUIEvent
import ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.contract.LeaderboardsUIState
import javax.inject.Inject

@HiltViewModel
class LeaderboardsViewModel @Inject constructor(
    private val cloud: CloudGameServices,
    private val loadLeaderboard: LoadLeaderboardUseCase,
) : BaseViewModel<LeaderboardsUIEvent, LeaderboardsUIState, LeaderboardsUIEffect>(LeaderboardsUIState()) {

    private var loadJob: Job? = null

    init {
        observeSignInState()
        load(isRefresh = false)
    }

    override fun handleUIEvent(event: LeaderboardsUIEvent) =
        when (event) {
            LeaderboardsUIEvent.BackClicked ->
                setEffect(LeaderboardsUIEffect.NavigateBack)

            LeaderboardsUIEvent.Refresh ->
                load(isRefresh = true)

            LeaderboardsUIEvent.SignInCtaClicked ->
                setEffect(LeaderboardsUIEffect.NavigateToSettings)
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
                    load(isRefresh = false)
                }
            }
        }
    }

    private fun load(isRefresh: Boolean) {
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
            val result = runCatching { loadLeaderboard() }
            result.fold(
                onSuccess = { data ->
                    val debugLog = data.topRows.joinToString("\n") {
                        "rank=${it.rank} name=${it.displayName} avatar=${it.avatarUri}"
                    }
                    updateState {
                        copy(
                            data = data,
                            isLoading = false,
                            isRefreshing = false,
                            debugAvatarLog = debugLog,
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
