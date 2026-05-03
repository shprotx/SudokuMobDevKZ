package ru.shprot.sudokumobdevkz.feature.splash.presentation.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.shprot.sudokumobdevkz.core.base.presentation.viewmodel.BaseViewModel
import ru.shprot.sudokumobdevkz.feature.splash.presentation.contract.SplashUIEffect
import ru.shprot.sudokumobdevkz.feature.splash.presentation.contract.SplashUIEvent
import ru.shprot.sudokumobdevkz.feature.splash.presentation.contract.SplashUIState
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() :
    BaseViewModel<SplashUIEvent, SplashUIState, SplashUIEffect>(SplashUIState()) {

    override fun handleUIEvent(event: SplashUIEvent) {
        when (event) {
            SplashUIEvent.AnimationCompleted -> setEffect(SplashUIEffect.NavigateToMenu)
        }
    }
}
