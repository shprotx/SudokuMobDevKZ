package ru.shprot.sudokumobdevkz.core.base.data.cloud.model

data class LeaderboardData(
    val topRows: List<LeaderboardRow>,
    val playerScore: PlayerScore?,
)
