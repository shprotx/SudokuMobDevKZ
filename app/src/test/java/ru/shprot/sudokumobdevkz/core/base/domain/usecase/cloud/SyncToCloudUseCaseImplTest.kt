package ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud

import android.app.Activity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudGameServices
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.LeaderboardRow
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.PlayerScore
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInResult
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInState
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
import ru.shprot.sudokumobdevkz.core.base.data.repository.IVisitStreakRepository
import ru.shprot.sudokumobdevkz.core.base.domain.model.VisitStreak

@OptIn(ExperimentalCoroutinesApi::class)
class SyncToCloudUseCaseImplTest {

    private lateinit var cloud: WritableFakeCloud
    private lateinit var useCase: SyncToCloudUseCaseImpl

    @Before
    fun setUp() {
        cloud = WritableFakeCloud()
        useCase = SyncToCloudUseCaseImpl(
            cloud = cloud,
            statisticDao = EmptyStatisticDao(),
            achievementUnlockedDao = EmptyAchievementUnlockedDao(),
            dailyChallengeDao = EmptyDailyChallengeDao(),
            savedGameDao = EmptySavedGameDao(),
            customThemeDao = EmptyCustomThemeDao(),
            visitStreakRepository = EmptyVisitStreakRepository(),
        )
    }

    @Test
    fun syncNow_writesSnapshot_whenNotImporting() = runTest {
        useCase.syncNow()
        assertNotNull(cloud.lastBytes)
    }

    @Test
    fun syncNow_isNoOp_whileImportInProgress() = runTest {
        useCase.beginImport()
        useCase.syncNow()
        assertNull(cloud.lastBytes)
    }

    @Test
    fun syncNow_resumes_afterEndImport() = runTest {
        useCase.beginImport()
        useCase.syncNow()
        assertNull(cloud.lastBytes)

        useCase.endImport()
        useCase.syncNow()
        assertNotNull(cloud.lastBytes)
    }

    @Test
    fun withImport_extension_isolatesPerformSync() = runTest {
        useCase.withImport {
            useCase.syncNow()
            assertNull(cloud.lastBytes)
        }
        useCase.syncNow()
        assertNotNull(cloud.lastBytes)
    }

    @Test
    fun withImport_releasesFlag_evenWhenBlockThrows() = runTest {
        val error = runCatching {
            useCase.withImport<Unit> { throw IllegalStateException("boom") }
        }
        assertEquals(IllegalStateException::class, error.exceptionOrNull()!!::class)

        useCase.syncNow()
        assertNotNull(cloud.lastBytes)
    }
}

internal class WritableFakeCloud : CloudGameServices {
    override val isAvailable: Boolean = true
    private val _signInState = MutableStateFlow<SignInState>(SignInState.SignedIn("p1", "name", null))
    override val signInState: StateFlow<SignInState> = _signInState.asStateFlow()
    var lastBytes: ByteArray? = null

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
    override suspend fun readSnapshot(name: String): ByteArray? = lastBytes
    override suspend fun writeSnapshot(name: String, bytes: ByteArray, description: String) {
        lastBytes = bytes
    }
}

internal class EmptyStatisticDao : StatisticDao {
    override suspend fun upsert(statistic: StatisticEntity) = Unit
    override suspend fun getByDifficulty(difficulty: Int): StatisticEntity? = null
    override fun observeByDifficulty(difficulty: Int): Flow<StatisticEntity?> = MutableStateFlow(null)
    override fun observeAll(): Flow<List<StatisticEntity>> = MutableStateFlow(emptyList())
    override suspend fun getAll(): List<StatisticEntity> = emptyList()
    override suspend fun deleteByDifficulty(difficulty: Int) = Unit
}

internal class EmptyAchievementUnlockedDao : AchievementUnlockedDao {
    override fun observeAll(): Flow<List<AchievementUnlockedEntity>> = MutableStateFlow(emptyList())
    override suspend fun getAll(): List<AchievementUnlockedEntity> = emptyList()
    override suspend fun existsById(id: String): Boolean = false
    override suspend fun insert(entity: AchievementUnlockedEntity) = Unit
}

internal class EmptyDailyChallengeDao : DailyChallengeDao {
    override suspend fun getByDate(dateKey: String): DailyChallengeEntity? = null
    override suspend fun upsert(entity: DailyChallengeEntity) = Unit
    override suspend fun getRecentCompleted(limit: Int): List<DailyChallengeEntity> = emptyList()
    override suspend fun getAllCompletedAsc(): List<DailyChallengeEntity> = emptyList()
    override fun observeAllCompleted(): Flow<List<DailyChallengeEntity>> = MutableStateFlow(emptyList())
    override suspend fun getAllCompleted(): List<DailyChallengeEntity> = emptyList()
}

internal class EmptySavedGameDao : SavedGameDao {
    override suspend fun save(game: SavedGameEntity) = Unit
    override suspend fun get(): SavedGameEntity? = null
    override suspend fun delete() = Unit
}

internal class EmptyCustomThemeDao : CustomThemeDao {
    override fun observeAll(): Flow<List<CustomThemeEntity>> = MutableStateFlow(emptyList())
    override suspend fun getAll(): List<CustomThemeEntity> = emptyList()
    override suspend fun getById(id: String): CustomThemeEntity? = null
    override suspend fun upsert(entity: CustomThemeEntity) = Unit
    override suspend fun deleteById(id: String) = Unit
    override suspend fun exists(id: String): Int = 0
}

internal class EmptyVisitStreakRepository : IVisitStreakRepository {
    override val streak: Flow<VisitStreak> = MutableStateFlow(VisitStreak())
    override val currentStreak: VisitStreak = VisitStreak()
    override suspend fun recordVisit(): VisitStreak = currentStreak

    override suspend fun mergeFromCloud(
        cloudCurrentStreak: Int,
        cloudBestStreak: Int,
        cloudLastVisitDate: String?,
    ): Unit = Unit
}
