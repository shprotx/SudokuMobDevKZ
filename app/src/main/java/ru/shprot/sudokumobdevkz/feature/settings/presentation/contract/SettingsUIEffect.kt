package ru.shprot.sudokumobdevkz.feature.settings.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEffect

sealed interface SettingsUIEffect : UIEffect {
    data object NavigateBack : SettingsUIEffect
    data object NavigateToPrivacyPolicy : SettingsUIEffect
    data object ShareApp : SettingsUIEffect
}
