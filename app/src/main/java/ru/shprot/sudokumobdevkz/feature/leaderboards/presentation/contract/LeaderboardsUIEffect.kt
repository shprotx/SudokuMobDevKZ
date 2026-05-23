package ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEffect

sealed interface LeaderboardsUIEffect : UIEffect {

    data object NavigateBack : LeaderboardsUIEffect

    data object NavigateToSettings : LeaderboardsUIEffect
}
