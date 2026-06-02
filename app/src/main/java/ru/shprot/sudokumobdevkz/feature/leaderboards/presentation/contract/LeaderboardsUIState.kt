package ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.LeaderboardData
import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIState

data class LeaderboardsUIState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isSignedIn: Boolean = false,
    val data: LeaderboardData? = null,
    val errorMessageRes: Int? = null,
    val showNameConsentPrompt: Boolean = false,
) : UIState