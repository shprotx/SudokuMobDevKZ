package ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEvent

sealed interface DailyChallengeUIEvent : UIEvent {
    data object PlayClicked : DailyChallengeUIEvent
    data object BackClicked : DailyChallengeUIEvent
    data object PrevMonth : DailyChallengeUIEvent
    data object NextMonth : DailyChallengeUIEvent
    data class DayClicked(val dateKey: String) : DailyChallengeUIEvent
}