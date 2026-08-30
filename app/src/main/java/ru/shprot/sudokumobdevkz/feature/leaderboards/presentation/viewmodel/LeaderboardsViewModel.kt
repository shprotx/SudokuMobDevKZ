package ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudGameServices
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInState
import ru.shprot.sudokumobdevkz.core.base.data.repository.ILeaderboardRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.ISettingsRepository
import ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud.ToggleShowNameOnLeaderboardUseCase
import ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud.UpdateLeaderboardIdentityUseCase
import ru.shprot.sudokumobdevkz.core.base.presentation.snackbar.SnackbarManager
import ru.shprot.sudokumobdevkz.core.base.presentation.viewmodel.BaseViewModel
import ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.contract.LeaderboardsUIEffect
import ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.contract.LeaderboardsUIEvent
import ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.contract.LeaderboardsUIState
import javax.inject.Inject

@HiltViewModel
class LeaderboardsViewModel @Inject constructor(
    private val cloud: CloudGameServices,
    private val leaderboardRepository: ILeaderboardRepository,
    private val settingsRepository: ISettingsRepository,
    private val updateLeaderboardIdentity: UpdateLeaderboardIdentityUseCase,
    private val toggleShowNameOnLeaderboard: ToggleShowNameOnLeaderboardUseCase,
) : BaseViewModel<LeaderboardsUIEvent, LeaderboardsUIState, LeaderboardsUIEffect>(LeaderboardsUIState()) {

    private var identityPushed = false

    init {
        observeSignInState()
        observeRepository()
        observeNameConsent()
        observeSettings()
        leaderboardRepository.refresh()
        pushIdentityIfPossible()
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

            LeaderboardsUIEvent.ToggleShowName ->
                handleToggleShowName()
        }

    private fun handleToggleShowName() {
        if (!currentState.isSignedIn) {
            setEffect(LeaderboardsUIEffect.NavigateToSettings)
            SnackbarManager.show(R.string.leaderboard_show_name_sign_in_required)
            return
        }
        viewModelScope.launch {
            toggleShowNameOnLeaderboard()
            leaderboardRepository.refresh()
        }
    }

    private fun observeSettings() {
        viewModelScope.launch {
            settingsRepository.settings.collect { settings ->
                updateState { copy(showNameOnLeaderboard = settings.showNameOnLeaderboard) }
            }
        }
    }

    private fun observeSignInState() {
        if (!cloud.isAvailable) return
        viewModelScope.launch {
            cloud.signInState.collect { state ->
                updateState { copy(isSignedIn = state is SignInState.SignedIn) }
                pushIdentityIfPossible()
            }
        }
    }

    private fun pushIdentityIfPossible() {
        if (identityPushed) return
        val showName = settingsRepository.currentSettings.showNameOnLeaderboard
        if (showName && !currentState.isSignedIn) return
        identityPushed = true
        viewModelScope.launch {
            updateLeaderboardIdentity(showName = showName)
            leaderboardRepository.refresh()
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
        viewModelScope.launch {
            updateLeaderboardIdentity(showName = true)
            leaderboardRepository.refresh()
        }
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
