package ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEvent

sealed interface HowToPlayUIEvent : UIEvent {
    data object BackClicked : HowToPlayUIEvent
}
