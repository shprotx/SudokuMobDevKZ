package ru.shprot.sudokumobdevkz.feature.settings.presentation.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.shprot.sudokumobdevkz.core.base.presentation.viewmodel.BaseViewModel
import ru.shprot.sudokumobdevkz.feature.settings.presentation.contract.PrivacyPolicyUIEffect
import ru.shprot.sudokumobdevkz.feature.settings.presentation.contract.PrivacyPolicyUIEvent
import ru.shprot.sudokumobdevkz.feature.settings.presentation.contract.PrivacyPolicyUIState
import javax.inject.Inject

@HiltViewModel
class PrivacyPolicyViewModel @Inject constructor() :
    BaseViewModel<PrivacyPolicyUIEvent, PrivacyPolicyUIState, PrivacyPolicyUIEffect>(
        PrivacyPolicyUIState()
    ) {

    override fun handleUIEvent(event: PrivacyPolicyUIEvent) =
        when (event) {
            PrivacyPolicyUIEvent.BackClicked ->
                setEffect(PrivacyPolicyUIEffect.NavigateBack)
        }
}
