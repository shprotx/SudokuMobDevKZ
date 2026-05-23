package ru.shprot.sudokumobdevkz.core.base.data.cloud.model

data class LeaderboardRow(
    val rank: Long,
    val displayName: String,
    val avatarUrl: String?,
    val rawScore: Long,
    val displayScore: String,
    val isCurrentPlayer: Boolean,
)
