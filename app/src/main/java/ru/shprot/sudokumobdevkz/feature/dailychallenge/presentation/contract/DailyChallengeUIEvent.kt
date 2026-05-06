package ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEvent

sealed interface DailyChallengeUIEvent : UIEvent {
    data object PlayClicked : DailyChallengeUIEvent
    data object BackClicked : DailyChallengeUIEvent
}