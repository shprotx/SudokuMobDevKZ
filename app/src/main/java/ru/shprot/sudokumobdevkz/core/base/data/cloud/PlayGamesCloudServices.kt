package ru.shprot.sudokumobdevkz.core.base.data.cloud

import android.app.Activity
import com.google.android.gms.games.PlayGames
import com.google.android.gms.games.SnapshotsClient
import com.google.android.gms.games.leaderboard.LeaderboardVariant
import com.google.android.gms.games.snapshot.SnapshotMetadataChange
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

    override suspend fun submitScore(leaderboardId: String, score: Long) {
        val activity = activityRef?.get() ?: return
        runCatching {
            PlayGames.getLeaderboardsClient(activity).submitScore(leaderboardId, score)
        }
    }

    override suspend fun loadTopScores(leaderboardId: String, limit: Int): List<LeaderboardRow> {
        val activity = activityRef?.get() ?: return emptyList()
        return runCatching {
            val currentPlayerId = (_signInState.value as? SignInState.SignedIn)?.playerId
            val data = PlayGames.getLeaderboardsClient(activity)
                .loadTopScores(
                    leaderboardId,
                    LeaderboardVariant.TIME_SPAN_ALL_TIME,
                    LeaderboardVariant.COLLECTION_PUBLIC,
                    limit,
                )
                .await()
                .get()
                ?: return emptyList()
            val buffer = data.scores ?: return emptyList()
            val rows = buffer.map { score ->
                LeaderboardRow(
                    rank = score.rank,
                    displayName = score.scoreHolderDisplayName.orEmpty(),
                    avatarUrl = pickAvatarUrl(
                        score.scoreHolder?.hiResImageUrl,
                        score.scoreHolder?.iconImageUrl,
                        score.scoreHolderHiResImageUri,
                        score.scoreHolderIconImageUri,
                    ),
                    rawScore = score.rawScore,
                    displayScore = score.displayScore.orEmpty(),
                    isCurrentPlayer = score.scoreHolder?.playerId == currentPlayerId,
                )
            }.toList()
            buffer.release()
            rows
        }.getOrElse { emptyList() }
    }

    override suspend fun loadPlayerScore(leaderboardId: String): PlayerScore? {
        val activity = activityRef?.get() ?: return null
        return runCatching {
            val score = PlayGames.getLeaderboardsClient(activity)
                .loadCurrentPlayerLeaderboardScore(
                    leaderboardId,
                    LeaderboardVariant.TIME_SPAN_ALL_TIME,
                    LeaderboardVariant.COLLECTION_PUBLIC,
                )
                .await()
                .get()
            score?.let {
                PlayerScore(
                    rank = it.rank,
                    rawScore = it.rawScore,
                    displayScore = it.displayScore.orEmpty(),
                )
            }
        }.getOrNull()
    }

    override suspend fun readSnapshot(name: String): ByteArray? {
        val activity = activityRef?.get() ?: return null
        return runCatching {
            val client = PlayGames.getSnapshotsClient(activity)
            val result = client.open(
                name,
                true,
                SnapshotsClient.RESOLUTION_POLICY_MOST_RECENTLY_MODIFIED,
            ).await()
            if (result.isConflict) {
                val conflict = result.conflict ?: return@runCatching null
                val winning = conflict.snapshot
                val bytes = winning.snapshotContents.readFully()
                client.resolveConflict(conflict.conflictId, winning).await()
                bytes
            } else {
                val snapshot = result.data ?: return@runCatching null
                val bytes = snapshot.snapshotContents.readFully()
                client.discardAndClose(snapshot)
                bytes
            }
        }.getOrNull()
    }

    override suspend fun writeSnapshot(name: String, bytes: ByteArray, description: String) {
        val activity = activityRef?.get() ?: return
        runCatching {
            val client = PlayGames.getSnapshotsClient(activity)
            val result = client.open(
                name,
                true,
                SnapshotsClient.RESOLUTION_POLICY_MOST_RECENTLY_MODIFIED,
            ).await()
            val metadata = SnapshotMetadataChange.Builder()
                .setDescription(description)
                .build()
            if (result.isConflict) {
                val conflict = result.conflict ?: return@runCatching
                conflict.resolutionSnapshotContents.writeBytes(bytes)
                client.resolveConflict(
                    conflict.conflictId,
                    conflict.conflictId,
                    metadata,
                    conflict.resolutionSnapshotContents,
                ).await()
            } else {
                val snapshot = result.data ?: return@runCatching
                snapshot.snapshotContents.writeBytes(bytes)
                client.commitAndClose(snapshot, metadata).await()
            }
        }
    }

    private suspend fun refreshPlayerState(activity: Activity) {
        runCatching {
            val player = PlayGames.getPlayersClient(activity).currentPlayer.await()
            _signInState.value = SignInState.SignedIn(
                playerId = player.playerId,
                displayName = player.displayName.orEmpty(),
                avatarUrl = pickAvatarUrl(
                    player.hiResImageUrl,
                    player.iconImageUrl,
                    player.hiResImageUri,
                    player.iconImageUri,
                ),
            )
        }
    }

    private fun pickAvatarUrl(
        hiResUrl: String?,
        iconUrl: String?,
        hiResUri: android.net.Uri?,
        iconUri: android.net.Uri?,
    ): String? {
        val candidates = listOf(hiResUrl, iconUrl, hiResUri?.toString(), iconUri?.toString())
        return candidates.firstOrNull { it != null && it.startsWith("http") }
            ?: candidates.firstOrNull { it != null }
    }

    private companion object {
        const val ACTIVITY_NOT_ATTACHED = "Activity not attached"
    }
}
