package ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.LeaderboardData
import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIState

data class LeaderboardsUIState(
    val selectedTab: Int = 0,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isSignedIn: Boolean = false,
    val data: LeaderboardData? = null,
    val errorMessageRes: Int? = null,
) : UIState
