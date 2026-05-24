package ru.shprot.sudokumobdevkz.core.base.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudGameServices
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInState
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.GameHistoryDao
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.SavedGameDao
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.StatisticDao
import ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud.SubmitOverallScoreUseCase
import ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud.SyncToCloudUseCase
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.GameHistoryEntity
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.SavedGameEntity
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.StatisticEntity
import ru.shprot.sudokumobdevkz.core.base.data.remote.FirebaseApi
import ru.shprot.sudokumobdevkz.core.base.data.util.safeRunCatching
import ru.shprot.sudokumobdevkz.core.base.data.remote.FirebaseStatDto
import ru.shprot.sudokumobdevkz.core.base.domain.model.DailyPlaytime
import ru.shprot.sudokumobdevkz.core.base.domain.model.GameSaveData
import ru.shprot.sudokumobdevkz.core.base.domain.model.PercentileResult
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SudokuRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val statisticDao: StatisticDao,
    private val gameHistoryDao: GameHistoryDao,
    private val savedGameDao: SavedGameDao,
    private val firebaseApi: FirebaseApi,
    private val json: Json,
    private val dailyChallengeRepository: DailyChallengeRepository,
    private val syncToCloud: SyncToCloudUseCase,
    private val submitOverallScore: SubmitOverallScoreUseCase,
    private val leaderboardRepository: LeaderboardRepository,
    private val cloud: CloudGameServices,
) {

    private val syncScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    // --- Statistics ---

    suspend fun syncStatisticsFromFirebase() = withContext(Dispatchers.IO) {
        safeRunCatching {
            val stats = firebaseApi.getOwnStats(getDeviceId()) ?: return@withContext
            for ((diffKey, dto) in stats) {
                val diffKeyInt = diffKey.toIntOrNull() ?: continue
                val difficulty = Difficulty.fromFirebaseKey(diffKeyInt) ?: continue
                if (dto.gamesStarted <= 0) continue
                val existing = statisticDao.getByDifficulty(difficulty.firebaseKey)
                statisticDao.upsert(
                    StatisticEntity(
                        difficulty = difficulty.firebaseKey,
                        bestTime = dto.bestTime,
                        averageTime = dto.averageTime,
                        gamesStarted = dto.gamesStarted,
                        gamesWon = dto.gamesWon,
                        percentOfWins = if (dto.gamesStarted > 0) (100 * dto.gamesWon) / dto.gamesStarted else 0,
                        winsWithoutErrors = dto.winsWithoutErrors,
                        bestWinsLine = dto.bestWinsLine,
                        currentWinsLine = existing?.currentWinsLine ?: 0,
                        casualGamesPlayed = existing?.casualGamesPlayed ?: 0,
                        allTime = dto.averageTime.toLong() * dto.gamesWon,
                    ),
                )
            }
        }
    }

    suspend fun getStatistic(difficulty: Difficulty): StatisticEntity? =
        statisticDao.getByDifficulty(difficulty.firebaseKey)

    fun observeStatistic(difficulty: Difficulty): Flow<StatisticEntity?> =
        statisticDao.observeByDifficulty(difficulty.firebaseKey)

    suspend fun totalWins(): Int {
        val standard = statisticDao.getAll().sumOf { it.gamesWon }
        val daily = dailyChallengeRepository.completedCount()
        return standard + daily
    }

    suspend fun updateStatistic(
        difficulty: Difficulty,
        isWin: Boolean,
        timeSeconds: Int,
        errorCount: Int,
    ) {
        val existing = statisticDao.getByDifficulty(difficulty.firebaseKey)
            ?: StatisticEntity(difficulty.firebaseKey)
        val updated = existing.updated(isWin, timeSeconds, errorCount)
        statisticDao.upsert(updated)
        syncScope.launch { syncToFirebase(updated) }
        syncToCloud.trigger()
        if (isWin && timeSeconds > 0) {
            syncScope.launch {
                submitOverallScore()
                leaderboardRepository.refresh()
            }
        }
    }

    suspend fun incrementCasualGames(difficulty: Difficulty) {
        val existing = statisticDao.getByDifficulty(difficulty.firebaseKey)
            ?: StatisticEntity(difficulty.firebaseKey)
        statisticDao.upsert(existing.copy(casualGamesPlayed = existing.casualGamesPlayed + 1))
    }

    suspend fun resetStatistic(difficulty: Difficulty) {
        statisticDao.deleteByDifficulty(difficulty.firebaseKey)
        gameHistoryDao.deleteByDifficulty(difficulty.firebaseKey)
        clearFirebaseStatistic(difficulty)
        syncToCloud.trigger()
    }

    // --- Game History (for bar chart) ---

    suspend fun saveGameResult(
        difficulty: Difficulty,
        timeSeconds: Int,
        errors: Int,
        isWin: Boolean,
        hintsUsed: Int = 0,
        isDaily: Boolean = false,
    ) {
        gameHistoryDao.insert(
            GameHistoryEntity(
                difficulty = difficulty.firebaseKey,
                timeSeconds = timeSeconds,
                errors = errors,
                isWin = isWin,
                hintsUsed = hintsUsed,
                isDaily = isDaily,
            )
        )
    }

    fun observeRecentGames(difficulty: Difficulty, limit: Int = 7): Flow<List<GameHistoryEntity>> =
        gameHistoryDao.getRecentGames(difficulty.firebaseKey, limit)

    fun observeDailyPlaytime(): Flow<List<DailyPlaytime>> {
        val zone = ZoneId.systemDefault()
        return gameHistoryDao.observeSince(0L).map { games ->
            aggregateDailyPlaytime(games, zone)
        }
    }

    private fun aggregateDailyPlaytime(
        games: List<GameHistoryEntity>,
        zone: ZoneId,
    ): List<DailyPlaytime> {
        if (games.isEmpty()) return emptyList()

        val totalsByDate = games
            .groupBy { Instant.ofEpochMilli(it.timestamp).atZone(zone).toLocalDate() }
            .mapValues { (_, list) -> list.sumOf { it.timeSeconds } }

        val today = LocalDate.now(zone)
        val startDate = totalsByDate.keys.min()
        val totalDays = ChronoUnit.DAYS.between(startDate, today).toInt() + 1

        return (0 until totalDays).map { offset ->
            val date = startDate.plusDays(offset.toLong())
            DailyPlaytime(date = date, totalSeconds = totalsByDate[date] ?: 0)
        }
    }

    // --- Saved Game ---

    suspend fun saveGame(data: GameSaveData) {
        savedGameDao.save(
            SavedGameEntity(
                difficulty = data.difficulty,
                timeSeconds = data.timeSeconds,
                errors = data.errors,
                maxErrors = data.maxErrors,
                hintsRemaining = data.hintsRemaining,
                isNotesEnabled = data.isNotesEnabled,
                cellsJson = json.encodeToString(data.cells),
                solutionJson = json.encodeToString(data.solution),
                isStandardMode = data.isStandardMode,
            )
        )
        syncToCloud.trigger()
    }

    suspend fun loadSavedGame(): GameSaveData? {
        val entity = savedGameDao.get() ?: return null
        return try {
            GameSaveData(
                difficulty = entity.difficulty,
                timeSeconds = entity.timeSeconds,
                errors = entity.errors,
                maxErrors = entity.maxErrors,
                hintsRemaining = entity.hintsRemaining,
                isNotesEnabled = entity.isNotesEnabled,
                cells = json.decodeFromString(entity.cellsJson),
                solution = json.decodeFromString(entity.solutionJson),
                isStandardMode = entity.isStandardMode,
            )
        } catch (_: Exception) {
            savedGameDao.delete()
            null
        }
    }

    suspend fun hasSavedGame(): Boolean = savedGameDao.get() != null

    suspend fun deleteSavedGame() {
        savedGameDao.delete()
        syncToCloud.trigger()
    }

    // --- Firebase ---

    private suspend fun syncToFirebase(stat: StatisticEntity) = withContext(Dispatchers.IO) {
        try {
            firebaseApi.uploadStatistic(
                deviceId = currentFirebaseKey(),
                difficulty = stat.difficulty,
                stat = FirebaseStatDto(
                    averageTime = stat.averageTime,
                    bestTime = stat.bestTime,
                    gamesWon = stat.gamesWon,
                    gamesStarted = stat.gamesStarted,
                    winsWithoutErrors = stat.winsWithoutErrors,
                    bestWinsLine = stat.bestWinsLine,
                ),
            )
        } catch (_: Exception) { }
    }

    private suspend fun clearFirebaseStatistic(difficulty: Difficulty) = withContext(Dispatchers.IO) {
        try {
            firebaseApi.uploadStatistic(
                deviceId = currentFirebaseKey(),
                difficulty = difficulty.firebaseKey,
                stat = FirebaseStatDto(),
            )
        } catch (_: Exception) { }
    }

    suspend fun fetchPercentile(difficulty: Difficulty, userAverageTime: Int): PercentileResult =
        withContext(Dispatchers.IO) {
            try {
                val allStats = firebaseApi.getAllStats() ?: return@withContext PercentileResult(-1, 0)
                val selfKey = currentFirebaseKey()
                val diffKey = difficulty.firebaseKey.toString()
                var totalPlayers = 0
                var slowerCount = 0

                for ((uid, userData) in allStats) {
                    val diffData = userData[diffKey] ?: continue
                    val avgTime = diffData.averageTime
                    if (avgTime <= 0 || uid == selfKey) continue
                    totalPlayers++
                    if (avgTime > userAverageTime) slowerCount++
                }

                if (totalPlayers < 10) {
                    PercentileResult(-1, totalPlayers)
                } else {
                    PercentileResult((slowerCount * 100) / totalPlayers, totalPlayers)
                }
            } catch (_: Exception) {
                PercentileResult(-1, 0)
            }
        }

    suspend fun migrateFirebaseKeyToPgs(playerId: String) = withContext(Dispatchers.IO) {
        try {
            val deviceKey = deviceFirebaseKey()
            val playerKey = pgsFirebaseKey(playerId)
            if (deviceKey == playerKey) return@withContext
            val oldStats = firebaseApi.getOwnStats(deviceKey) ?: return@withContext
            if (oldStats.isEmpty()) return@withContext

            for ((diffKey, dto) in oldStats) {
                val difficulty = diffKey.toIntOrNull() ?: continue
                firebaseApi.uploadStatistic(playerKey, difficulty, dto)
                firebaseApi.uploadStatistic(deviceKey, difficulty, FirebaseStatDto())
            }
        } catch (_: Exception) { }
    }

    private fun currentFirebaseKey(): String {
        val signed = cloud.signInState.value
        return if (signed is SignInState.SignedIn) {
            pgsFirebaseKey(signed.playerId)
        } else {
            deviceFirebaseKey()
        }
    }

    private fun pgsFirebaseKey(playerId: String): String = "pgs_$playerId"

    private fun deviceFirebaseKey(): String = "dev_${getDeviceId()}"

    @SuppressLint("HardwareIds")
    private fun getDeviceId(): String =
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
}
