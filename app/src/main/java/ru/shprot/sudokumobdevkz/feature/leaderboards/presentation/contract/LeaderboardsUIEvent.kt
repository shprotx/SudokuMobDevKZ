package ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEvent

sealed interface LeaderboardsUIEvent : UIEvent {

    data object BackClicked : LeaderboardsUIEvent

    data object Refresh : LeaderboardsUIEvent

    data object SignInCtaClicked : LeaderboardsUIEvent
}
