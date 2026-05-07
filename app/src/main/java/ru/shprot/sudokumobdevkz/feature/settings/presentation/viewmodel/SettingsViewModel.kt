package ru.shprot.sudokumobdevkz.feature.settings.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
) : BaseViewModel<SettingsUIEvent, SettingsUIState, SettingsUIEffect>(
    SettingsUIState()
) {

    init {
        setState(currentState.copy(settings = settingsRepository.currentSettings))
        viewModelScope.launch {
            settingsRepository.settings.collectLatest { settings ->
                updateState { copy(settings = settings) }
            }
        }
        viewModelScope.launch {
            val saved = sudokuRepository.loadSavedGame()
            updateState { copy(hasActiveStandardGame = saved != null && saved.isStandardMode) }
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

            SettingsUIEvent.ShareAppClicked ->
                setEffect(SettingsUIEffect.ShareApp)
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
