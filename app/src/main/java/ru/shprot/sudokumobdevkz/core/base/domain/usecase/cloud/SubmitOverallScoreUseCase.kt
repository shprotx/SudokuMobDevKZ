package ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud

import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudGameServices
import ru.shprot.sudokumobdevkz.core.base.data.cloud.LeaderboardsConfig
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInState
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.GameHistoryDao
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import javax.inject.Inject

class SubmitOverallScoreUseCase @Inject constructor(
    private val cloud: CloudGameServices,
    private val gameHistoryDao: GameHistoryDao,
) {

    suspend operator fun invoke(): Long? {
        if (!cloud.isAvailable) return null
        if (cloud.signInState.value !is SignInState.SignedIn) return null
        val total = calculateTotal()
        if (total <= 0L) return total
        cloud.submitScore(LeaderboardsConfig.OVERALL_LEADERBOARD_ID, total)
        return total
    }

    suspend fun calculateTotal(): Long {
        val wins = gameHistoryDao.getAllStandardWins()
        return wins.sumOf { entry ->
            val difficulty = Difficulty.fromFirebaseKey(entry.difficulty) ?: return@sumOf 0L
            RatingCalculator.scoreForWin(
                difficulty = difficulty,
                timeSeconds = entry.timeSeconds,
                errors = entry.errors,
                hintsUsed = entry.hintsUsed,
                isDaily = entry.isDaily,
            )
        }
    }
}
