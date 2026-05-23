package ru.shprot.sudokumobdevkz.core.base.data.cloud.model

import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty

data class LeaderboardData(
    val difficulty: Difficulty,
    val topRows: List<LeaderboardRow>,
    val playerScore: PlayerScore?,
)
