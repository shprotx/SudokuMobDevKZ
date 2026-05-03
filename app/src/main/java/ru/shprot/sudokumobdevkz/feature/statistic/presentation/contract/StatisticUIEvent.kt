package ru.shprot.sudokumobdevkz.feature.statistic.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEvent

sealed interface StatisticUIEvent : UIEvent {
    data class TabSelected(val index: Int) : StatisticUIEvent
    data class ResetRequested(val difficulty: Int) : StatisticUIEvent
    data object ShowResetDialog : StatisticUIEvent
    data object DismissResetDialog : StatisticUIEvent
    data object BackClicked : StatisticUIEvent
    data object ResetClicked : StatisticUIEvent
}
