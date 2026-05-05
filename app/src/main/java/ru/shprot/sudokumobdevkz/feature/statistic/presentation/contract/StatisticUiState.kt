package ru.shprot.sudokumobdevkz.feature.statistic.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIState
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.GameHistoryEntity

data class StatisticUIState(
    val selectedTab: Int = 0,
    val bestTime: String = "--:--",
    val averageTime: String = "--:--",
    val percentOfWins: String = "0%",
    val winsWithoutErrors: String = "0",
    val gamesStarted: String = "0",
    val gamesWon: String = "0",
    val bestWinsLine: String = "0",
    val currentWinsLine: String = "0",
    val recentGames: List<GameHistoryEntity> = emptyList(),
    val percentile: Int = -1,
    val totalPlayers: Int = 0,
    val casualGamesPlayed: String = "0",
    val showResetDialog: Boolean = false,
) : UIState
