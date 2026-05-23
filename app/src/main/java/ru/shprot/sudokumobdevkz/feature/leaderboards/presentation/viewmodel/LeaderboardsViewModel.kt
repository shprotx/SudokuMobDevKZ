package ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudGameServices
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInState
import ru.shprot.sudokumobdevkz.core.base.data.repository.LeaderboardRepository
import ru.shprot.sudokumobdevkz.core.base.presentation.viewmodel.BaseViewModel
import ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.contract.LeaderboardsUIEffect
import ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.contract.LeaderboardsUIEvent
import ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.contract.LeaderboardsUIState
import javax.inject.Inject

@HiltViewModel
class LeaderboardsViewModel @Inject constructor(
    private val cloud: CloudGameServices,
    private val leaderboardRepository: LeaderboardRepository,
) : BaseViewModel<LeaderboardsUIEvent, LeaderboardsUIState, LeaderboardsUIEffect>(LeaderboardsUIState()) {

    init {
        observeSignInState()
        observeRepository()
        if (cloud.isAvailable && cloud.signInState.value is SignInState.SignedIn) {
            leaderboardRepository.refresh()
        }
    }

    override fun handleUIEvent(event: LeaderboardsUIEvent) =
        when (event) {
            LeaderboardsUIEvent.BackClicked ->
                setEffect(LeaderboardsUIEffect.NavigateBack)

            LeaderboardsUIEvent.Refresh ->
                leaderboardRepository.refresh()

            LeaderboardsUIEvent.SignInCtaClicked ->
                setEffect(LeaderboardsUIEffect.NavigateToSettings)
        }

    private fun observeSignInState() {
        if (!cloud.isAvailable) return
        viewModelScope.launch {
            cloud.signInState.collect { state ->
                updateState { copy(isSignedIn = state is SignInState.SignedIn) }
            }
        }
    }

    private fun observeRepository() {
        viewModelScope.launch {
            combine(
                leaderboardRepository.data,
                leaderboardRepository.isLoading,
            ) { data, loading -> data to loading }.collect { (data, loading) ->
                val debugLog = data?.topRows.orEmpty().joinToString("\n") {
                    "rank=${it.rank} name=${it.displayName} avatar=${it.avatarUrl}"
                }
                updateState {
                    copy(
                        data = data,
                        isLoading = loading && data == null,
                        isRefreshing = loading && data != null,
                        debugAvatarLog = debugLog,
                    )
                }
            }
        }
    }
}
