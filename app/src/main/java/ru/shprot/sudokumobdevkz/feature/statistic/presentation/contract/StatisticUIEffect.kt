package ru.shprot.sudokumobdevkz.feature.statistic.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEffect

sealed interface StatisticUIEffect : UIEffect {
    data object NavigateBack : StatisticUIEffect
    data object NavigateToSettings : StatisticUIEffect
    data object NavigateToLeaderboard : StatisticUIEffect
}
