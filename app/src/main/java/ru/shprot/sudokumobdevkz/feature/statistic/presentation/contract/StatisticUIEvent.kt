package ru.shprot.sudokumobdevkz.feature.statistic.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEvent

sealed interface StatisticUIEvent : UIEvent {
    data object ShowResetDialog : StatisticUIEvent
    data object DismissResetDialog : StatisticUIEvent
    data object BackClicked : StatisticUIEvent
    data object ResetClicked : StatisticUIEvent
    data object OpenLeaderboardClicked : StatisticUIEvent
    data object SignInCtaClicked : StatisticUIEvent

    class TabSelected(val index: Int) : StatisticUIEvent
    class ResetRequested(val tabIndex: Int) : StatisticUIEvent
}
