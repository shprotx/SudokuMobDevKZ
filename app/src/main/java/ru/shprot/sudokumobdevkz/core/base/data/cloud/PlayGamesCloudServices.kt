package ru.shprot.sudokumobdevkz.core.base.data.cloud

import android.app.Activity
import com.google.android.gms.games.PlayGames
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.LeaderboardRow
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.PlayerScore
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInResult
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInState
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayGamesCloudServices @Inject constructor() : CloudGameServices {

    override val isAvailable: Boolean = true

    private val _signInState = MutableStateFlow<SignInState>(SignInState.SignedOut)
    override val signInState: StateFlow<SignInState> = _signInState.asStateFlow()

    private var activityRef: WeakReference<Activity>? = null

    override fun attachActivity(activity: Activity) {
        activityRef = WeakReference(activity)
    }

    override fun detachActivity() {
        activityRef = null
    }

    override suspend fun trySilentSignIn(): SignInResult {
        val activity = activityRef?.get()
            ?: return SignInResult.Failure(ACTIVITY_NOT_ATTACHED)
        return runCatching {
            val authenticated = PlayGames.getGamesSignInClient(activity)
                .isAuthenticated
                .await()
                .isAuthenticated
            if (authenticated) {
                refreshPlayerState(activity)
                SignInResult.Success
            } else {
                _signInState.value = SignInState.SignedOut
                SignInResult.Cancelled
            }
        }.getOrElse {
            _signInState.value = SignInState.SignedOut
            SignInResult.Failure(it.message)
        }
    }

    override suspend fun requestSignIn(): SignInResult {
        val activity = activityRef?.get()
            ?: return SignInResult.Failure(ACTIVITY_NOT_ATTACHED)
        return runCatching {
            val authenticated = PlayGames.getGamesSignInClient(activity)
                .signIn()
                .await()
                .isAuthenticated
            if (authenticated) {
                refreshPlayerState(activity)
                SignInResult.Success
            } else {
                SignInResult.Cancelled
            }
        }.getOrElse {
            SignInResult.Failure(it.message)
        }
    }

    override suspend fun signOut() = Unit

    override suspend fun unlockAchievement(pgsId: String) {
        val activity = activityRef?.get() ?: return
        runCatching {
            PlayGames.getAchievementsClient(activity).unlockImmediate(pgsId).await()
        }
    }

    override suspend fun incrementAchievement(pgsId: String, steps: Int) {
        if (steps <= 0) return
        val activity = activityRef?.get() ?: return
        runCatching {
            PlayGames.getAchievementsClient(activity).incrementImmediate(pgsId, steps).await()
        }
    }

    override suspend fun submitScore(leaderboardId: String, score: Long) = Unit

    override suspend fun loadTopScores(leaderboardId: String, limit: Int): List<LeaderboardRow> =
        emptyList()

    override suspend fun loadPlayerScore(leaderboardId: String): PlayerScore? = null

    override suspend fun readSnapshot(name: String): ByteArray? = null

    override suspend fun writeSnapshot(name: String, bytes: ByteArray, description: String) = Unit

    private suspend fun refreshPlayerState(activity: Activity) {
        runCatching {
            val player = PlayGames.getPlayersClient(activity).currentPlayer.await()
            _signInState.value = SignInState.SignedIn(
                playerId = player.playerId,
                displayName = player.displayName.orEmpty(),
                avatarUrl = player.hiResImageUri?.toString() ?: player.iconImageUri?.toString(),
            )
        }
    }

    private companion object {
        const val ACTIVITY_NOT_ATTACHED = "Activity not attached"
    }
}
