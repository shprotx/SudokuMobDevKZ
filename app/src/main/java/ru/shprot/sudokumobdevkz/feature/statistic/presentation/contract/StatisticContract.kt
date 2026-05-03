package ru.shprot.sudokumobdevkz.feature.statistic.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEffect
import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEvent
import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIState
import ru.shprot.sudokumobdevkz.model.database.entity.GameHistoryEntity

data class StatisticUiState(
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
) : UIState

sealed interface StatisticEvent : UIEvent {
    data class TabSelected(val index: Int) : StatisticEvent
    data class ResetRequested(val difficulty: Int) : StatisticEvent
}

sealed interface StatisticEffect : UIEffect
