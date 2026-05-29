package ru.shprot.sudokumobdevkz.feature.settings.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.domain.model.HintMode
import ru.shprot.sudokumobdevkz.core.base.domain.model.ThemeMode
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
    data object ToggleSounds : SettingsUIEvent
    data object ToggleCompactNumberPad : SettingsUIEvent
    data object NavigateToFeedback : SettingsUIEvent
    data object ShareAppClicked : SettingsUIEvent
    data object RateAppClicked : SettingsUIEvent
    data object SignInClicked : SettingsUIEvent
    data object SignOutClicked : SettingsUIEvent
    data object DismissSignOutHint : SettingsUIEvent
    data object OpenPlayGamesAppClicked : SettingsUIEvent
    data object ImportFromCloudClicked : SettingsUIEvent
    data object ImportChoiceMerge : SettingsUIEvent
    data object ImportChoiceKeepLocal : SettingsUIEvent
    data object ImportChoiceUseCloud : SettingsUIEvent
    data object DismissImportDialog : SettingsUIEvent

    data class SelectThemeMode(val mode: ThemeMode) : SettingsUIEvent
    data class SelectHintMode(val mode: HintMode) : SettingsUIEvent
}
