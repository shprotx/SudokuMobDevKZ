package ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudGameServices
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInState
import ru.shprot.sudokumobdevkz.core.base.data.repository.LeaderboardRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.SettingsRepository
import ru.shprot.sudokumobdevkz.core.base.presentation.viewmodel.BaseViewModel
import ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.contract.LeaderboardsUIEffect
import ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.contract.LeaderboardsUIEvent
import ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.contract.LeaderboardsUIState
import javax.inject.Inject

@HiltViewModel
class LeaderboardsViewModel @Inject constructor(
    private val cloud: CloudGameServices,
    private val leaderboardRepository: LeaderboardRepository,
    private val settingsRepository: SettingsRepository,
) : BaseViewModel<LeaderboardsUIEvent, LeaderboardsUIState, LeaderboardsUIEffect>(LeaderboardsUIState()) {

    init {
        observeSignInState()
        observeRepository()
        observeNameConsent()
        leaderboardRepository.refresh()
    }

    override fun handleUIEvent(event: LeaderboardsUIEvent) =
        when (event) {
            LeaderboardsUIEvent.BackClicked ->
                setEffect(LeaderboardsUIEffect.NavigateBack)

            LeaderboardsUIEvent.Refresh ->
                leaderboardRepository.refresh()

            LeaderboardsUIEvent.SignInCtaClicked ->
                setEffect(LeaderboardsUIEffect.NavigateToSettings)

            LeaderboardsUIEvent.DismissNameConsentPrompt ->
                dismissNameConsentPrompt()

            LeaderboardsUIEvent.AcceptNameConsent ->
                acceptNameConsent()
        }

    private fun observeSignInState() {
        if (!cloud.isAvailable) return
        viewModelScope.launch {
            cloud.signInState.collect { state ->
                updateState { copy(isSignedIn = state is SignInState.SignedIn) }
            }
        }
    }

    private fun observeNameConsent() {
        viewModelScope.launch {
            settingsRepository.isLeaderboardNamePromptShown().collect { shown ->
                if (!shown) {
                    updateState { copy(showNameConsentPrompt = true) }
                }
            }
        }
    }

    private fun dismissNameConsentPrompt() {
        settingsRepository.markLeaderboardNamePromptShown()
        updateState { copy(showNameConsentPrompt = false) }
    }

    private fun acceptNameConsent() {
        settingsRepository.markLeaderboardNamePromptShown()
        settingsRepository.update { copy(showNameOnLeaderboard = true) }
        updateState { copy(showNameConsentPrompt = false) }
        leaderboardRepository.refresh()
    }

    private fun observeRepository() {
        viewModelScope.launch {
            combine(
                leaderboardRepository.data,
                leaderboardRepository.isLoading,
            ) { data, loading -> data to loading }.collect { (data, loading) ->
                updateState {
                    copy(
                        data = data,
                        isLoading = loading && data == null,
                        isRefreshing = loading && data != null,
                    )
                }
            }
        }
    }
}
