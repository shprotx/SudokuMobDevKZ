package ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEffect

sealed interface HowToPlayUIEffect : UIEffect {
    data object NavigateBack : HowToPlayUIEffect
}
