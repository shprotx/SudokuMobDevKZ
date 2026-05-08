package ru.shprot.sudokumobdevkz.feature.statistic.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.domain.model.DailyPlaytime
import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIState

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
    val dailyPlaytimes: List<DailyPlaytime> = emptyList(),
    val percentile: Int = -1,
    val totalPlayers: Int = 0,
    val casualGamesPlayed: String = "0",
    val showResetDialog: Boolean = false,
    val dailyCurrentStreak: Int = 0,
    val dailyBestStreak: Int = 0,
) : UIState