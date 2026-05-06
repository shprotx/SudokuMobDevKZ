package ru.shprot.sudokumobdevkz.core.base.data.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.AchievementUnlockedDao
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.DailyChallengeDao
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.GameHistoryDao
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.StatisticDao
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.AchievementUnlockedEntity
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.DailyChallengeEntity
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.GameHistoryEntity
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.StatisticEntity
import ru.shprot.sudokumobdevkz.core.base.domain.achievement.UnlockedAchievement

@OptIn(ExperimentalCoroutinesApi::class)
class AchievementsRepositoryTest {

    private lateinit var statisticDao: FakeStatisticDao
    private lateinit var dailyDao: FakeDailyChallengeDao
    private lateinit var gameHistoryDao: FakeGameHistoryDao
    private lateinit var achievementsDao: FakeAchievementUnlockedDao
    private lateinit var repository: AchievementsRepositoryImpl

    @Before
    fun setUp() {
        statisticDao = FakeStatisticDao()
        dailyDao = FakeDailyChallengeDao()
        gameHistoryDao = FakeGameHistoryDao()
        achievementsDao = FakeAchievementUnlockedDao()
        repository = AchievementsRepositoryImpl(
            statisticDao = statisticDao,
            dailyChallengeDao = dailyDao,
            gameHistoryDao = gameHistoryDao,
            achievementUnlockedDao = achievementsDao,
        )
    }

    @Test
    fun checkAndUnlock_returnsEmpty_whenNoStats() = runTest {
        val result = repository.checkAndUnlock()
        assertEquals(emptyList<UnlockedAchievement>(), result)
    }

    @Test
    fun checkAndUnlock_unlocksWinsFirst_afterOneWin() = runTest {
        statisticDao.seed(listOf(StatisticEntity(difficulty = 0, gamesWon = 1)))
        val result = repository.checkAndUnlock()
        assertTrue(result.any { it.achievement.id == "wins_first" })
        assertEquals(1, achievementsDao.allInserted().count { it.id == "wins_first" })
    }

    @Test
    fun checkAndUnlock_isIdempotent() = runTest {
        statisticDao.seed(listOf(StatisticEntity(difficulty = 0, gamesWon = 1)))
        repository.checkAndUnlock()
        val secondRun = repository.checkAndUnlock()
        assertEquals(emptyList<UnlockedAchievement>(), secondRun)
    }

    @Test
    fun checkAndUnlock_emitsToFlow_whenEmitToFlowTrue() = runTest {
        statisticDao.seed(listOf(StatisticEntity(difficulty = 0, gamesWon = 1)))
        val collected = mutableListOf<UnlockedAchievement>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            repository.newlyUnlocked.collect { collected.add(it) }
        }
        repository.checkAndUnlock(emitToFlow = true)
        advanceUntilIdle()
        assertTrue(collected.any { it.achievement.id == "wins_first" })
        job.cancel()
    }

    @Test
    fun checkAndUnlock_doesNotEmitToFlow_whenEmitToFlowFalse() = runTest {
        statisticDao.seed(listOf(StatisticEntity(difficulty = 0, gamesWon = 1)))
        val collected = mutableListOf<UnlockedAchievement>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            repository.newlyUnlocked.collect { collected.add(it) }
        }
        repository.checkAndUnlock(emitToFlow = false)
        advanceUntilIdle()
        assertTrue(collected.isEmpty())
        job.cancel()
    }
}

internal class FakeStatisticDao : StatisticDao {

    private val flow = MutableStateFlow<List<StatisticEntity>>(emptyList())

    fun seed(items: List<StatisticEntity>) {
        flow.value = items
    }

    override fun observeAll(): Flow<List<StatisticEntity>> = flow

    override suspend fun getAll(): List<StatisticEntity> = flow.value

    override suspend fun upsert(statistic: StatisticEntity): Unit =
        error("not used in test: upsert")

    override suspend fun getByDifficulty(difficulty: Int): StatisticEntity? =
        error("not used in test: getByDifficulty")

    override fun observeByDifficulty(difficulty: Int): Flow<StatisticEntity?> =
        error("not used in test: observeByDifficulty")

    override suspend fun deleteByDifficulty(difficulty: Int): Unit =
        error("not used in test: deleteByDifficulty")
}

internal class FakeDailyChallengeDao : DailyChallengeDao {

    private val flow = MutableStateFlow<List<DailyChallengeEntity>>(emptyList())

    fun seed(items: List<DailyChallengeEntity>) {
        flow.value = items
    }

    override fun observeAllCompleted(): Flow<List<DailyChallengeEntity>> = flow

    override suspend fun getAllCompleted(): List<DailyChallengeEntity> = flow.value

    override suspend fun getByDate(dateKey: String): DailyChallengeEntity? =
        error("not used in test: getByDate")

    override suspend fun upsert(entity: DailyChallengeEntity): Unit =
        error("not used in test: upsert")

    override suspend fun getRecentCompleted(limit: Int): List<DailyChallengeEntity> =
        error("not used in test: getRecentCompleted")

    override suspend fun getAllCompletedAsc(): List<DailyChallengeEntity> =
        error("not used in test: getAllCompletedAsc")
}

internal class FakeGameHistoryDao : GameHistoryDao {

    private val flow = MutableStateFlow<List<GameHistoryEntity>>(emptyList())

    fun seed(items: List<GameHistoryEntity>) {
        flow.value = items
    }

    override fun observeRecentWins(limit: Int): Flow<List<GameHistoryEntity>> = flow

    override suspend fun getRecentWins(limit: Int): List<GameHistoryEntity> = flow.value

    override suspend fun insert(entry: GameHistoryEntity): Unit =
        error("not used in test: insert")

    override fun getRecentGames(difficulty: Int, limit: Int): Flow<List<GameHistoryEntity>> =
        error("not used in test: getRecentGames")

    override suspend fun deleteByDifficulty(difficulty: Int): Unit =
        error("not used in test: deleteByDifficulty")
}

internal class FakeAchievementUnlockedDao : AchievementUnlockedDao {

    private val storage = mutableListOf<AchievementUnlockedEntity>()
    private val flow = MutableStateFlow<List<AchievementUnlockedEntity>>(emptyList())

    fun allInserted(): List<AchievementUnlockedEntity> = storage.toList()

    override fun observeAll(): Flow<List<AchievementUnlockedEntity>> = flow

    override suspend fun getAll(): List<AchievementUnlockedEntity> = storage.toList()

    override suspend fun existsById(id: String): Boolean = storage.any { it.id == id }

    override suspend fun insert(entity: AchievementUnlockedEntity) {
        if (storage.none { it.id == entity.id }) {
            storage.add(entity)
            flow.value = storage.toList()
        }
    }
}