package ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud

import android.app.Activity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudGameServices
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudProgressSerializer
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.CloudProgress
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.LeaderboardRow
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.PlayerScore
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInResult
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInState
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.StatisticDto
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.UnlockedAchievementDto
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.AchievementUnlockedDao
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.CustomThemeDao
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.DailyChallengeDao
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.SavedGameDao
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.StatisticDao
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.AchievementUnlockedEntity
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.CustomThemeEntity
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.DailyChallengeEntity
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.SavedGameEntity
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.StatisticEntity

@OptIn(ExperimentalCoroutinesApi::class)
class AutoImportSnapshotUseCaseTest {

    private lateinit var cloud: FakeCloud
    private lateinit var syncToCloud: TrackingSyncToCloudUseCase
    private lateinit var statisticDao: SnapshotStatisticDao
    private lateinit var dailyDao: SnapshotDailyChallengeDao
    private lateinit var achievementsDao: SnapshotAchievementUnlockedDao
    private lateinit var savedGameDao: SnapshotSavedGameDao
    private lateinit var importUseCase: ImportFromCloudUseCase
    private lateinit var useCase: AutoImportSnapshotUseCase

    @Before
    fun setUp() {
        cloud = FakeCloud()
        syncToCloud = TrackingSyncToCloudUseCase()
        statisticDao = SnapshotStatisticDao()
        dailyDao = SnapshotDailyChallengeDao()
        achievementsDao = SnapshotAchievementUnlockedDao()
        savedGameDao = SnapshotSavedGameDao()
        importUseCase = ImportFromCloudUseCase(
            cloud = cloud,
            statisticDao = statisticDao,
            achievementUnlockedDao = achievementsDao,
            dailyChallengeDao = dailyDao,
            savedGameDao = savedGameDao,
            customThemeDao = NoOpCustomThemeDao(),
            syncToCloud = syncToCloud,
        )
        useCase = AutoImportSnapshotUseCase(importUseCase)
    }

    @Test
    fun importsCloud_whenLocalEmpty_andCloudHasData() = runTest {
        cloud.storeSnapshot(NON_EMPTY_PROGRESS)

        val result = useCase()

        assertEquals(AutoImportSnapshotUseCase.Result.IMPORTED, result)
        assertEquals(1, statisticDao.getAll().size)
        assertEquals(1, achievementsDao.getAll().size)
    }

    @Test
    fun skips_whenCloudSnapshotMissing() = runTest {
        val result = useCase()

        assertEquals(AutoImportSnapshotUseCase.Result.NO_CLOUD_SNAPSHOT, result)
        assertTrue(statisticDao.getAll().isEmpty())
    }

    @Test
    fun skips_whenCloudSnapshotIsEmpty() = runTest {
        cloud.storeSnapshot(CloudProgress())

        val result = useCase()

        assertEquals(AutoImportSnapshotUseCase.Result.CLOUD_EMPTY, result)
        assertTrue(statisticDao.getAll().isEmpty())
    }

    @Test
    fun skips_whenLocalNotEmpty() = runTest {
        cloud.storeSnapshot(NON_EMPTY_PROGRESS)
        statisticDao.seed(StatisticEntity(difficulty = 0, gamesWon = 5))

        val result = useCase()

        assertEquals(AutoImportSnapshotUseCase.Result.LOCAL_NOT_EMPTY, result)
        assertEquals(1, statisticDao.getAll().size)
        assertEquals(5, statisticDao.getAll().single().gamesWon)
        assertTrue(achievementsDao.getAll().isEmpty())
    }

    @Test
    fun returnsError_whenCloudThrows() = runTest {
        cloud.failOnRead = true

        val result = useCase()

        assertEquals(AutoImportSnapshotUseCase.Result.ERROR, result)
    }

    @Test
    fun importBlock_wrappedWithImportFlag() = runTest {
        cloud.storeSnapshot(NON_EMPTY_PROGRESS)

        useCase()

        assertEquals(1, syncToCloud.beginCount)
        assertEquals(1, syncToCloud.endCount)
        assertFalse(syncToCloud.isImporting)
    }

    @Test
    fun importBlock_releasesFlag_whenDaoThrows() = runTest {
        cloud.storeSnapshot(NON_EMPTY_PROGRESS)
        statisticDao.failOnUpsert = true

        useCase()

        assertEquals(1, syncToCloud.beginCount)
        assertEquals(1, syncToCloud.endCount)
        assertFalse(syncToCloud.isImporting)
    }

    private companion object {
        val NON_EMPTY_PROGRESS = CloudProgress(
            statistics = mapOf(0 to StatisticDto(gamesWon = 7, bestTime = 120)),
            unlockedAchievements = listOf(UnlockedAchievementDto(id = "wins_first", unlockedAt = 1L)),
        )
    }
}

internal class FakeCloud : CloudGameServices {
    override val isAvailable: Boolean = true
    private val _signInState = MutableStateFlow<SignInState>(SignInState.SignedIn("p1", "name", null))
    override val signInState: StateFlow<SignInState> = _signInState.asStateFlow()

