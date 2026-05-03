package ru.shprot.sudokumobdevkz.feature.settings.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.shprot.sudokumobdevkz.core.base.data.repository.SettingsRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.SudokuRepository
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

            SettingsUIEvent.ResetConfirmed -> {
                viewModelScope.launch(exceptionHandler) {
                    for (difficulty in 0..2) {
                        sudokuRepository.resetStatistic(difficulty)
                    }
                }
                updateState { copy(showResetDialog = false) }
            }

            is SettingsUIEvent.SettingChanged ->
                settingsRepository.update(event.transform)
        }
}
