package ru.shprot.sudokumobdevkz.core.base.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class LeaderboardEntryDto(
    val platform: String = "android",
    val displayName: String = "Anonymous",
    val avatarUrl: String? = null,
    val score: Long = 0,
    val updatedAt: Long = 0,
)