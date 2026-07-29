package ru.shprot.sudokumobdevkz.core.base.data.cloud.model

object LeaderboardMappers {

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
        )
    }
}
