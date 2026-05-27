package ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.shprot.sudokumobdevkz.core.base.data.cloud.NoOpCloudGameServices
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.GameHistoryDao
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.GameHistoryEntity
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty

class SubmitOverallScoreUseCaseTest {

    private fun makeUseCase(wins: List<GameHistoryEntity>): SubmitOverallScoreUseCase {
        return SubmitOverallScoreUseCase(
            cloud = NoOpCloudGameServices(),
            gameHistoryDao = FakeGameHistoryDao(wins),
        )
    }

    private fun standardWin(difficulty: Difficulty, timeSeconds: Int = 600): GameHistoryEntity =
        GameHistoryEntity(
            difficulty = difficulty.firebaseKey,
            timeSeconds = timeSeconds,
            errors = 0,
            isWin = true,
            isStandardMode = true,
        )

    private fun casualWin(difficulty: Difficulty, timeSeconds: Int = 600): GameHistoryEntity =
        GameHistoryEntity(
            difficulty = difficulty.firebaseKey,
            timeSeconds = timeSeconds,
            errors = 0,
            isWin = true,
            isStandardMode = false,
        )

    @Test
    fun `calculateTotal returns zero for empty history`() = runTest {
        val useCase = makeUseCase(emptyList())
        assertEquals(0L, useCase.calculateTotal())
    }

    @Test
    fun `calculateTotal sums only standard wins from mixed history`() = runTest {
        val useCase = makeUseCase(
            listOf(
                standardWin(Difficulty.EASY),
                casualWin(Difficulty.HARD),
                standardWin(Difficulty.MEDIUM),
            ),
        )
        val expected = RatingCalculator.scoreForWin(Difficulty.EASY, 600, 0) +
            RatingCalculator.scoreForWin(Difficulty.MEDIUM, 600, 0)
        assertEquals(expected, useCase.calculateTotal())
    }

    @Test
    fun `calculateTotal returns zero when all wins are casual`() = runTest {
        val useCase = makeUseCase(
            listOf(
                casualWin(Difficulty.EASY),
                casualWin(Difficulty.HARD),
            ),
        )
        assertEquals(0L, useCase.calculateTotal())
    }
}

private class FakeGameHistoryDao(private val standardWins: List<GameHistoryEntity>) : GameHistoryDao {
    override suspend fun insert(entry: GameHistoryEntity) = Unit
    override fun getRecentGames(difficulty: Int, limit: Int): Flow<List<GameHistoryEntity>> =
        MutableStateFlow(emptyList())
    override fun observeSince(sinceMs: Long): Flow<List<GameHistoryEntity>> =
        MutableStateFlow(emptyList())
    override fun observeRecentWins(limit: Int): Flow<List<GameHistoryEntity>> =
        MutableStateFlow(emptyList())
    override suspend fun getRecentWins(limit: Int): List<GameHistoryEntity> = emptyList()
    override suspend fun getAllWins(): List<GameHistoryEntity> = standardWins
    override suspend fun getAllStandardWins(): List<GameHistoryEntity> =
        standardWins.filter { it.isStandardMode }
    override suspend fun deleteByDifficulty(difficulty: Int) = Unit
}