package ru.shprot.sudokumobdevkz.core.base.data.cloud

import android.app.Activity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.LeaderboardRow
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.PlayerScore
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInResult
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoOpCloudGameServices @Inject constructor() : CloudGameServices {

    override val isAvailable: Boolean = false

    private val _signInState = MutableStateFlow<SignInState>(SignInState.NotAvailable)
    override val signInState: StateFlow<SignInState> = _signInState.asStateFlow()

    override fun attachActivity(activity: Activity) = Unit

    override fun detachActivity() = Unit

    override suspend fun trySilentSignIn(): SignInResult = SignInResult.NotAvailable

    override suspend fun requestSignIn(): SignInResult = SignInResult.NotAvailable

    override suspend fun signOut() = Unit

    override suspend fun unlockAchievement(pgsId: String) = Unit

    override suspend fun incrementAchievement(pgsId: String, steps: Int) = Unit

    override suspend fun submitScore(leaderboardId: String, score: Long) = Unit

    override suspend fun loadTopScores(leaderboardId: String, limit: Int): List<LeaderboardRow> =
        emptyList()

    override suspend fun loadPlayerScore(leaderboardId: String): PlayerScore? = null

    override suspend fun readSnapshot(name: String): ByteArray? = null

    override suspend fun writeSnapshot(name: String, bytes: ByteArray, description: String) = Unit
}
