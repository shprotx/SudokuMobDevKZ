package ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudGameServices
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudProgressMappers.toDto
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudProgressSerializer
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.CloudProgress
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInState
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.AchievementUnlockedDao
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.DailyChallengeDao
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.SavedGameDao
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.StatisticDao
import javax.inject.Inject
import javax.inject.Singleton

interface SyncToCloudUseCase {

    fun trigger()

    suspend fun syncNow()

    suspend fun observeAndSync()

    companion object {
        const val SNAPSHOT_NAME = "progress_v1"
        const val SNAPSHOT_DESC = "Sudoku progress"
        const val DEBOUNCE_MS = 10_000L
    }
}

@Singleton
class SyncToCloudUseCaseImpl @Inject constructor(
    private val cloud: CloudGameServices,
    private val statisticDao: StatisticDao,
    private val achievementUnlockedDao: AchievementUnlockedDao,
    private val dailyChallengeDao: DailyChallengeDao,
    private val savedGameDao: SavedGameDao,
) : SyncToCloudUseCase {

    private val triggers = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    override fun trigger() {
        triggers.tryEmit(Unit)
    }

    override suspend fun syncNow() {
        performSync()
    }

    override suspend fun observeAndSync() {
        triggers
            .debounce(SyncToCloudUseCase.DEBOUNCE_MS)
            .collect { performSync() }
    }

    private suspend fun performSync() {
        if (!cloud.isAvailable) return
        if (cloud.signInState.value !is SignInState.SignedIn) return

        val progress = collectProgress()
        val bytes = CloudProgressSerializer.encode(progress)
        cloud.writeSnapshot(SyncToCloudUseCase.SNAPSHOT_NAME, bytes, SyncToCloudUseCase.SNAPSHOT_DESC)
    }

    private suspend fun collectProgress(): CloudProgress = CloudProgress(
        schemaVersion = CloudProgress.SCHEMA_VERSION,
        statistics = statisticDao.getAll().associateBy({ it.difficulty }, { it.toDto() }),
        unlockedAchievements = achievementUnlockedDao.getAll().map { it.toDto() },
        dailyChallenges = dailyChallengeDao.getAllCompleted().map { it.toDto() },
        savedGame = savedGameDao.get()?.toDto(),
        lastSyncTimestamp = System.currentTimeMillis(),
    )
}
