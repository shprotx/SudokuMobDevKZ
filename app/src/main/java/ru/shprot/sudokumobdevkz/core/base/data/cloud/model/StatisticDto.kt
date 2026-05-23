package ru.shprot.sudokumobdevkz.core.base.data.cloud.model

import kotlinx.serialization.Serializable

@Serializable
data class StatisticDto(
    val allTime: Long = 0,
    val bestTime: Int = 0,
    val averageTime: Int = 0,
    val gamesStarted: Int = 0,
    val gamesWon: Int = 0,
    val percentOfWins: Int = 0,
    val winsWithoutErrors: Int = 0,
    val bestWinsLine: Int = 0,
    val currentWinsLine: Int = 0,
    val casualGamesPlayed: Int = 0,
)
