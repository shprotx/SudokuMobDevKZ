package ru.shprot.sudokumobdevkz.core.base.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class LeaderboardBackfillDto(
    val stableId: String,
    val platform: String,
    val displayName: String,
    val avatarUrl: String?,
)