package ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud

import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudGameServices
import ru.shprot.sudokumobdevkz.core.base.data.cloud.LeaderboardsConfig
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.LeaderboardData
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInState
import javax.inject.Inject

class LoadLeaderboardUseCase @Inject constructor(
    private val cloud: CloudGameServices,
) {

    suspend operator fun invoke(limit: Int = DEFAULT_LIMIT): LeaderboardData {
        if (!cloud.isAvailable || cloud.signInState.value !is SignInState.SignedIn) {
            return LeaderboardData(emptyList(), null)
        }
        val id = LeaderboardsConfig.OVERALL_LEADERBOARD_ID
        val topRows = cloud.loadTopScores(id, limit)
        val playerScore = cloud.loadPlayerScore(id)
        return LeaderboardData(topRows = topRows, playerScore = playerScore)
    }

    companion object {
        const val DEFAULT_LIMIT = 10
    }
}
