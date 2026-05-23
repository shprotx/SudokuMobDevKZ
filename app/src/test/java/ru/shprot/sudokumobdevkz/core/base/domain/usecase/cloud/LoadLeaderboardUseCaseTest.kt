package ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud

import android.app.Activity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudGameServices
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.LeaderboardRow
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.PlayerScore
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInResult
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInState
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty

class LoadLeaderboardUseCaseTest {

    private val sampleRows = listOf(
        LeaderboardRow(1L, "Alice", null, 60L, "1:00", isCurrentPlayer = false),
        LeaderboardRow(2L, "Bob", null, 75L, "1:15", isCurrentPlayer = true),
    )
    private val samplePlayerScore = PlayerScore(rank = 2L, rawScore = 75L, displayScore = "1:15")

    @Test
    fun `returns empty when cloud is not available`() = runTest {
        val cloud = FakeCloud(isAvailable = false, signedIn = false)
        val useCase = LoadLeaderboardUseCase(cloud)

        val data = useCase(Difficulty.EASY)

        assertEquals(Difficulty.EASY, data.difficulty)
        assertTrue(data.topRows.isEmpty())
        assertNull(data.playerScore)
        assertTrue(cloud.calls.isEmpty())
    }

    @Test
    fun `returns empty when signed out`() = runTest {
        val cloud = FakeCloud(isAvailable = true, signedIn = false)
        val useCase = LoadLeaderboardUseCase(cloud)

        val data = useCase(Difficulty.MEDIUM)

        assertTrue(data.topRows.isEmpty())
        assertNull(data.playerScore)
        assertTrue(cloud.calls.isEmpty())
    }

    @Test
    fun `loads top scores and player score for the difficulty leaderboard`() = runTest {
        val cloud = FakeCloud(
            isAvailable = true,
            signedIn = true,
            topRows = sampleRows,
            playerScore = samplePlayerScore,
        )
        val useCase = LoadLeaderboardUseCase(cloud)

        val data = useCase(Difficulty.HARD, limit = 5)

        assertEquals(Difficulty.HARD, data.difficulty)
        assertEquals(sampleRows, data.topRows)
        assertEquals(samplePlayerScore, data.playerScore)
        assertEquals(
            listOf(
                "loadTopScores(${Difficulty.HARD.leaderboardId}, 5)",
                "loadPlayerScore(${Difficulty.HARD.leaderboardId})",
            ),
            cloud.calls,
        )
    }

    @Test
    fun `default limit is 10`() = runTest {
        val cloud = FakeCloud(isAvailable = true, signedIn = true)
        val useCase = LoadLeaderboardUseCase(cloud)

        useCase(Difficulty.EASY)

        assertEquals(
            "loadTopScores(${Difficulty.EASY.leaderboardId}, 10)",
            cloud.calls.first(),
        )
    }

    private class FakeCloud(
        override val isAvailable: Boolean,
        signedIn: Boolean,
        private val topRows: List<LeaderboardRow> = emptyList(),
        private val playerScore: PlayerScore? = null,
    ) : CloudGameServices {

        val calls = mutableListOf<String>()
        private val state = MutableStateFlow<SignInState>(
            if (signedIn) SignInState.SignedIn("p1", "P", null) else SignInState.SignedOut,
        )
        override val signInState: StateFlow<SignInState> = state.asStateFlow()

        override fun attachActivity(activity: Activity) = Unit
        override fun detachActivity() = Unit
        override suspend fun trySilentSignIn(): SignInResult = SignInResult.NotAvailable
        override suspend fun requestSignIn(): SignInResult = SignInResult.NotAvailable
        override suspend fun signOut() = Unit
        override suspend fun unlockAchievement(pgsId: String) = Unit
        override suspend fun incrementAchievement(pgsId: String, steps: Int) = Unit
        override suspend fun submitScore(leaderboardId: String, score: Long) = Unit

        override suspend fun loadTopScores(leaderboardId: String, limit: Int): List<LeaderboardRow> {
            calls += "loadTopScores($leaderboardId, $limit)"
            return topRows
        }

        override suspend fun loadPlayerScore(leaderboardId: String): PlayerScore? {
            calls += "loadPlayerScore($leaderboardId)"
            return playerScore
        }

        override suspend fun readSnapshot(name: String): ByteArray? = null
        override suspend fun writeSnapshot(name: String, bytes: ByteArray, description: String) = Unit
    }
}
