package ru.shprot.sudokumobdevkz.core.base.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class LeaderboardSubmitDto(
    val stableId: String,
    val platform: String,
    val displayName: String,
    val avatarUrl: String?,
    val scoreDelta: Long,
    val gameContext: GameContextDto? = null,
)