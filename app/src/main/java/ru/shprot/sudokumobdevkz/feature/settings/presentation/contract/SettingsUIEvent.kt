package ru.shprot.sudokumobdevkz.feature.settings.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.data.repository.AppSettings
import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEvent

sealed interface SettingsUIEvent : UIEvent {
    data class SettingChanged(val transform: AppSettings.() -> AppSettings) : SettingsUIEvent
    data object BackClicked : SettingsUIEvent
    data object NavigateToPrivacyPolicy : SettingsUIEvent
    data object ShowResetDialog : SettingsUIEvent
    data object DismissResetDialog : SettingsUIEvent
    data object ResetConfirmed : SettingsUIEvent
}
