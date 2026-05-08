package ru.shprot.sudokumobdevkz.feature.settings.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEvent

sealed interface SettingsUIEvent : UIEvent {
    data object BackClicked : SettingsUIEvent
    data object NavigateToPrivacyPolicy : SettingsUIEvent
    data object ShowResetDialog : SettingsUIEvent
    data object DismissResetDialog : SettingsUIEvent
    data object ResetConfirmed : SettingsUIEvent
    data object DismissLockedDialog : SettingsUIEvent
    data object ToggleCheckErrors : SettingsUIEvent
    data object ToggleUnlimitedErrors : SettingsUIEvent
    data object ToggleUnlimitedHints : SettingsUIEvent
    data object ToggleHighlightDuplicates : SettingsUIEvent
    data object ToggleAutoSave : SettingsUIEvent
    data object ToggleShowTimer : SettingsUIEvent
    data object ToggleShowErrors : SettingsUIEvent
    data object ToggleTrackStatistics : SettingsUIEvent
    data object ToggleDarkTheme : SettingsUIEvent
    data object ToggleSounds : SettingsUIEvent
    data object ShareAppClicked : SettingsUIEvent
    data object RateAppClicked : SettingsUIEvent
}
