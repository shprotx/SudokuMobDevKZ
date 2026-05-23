package ru.shprot.sudokumobdevkz.core.base.data.cloud

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInState
import ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud.BackfillAchievementsUseCase
import ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud.SyncToCloudUseCase
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CloudSyncOrchestrator @Inject constructor(
    private val cloud: CloudGameServices,
    private val backfill: BackfillAchievementsUseCase,
    private val backfillTracker: CloudBackfillTracker,
    private val syncToCloud: SyncToCloudUseCase,
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val started = AtomicBoolean(false)

    fun start() {
        if (!started.compareAndSet(false, true)) return
        if (!cloud.isAvailable) return

        scope.launch {
            cloud.signInState.collect { state ->
                if (state is SignInState.SignedIn) {
                    handleSignedIn(state.playerId)
                }
            }
        }
        scope.launch {
            syncToCloud.observeAndSync()
        }
    }

    private suspend fun handleSignedIn(playerId: String) {
        if (backfillTracker.wasBackfilledFor(playerId)) return
        backfill()
        backfillTracker.markBackfilledFor(playerId)
    }
}
