package ru.shprot.sudokumobdevkz.core.base.data.cloud.model

import android.net.Uri

data class LeaderboardRow(
    val rank: Long,
    val displayName: String,
    val avatarUri: Uri?,
    val rawScore: Long,
    val displayScore: String,
    val isCurrentPlayer: Boolean,
)
