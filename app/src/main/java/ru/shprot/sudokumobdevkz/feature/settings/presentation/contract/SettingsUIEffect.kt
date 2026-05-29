package ru.shprot.sudokumobdevkz.feature.settings.presentation.contract

import androidx.annotation.StringRes
import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEffect

sealed interface SettingsUIEffect : UIEffect {
    data object NavigateBack : SettingsUIEffect
    data object NavigateToPrivacyPolicy : SettingsUIEffect
    data object NavigateToFeedback : SettingsUIEffect
    data object ShareApp : SettingsUIEffect
    data object OpenPlayStore : SettingsUIEffect
    data object OpenPlayGamesApp : SettingsUIEffect
    data object NavigateToThemeBuilder : SettingsUIEffect

    data class ShowMessage(@StringRes val messageRes: Int) : SettingsUIEffect
    data class NavigateToEditTheme(val themeId: String) : SettingsUIEffect
}
