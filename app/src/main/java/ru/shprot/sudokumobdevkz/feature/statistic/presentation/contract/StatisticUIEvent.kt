package ru.shprot.sudokumobdevkz.feature.statistic.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEvent

sealed interface StatisticUIEvent : UIEvent {
    data object ShowResetDialog : StatisticUIEvent
    data object DismissResetDialog : StatisticUIEvent
    data object BackClicked : StatisticUIEvent
    data object ResetClicked : StatisticUIEvent

    class TabSelected(val index: Int) : StatisticUIEvent
    class ResetRequested(val difficulty: Int) : StatisticUIEvent
}
