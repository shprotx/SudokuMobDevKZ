package ru.shprot.sudokumobdevkz.feature.settings.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudGameServices
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInResult
import ru.shprot.sudokumobdevkz.core.base.domain.model.AppSettings
import ru.shprot.sudokumobdevkz.core.base.data.repository.SettingsRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.SudokuRepository
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import ru.shprot.sudokumobdevkz.core.base.presentation.viewmodel.BaseViewModel
import ru.shprot.sudokumobdevkz.feature.settings.presentation.contract.SettingsUIEffect
import ru.shprot.sudokumobdevkz.feature.settings.presentation.contract.SettingsUIEvent
import ru.shprot.sudokumobdevkz.feature.settings.presentation.contract.SettingsUIState
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val sudokuRepository: SudokuRepository,
    private val cloud: CloudGameServices,
) : BaseViewModel<SettingsUIEvent, SettingsUIState, SettingsUIEffect>(
    SettingsUIState()
) {

    init {
        setState(currentState.copy(
            settings = settingsRepository.currentSettings,
            isCloudAvailable = cloud.isAvailable,
        ))
        viewModelScope.launch {
            settingsRepository.settings.collectLatest { settings ->
                updateState { copy(settings = settings) }
            }
        }
        viewModelScope.launch {
            val saved = sudokuRepository.loadSavedGame()
            updateState { copy(hasActiveStandardGame = saved != null && saved.isStandardMode) }
        }
        if (cloud.isAvailable) {
            viewModelScope.launch {
                cloud.signInState.collect { state ->
                    updateState { copy(signInState = state) }
                }
            }
        }
    }

    override fun handleUIEvent(event: SettingsUIEvent) =
        when (event) {
            SettingsUIEvent.BackClicked ->
                setEffect(SettingsUIEffect.NavigateBack)

            SettingsUIEvent.NavigateToPrivacyPolicy ->
                setEffect(SettingsUIEffect.NavigateToPrivacyPolicy)

            SettingsUIEvent.ShowResetDialog ->
                updateState { copy(showResetDialog = true) }

            SettingsUIEvent.DismissResetDialog ->
                updateState { copy(showResetDialog = false) }

            SettingsUIEvent.ResetConfirmed ->
                handleResetConfirmed()

            SettingsUIEvent.DismissLockedDialog ->
                updateState { copy(showLockedSettingDialog = false) }

            SettingsUIEvent.ToggleCheckErrors ->
                handleSensitiveSetting { copy(checkErrors = !checkErrors) }

            SettingsUIEvent.ToggleUnlimitedErrors ->
                handleSensitiveSetting { copy(unlimitedErrors = !unlimitedErrors) }

            SettingsUIEvent.ToggleUnlimitedHints ->
                handleSensitiveSetting { copy(unlimitedHints = !unlimitedHints) }

            SettingsUIEvent.ToggleHighlightDuplicates ->
                settingsRepository.update { copy(highlightDuplicates = !highlightDuplicates) }

            SettingsUIEvent.ToggleAutoSave ->
                settingsRepository.update { copy(autoSave = !autoSave) }

            SettingsUIEvent.ToggleShowTimer ->
                settingsRepository.update { copy(showTimer = !showTimer) }

            SettingsUIEvent.ToggleShowErrors ->
                settingsRepository.update { copy(showErrors = !showErrors) }

            SettingsUIEvent.ToggleTrackStatistics ->
                settingsRepository.update { copy(trackStatistics = !trackStatistics) }

            SettingsUIEvent.ToggleDarkTheme ->
                settingsRepository.update { copy(isDarkTheme = !isDarkTheme) }

            SettingsUIEvent.ToggleSounds ->
                settingsRepository.update { copy(soundsEnabled = !soundsEnabled) }

            SettingsUIEvent.ToggleCompactNumberPad ->
                settingsRepository.update { copy(compactNumberPad = !compactNumberPad) }

            SettingsUIEvent.ShareAppClicked ->
                setEffect(SettingsUIEffect.ShareApp)

            SettingsUIEvent.RateAppClicked ->
                setEffect(SettingsUIEffect.OpenPlayStore)

            SettingsUIEvent.SignInClicked ->
                handleSignIn()

            SettingsUIEvent.SignOutClicked ->
                updateState { copy(showSignOutHint = true) }

            SettingsUIEvent.DismissSignOutHint ->
                updateState { copy(showSignOutHint = false) }

            SettingsUIEvent.OpenPlayGamesAppClicked -> {
                updateState { copy(showSignOutHint = false) }
                setEffect(SettingsUIEffect.OpenPlayGamesApp)
            }
        }

    private fun handleSignIn() {
        if (currentState.isSigningIn) return
        updateState { copy(isSigningIn = true) }
        viewModelScope.launch(exceptionHandler) {
            val result = cloud.requestSignIn()
            updateState { copy(isSigningIn = false) }
            if (result is SignInResult.Failure) {
                setEffect(SettingsUIEffect.ShowMessage(R.string.cloud_sign_in_failed))
            }
        }
    }

    private fun handleResetConfirmed() {
        viewModelScope.launch(exceptionHandler) {
            for (difficulty in Difficulty.entries) {
                sudokuRepository.resetStatistic(difficulty)
            }
        }
        updateState { copy(showResetDialog = false) }
    }

    private fun handleSensitiveSetting(transform: AppSettings.() -> AppSettings) {
        if (currentState.hasActiveStandardGame) {
            updateState { copy(showLockedSettingDialog = true) }
        } else {
            settingsRepository.update(transform)
        }
    }
}
