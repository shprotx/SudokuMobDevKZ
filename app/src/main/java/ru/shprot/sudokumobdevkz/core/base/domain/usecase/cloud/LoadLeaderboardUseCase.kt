package ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud

import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudGameServices
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.LeaderboardData
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInState
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import javax.inject.Inject

class LoadLeaderboardUseCase @Inject constructor(
    private val cloud: CloudGameServices,
) {

    suspend operator fun invoke(
        difficulty: Difficulty,
        limit: Int = DEFAULT_LIMIT,
    ): LeaderboardData {
        if (!cloud.isAvailable || cloud.signInState.value !is SignInState.SignedIn) {
            return LeaderboardData(
                difficulty = difficulty,
                topRows = emptyList(),
                playerScore = null,
            )
        }
        val id = difficulty.leaderboardId
        val topRows = cloud.loadTopScores(id, limit)
        val playerScore = cloud.loadPlayerScore(id)
        return LeaderboardData(
            difficulty = difficulty,
            topRows = topRows,
            playerScore = playerScore,
        )
    }

    companion object {
        const val DEFAULT_LIMIT = 10
    }
}
