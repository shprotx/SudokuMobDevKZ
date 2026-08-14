package ru.shprot.sudokumobdevkz.core.base.data.cloud.model

import ru.shprot.sudokumobdevkz.core.base.data.remote.LeaderboardEntryDto

object LeaderboardMappers {

    private const val ANONYMOUS = "Anonymous"

    fun LeaderboardData.ownRowOutsideTop(): LeaderboardRow? {
        val score = playerScore ?: return null
        val rank = score.rank ?: return null
        if (topRows.any { it.isCurrentPlayer }) return null
        return LeaderboardRow(
            rank = rank,
            displayName = score.displayName,
            avatarUrl = score.avatarUrl,
            rawScore = score.rawScore,
            displayScore = score.displayScore,
            isCurrentPlayer = true,
            achievementsCount = score.achievementsCount,
        )
    }

    fun LeaderboardEntryDto.toRow(rank: Long, isCurrentPlayer: Boolean): LeaderboardRow =
        LeaderboardRow(
            rank = rank,
            displayName = displayName.ifBlank { ANONYMOUS },
            avatarUrl = avatarUrl,
            rawScore = score,
            displayScore = score.toString(),
            isCurrentPlayer = isCurrentPlayer,
            achievementsCount = achievementsCount,
        )

    fun LeaderboardEntryDto.toPlayerScore(rank: Long): PlayerScore =
        PlayerScore(
            rank = rank,
            rawScore = score,
            displayScore = score.toString(),
            displayName = displayName.ifBlank { ANONYMOUS },
            avatarUrl = avatarUrl,
            achievementsCount = achievementsCount,
        )
}
