package ru.shprot.sudokumobdevkz.core.base.data.cloud.model

data class PlayerScore(
    val rank: Long?,
    val rawScore: Long,
    val displayScore: String,
    val displayName: String,
    val avatarUrl: String?,
    val achievementsCount: Int? = null,
)
