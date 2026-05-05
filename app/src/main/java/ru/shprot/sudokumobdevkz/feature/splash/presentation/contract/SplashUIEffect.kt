package ru.shprot.sudokumobdevkz.feature.splash.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEffect

sealed interface SplashUIEffect : UIEffect {
    data object NavigateToMenu : SplashUIEffect
}
