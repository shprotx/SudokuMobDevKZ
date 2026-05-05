package ru.shprot.sudokumobdevkz.feature.settings.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEvent

sealed interface PrivacyPolicyUIEvent : UIEvent {
    data object BackClicked : PrivacyPolicyUIEvent
}