    private var snapshotBytes: ByteArray? = null
    var failOnRead: Boolean = false

    fun storeSnapshot(progress: CloudProgress) {
        snapshotBytes = CloudProgressSerializer.encode(progress)
    }

    override fun attachActivity(activity: Activity) = Unit
    override fun detachActivity() = Unit
    override suspend fun trySilentSignIn(): SignInResult = SignInResult.Success
    override suspend fun requestSignIn(): SignInResult = SignInResult.Success
    override suspend fun signOut() = Unit
    override suspend fun unlockAchievement(pgsId: String) = Unit
    override suspend fun incrementAchievement(pgsId: String, steps: Int) = Unit
    override suspend fun submitScore(leaderboardId: String, score: Long) = Unit
    override suspend fun loadTopScores(leaderboardId: String, limit: Int): List<LeaderboardRow> = emptyList()
    override suspend fun loadPlayerScore(leaderboardId: String): PlayerScore? = null
    override suspend fun readSnapshot(name: String): ByteArray? {
        if (failOnRead) error("snapshot read failed")
        return snapshotBytes
    }
    override suspend fun writeSnapshot(name: String, bytes: ByteArray, description: String) {
        snapshotBytes = bytes
    }
}

internal class TrackingSyncToCloudUseCase : SyncToCloudUseCase {
    var beginCount: Int = 0
    var endCount: Int = 0
    val isImporting: Boolean get() = beginCount > endCount

    override fun trigger() = Unit
    override suspend fun syncNow() = Unit
    override suspend fun observeAndSync() = Unit
    override fun beginImport() { beginCount++ }
    override fun endImport() { endCount++ }
}

internal class SnapshotStatisticDao : StatisticDao {
    private val storage = mutableListOf<StatisticEntity>()
    var failOnUpsert: Boolean = false

    fun seed(entity: StatisticEntity) {
        storage.add(entity)
    }

    override suspend fun upsert(statistic: StatisticEntity) {
        if (failOnUpsert) error("upsert failed")
        storage.removeAll { it.difficulty == statistic.difficulty }
        storage.add(statistic)
    }

    override suspend fun getAll(): List<StatisticEntity> = storage.toList()
    override fun observeAll(): Flow<List<StatisticEntity>> = MutableStateFlow(storage.toList())
    override suspend fun getByDifficulty(difficulty: Int): StatisticEntity? = error("unused")
    override fun observeByDifficulty(difficulty: Int): Flow<StatisticEntity?> = error("unused")
    override suspend fun deleteByDifficulty(difficulty: Int) = error("unused")
}

internal class SnapshotDailyChallengeDao : DailyChallengeDao {
    private val storage = mutableListOf<DailyChallengeEntity>()

    override suspend fun upsert(entity: DailyChallengeEntity) {
        storage.removeAll { it.dateKey == entity.dateKey }
        storage.add(entity)
    }

    override suspend fun getAllCompleted(): List<DailyChallengeEntity> = storage.filter { it.isCompleted }
    override fun observeAllCompleted(): Flow<List<DailyChallengeEntity>> = MutableStateFlow(storage.toList())
    override suspend fun getByDate(dateKey: String): DailyChallengeEntity? = storage.firstOrNull { it.dateKey == dateKey }
    override suspend fun getRecentCompleted(limit: Int): List<DailyChallengeEntity> = error("unused")
    override suspend fun getAllCompletedAsc(): List<DailyChallengeEntity> = error("unused")
}

internal class SnapshotAchievementUnlockedDao : AchievementUnlockedDao {
    private val storage = mutableListOf<AchievementUnlockedEntity>()

    override fun observeAll(): Flow<List<AchievementUnlockedEntity>> = MutableStateFlow(storage.toList())
    override suspend fun getAll(): List<AchievementUnlockedEntity> = storage.toList()
    override suspend fun existsById(id: String): Boolean = storage.any { it.id == id }
    override suspend fun insert(entity: AchievementUnlockedEntity) {
        if (storage.none { it.id == entity.id }) storage.add(entity)
    }
}

internal class SnapshotSavedGameDao : SavedGameDao {
    private var current: SavedGameEntity? = null

    override suspend fun save(game: SavedGameEntity) { current = game }
    override suspend fun get(): SavedGameEntity? = current
    override suspend fun delete() { current = null }
}

internal class NoOpCustomThemeDao : CustomThemeDao {
    override fun observeAll(): Flow<List<CustomThemeEntity>> = MutableStateFlow(emptyList())
    override suspend fun getAll(): List<CustomThemeEntity> = emptyList()
    override suspend fun getById(id: String): CustomThemeEntity? = null
    override suspend fun upsert(entity: CustomThemeEntity) = Unit
    override suspend fun deleteById(id: String) = Unit
    override suspend fun exists(id: String): Int = 0
}
