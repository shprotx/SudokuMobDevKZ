package ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.shprot.sudokumobdevkz.core.base.presentation.viewmodel.BaseViewModel
import ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.contract.HowToPlayUIEffect
import ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.contract.HowToPlayUIEvent
import ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.contract.HowToPlayUIState
import javax.inject.Inject

@HiltViewModel
class HowToPlayViewModel @Inject constructor() :
    BaseViewModel<HowToPlayUIEvent, HowToPlayUIState, HowToPlayUIEffect>(HowToPlayUIState()) {

    override fun handleUIEvent(event: HowToPlayUIEvent) {
        when (event) {
            HowToPlayUIEvent.BackClicked -> setEffect(HowToPlayUIEffect.NavigateBack)
        }
    }
}
