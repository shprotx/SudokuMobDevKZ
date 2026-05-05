package ru.shprot.sudokumobdevkz.feature.settings.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEffect

sealed interface PrivacyPolicyUIEffect : UIEffect {
    data object NavigateBack : PrivacyPolicyUIEffect
}
