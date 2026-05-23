package ru.shprot.sudokumobdevkz.core.base.data.cloud

import android.app.Activity
import kotlinx.coroutines.flow.StateFlow
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.LeaderboardRow
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.PlayerScore
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInResult
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInState

interface CloudGameServices {

    val isAvailable: Boolean

    val signInState: StateFlow<SignInState>

    fun attachActivity(activity: Activity)

    fun detachActivity()

    suspend fun trySilentSignIn(): SignInResult

    suspend fun requestSignIn(): SignInResult

    suspend fun signOut()

    suspend fun unlockAchievement(pgsId: String)

    suspend fun incrementAchievement(pgsId: String, steps: Int)

    suspend fun submitScore(leaderboardId: String, score: Long)

    suspend fun loadTopScores(leaderboardId: String, limit: Int = 10): List<LeaderboardRow>

    suspend fun loadPlayerScore(leaderboardId: String): PlayerScore?

    suspend fun readSnapshot(name: String): ByteArray?

    suspend fun writeSnapshot(name: String, bytes: ByteArray, description: String)
}
