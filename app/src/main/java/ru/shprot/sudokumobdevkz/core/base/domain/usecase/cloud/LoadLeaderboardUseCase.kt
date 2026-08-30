package ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud

import ru.shprot.sudokumobdevkz.core.base.data.cloud.LeaderboardKey
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.LeaderboardData
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.LeaderboardMappers.toPlayerScore
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.LeaderboardMappers.toRow
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

        val selfHash = LeaderboardKey.hash(stableIdProvider.current())

        val sorted = raw.entries
            .sortedByDescending { it.value.score }
            .take(limit)

        val topRows = sorted.mapIndexed { index, (id, entry) ->
            entry.toRow(rank = (index + 1).toLong(), isCurrentPlayer = id == selfHash)
        }

        val allSorted = raw.entries.sortedByDescending { it.value.score }
        val playerIndex = allSorted.indexOfFirst { it.key == selfHash }
        val playerScore = if (playerIndex >= 0) {
            allSorted[playerIndex].value.toPlayerScore(rank = (playerIndex + 1).toLong())
        } else {
            null
        }

        return LeaderboardData(topRows = topRows, playerScore = playerScore)
    }

    private companion object {
        const val DEFAULT_LIMIT = 10
    }
}