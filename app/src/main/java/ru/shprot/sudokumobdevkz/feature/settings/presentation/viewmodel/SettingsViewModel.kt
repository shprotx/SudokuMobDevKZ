package ru.shprot.sudokumobdevkz.feature.settings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.shprot.sudokumobdevkz.core.base.data.repository.AppSettings
import ru.shprot.sudokumobdevkz.core.base.data.repository.SettingsRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.SudokuRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val sudokuRepository: SudokuRepository,
) : ViewModel() {

    val settings = settingsRepository.settings
        .stateIn(viewModelScope, SharingStarted.Eagerly, settingsRepository.currentSettings)

    fun updateSetting(transform: AppSettings.() -> AppSettings) {
        settingsRepository.update(transform)
    }

    fun resetAllStatistics() {
        viewModelScope.launch {
            for (difficulty in 0..2) {
                sudokuRepository.resetStatistic(difficulty)
            }
        }
    }
}
