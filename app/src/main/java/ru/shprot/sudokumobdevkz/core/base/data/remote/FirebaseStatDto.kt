package ru.shprot.sudokumobdevkz.core.base.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class FirebaseStatDto(
    val averageTime: Int = 0,
    val bestTime: Int = 0,
    val gamesWon: Int = 0,
    val gamesStarted: Int = 0,
    val winsWithoutErrors: Int = 0,
    val bestWinsLine: Int = 0,
)
