package ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud

import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.LeaderboardData
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.LeaderboardRow
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.PlayerScore
import ru.shprot.sudokumobdevkz.core.base.data.remote.FirebaseApi
import ru.shprot.sudokumobdevkz.core.base.data.repository.StableIdProvider
import javax.inject.Inject

class LoadLeaderboardUseCase @Inject constructor(
    private val firebaseApi: FirebaseApi,
    private val stableIdProvider: StableIdProvider,
) {

    suspend operator fun invoke(limit: Int = DEFAULT_LIMIT): LeaderboardData {
        val raw = runCatching { firebaseApi.getLeaderboard() }.getOrNull()
            ?: return LeaderboardData(emptyList(), null)

        val selfId = stableIdProvider.current()

        val sorted = raw.entries
            .sortedByDescending { it.value.score }
            .take(limit)

        val topRows = sorted.mapIndexed { index, (id, entry) ->
            LeaderboardRow(
                rank = (index + 1).toLong(),
                displayName = entry.displayName.ifBlank { ANONYMOUS },
                avatarUrl = entry.avatarUrl,
                rawScore = entry.score,
                displayScore = entry.score.toString(),
                isCurrentPlayer = id == selfId,
            )
        }

        val allSorted = raw.entries.sortedByDescending { it.value.score }
        val playerIndex = allSorted.indexOfFirst { it.key == selfId }
        val playerScore = if (playerIndex >= 0) {
            val entry = allSorted[playerIndex].value
            PlayerScore(
                rank = (playerIndex + 1).toLong(),
                rawScore = entry.score,
                displayScore = entry.score.toString(),
            )
        } else {
            null
        }

        return LeaderboardData(topRows = topRows, playerScore = playerScore)
    }

    private companion object {
        const val DEFAULT_LIMIT = 10
        const val ANONYMOUS = "Anonymous"
    }
}