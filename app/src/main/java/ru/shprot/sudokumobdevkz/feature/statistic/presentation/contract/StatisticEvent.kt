package ru.shprot.sudokumobdevkz.feature.statistic.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEvent

sealed interface StatisticEvent : UIEvent {
    data class TabSelected(val index: Int) : StatisticEvent
    data class ResetRequested(val difficulty: Int) : StatisticEvent
}
