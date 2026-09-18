package ru.shprot.sudokumobdevkz.core.base.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.DailyPlaytimeDao
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.GameHistoryDao
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.SavedGameDao
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.StatisticDao
import ru.shprot.sudokumobdevkz.core.base.data.notification.ReengagementScheduler
import ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud.RatingCalculator
import ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud.SubmitFirebaseLeaderboardUseCase
import ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud.SubmitOverallScoreUseCase
import ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud.SyncToCloudUseCase
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.DailyPlaytimeEntity
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
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SudokuRepository @Inject constructor(
    private val statisticDao: StatisticDao,
    private val gameHistoryDao: GameHistoryDao,
    private val dailyPlaytimeDao: DailyPlaytimeDao,
    private val savedGameDao: SavedGameDao,
    private val firebaseApi: FirebaseApi,
    private val json: Json,
    private val dailyChallengeRepository: DailyChallengeRepository,
    private val syncToCloud: SyncToCloudUseCase,
    private val submitOverallScore: SubmitOverallScoreUseCase,
    private val submitFirebaseLeaderboard: SubmitFirebaseLeaderboardUseCase,
    private val leaderboardRepository: LeaderboardRepository,
    private val stableIdProvider: StableIdProvider,
    private val reengagementScheduler: ReengagementScheduler,
) {

    private val syncScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    // --- Statistics ---

    suspend fun syncStatisticsFromFirebase() = withContext(Dispatchers.IO) {
        safeRunCatching {
            val stats = firebaseApi.getOwnStats(stableIdProvider.current()) ?: return@safeRunCatching
            for ((diffKey, dto) in stats) {
                val diffKeyInt = diffKey.toIntOrNull() ?: continue
                val difficulty = Difficulty.fromFirebaseKey(diffKeyInt) ?: continue
                if (dto.gamesStarted <= 0) continue
                val remote = dto.toEntity(difficulty.firebaseKey)
                val local = statisticDao.getByDifficulty(difficulty.firebaseKey)
                val merged = local?.mergedWith(remote) ?: remote
                statisticDao.upsert(merged)
                if (merged.toFirebaseDto() != dto) {
                    syncToFirebase(merged)
                }
            }
        }
    }

    private fun FirebaseStatDto.toEntity(difficultyKey: Int): StatisticEntity = StatisticEntity(
        difficulty = difficultyKey,
        bestTime = bestTime,
        averageTime = averageTime,
        gamesStarted = gamesStarted,
        gamesWon = gamesWon,
        percentOfWins = if (gamesStarted > 0) (100 * gamesWon) / gamesStarted else 0,
        winsWithoutErrors = winsWithoutErrors,
        bestWinsLine = bestWinsLine,
        allTime = averageTime.toLong() * gamesWon,
    )

    private fun StatisticEntity.toFirebaseDto(): FirebaseStatDto = FirebaseStatDto(
        averageTime = averageTime,
        bestTime = bestTime,
        gamesWon = gamesWon,
        gamesStarted = gamesStarted,
        winsWithoutErrors = winsWithoutErrors,
        bestWinsLine = bestWinsLine,
    )

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
    }

    fun submitLeaderboardForWin(
        difficulty: Difficulty,
        timeSeconds: Int,
        errors: Int,
        hintsUsed: Int,
        isDaily: Boolean,
    ) {
        syncScope.launch {
            submitOverallScore()
            val scoreDelta = RatingCalculator.scoreForWin(
                difficulty = difficulty,
                timeSeconds = timeSeconds,
                errors = errors,
                hintsUsed = hintsUsed,
                isDaily = isDaily,
            )
            submitFirebaseLeaderboard(
                scoreDelta = scoreDelta,
                difficulty = difficulty,
                timeSeconds = timeSeconds,
                errors = errors,
                hintsUsed = hintsUsed,
                isDaily = isDaily,
            )
            leaderboardRepository.refresh()
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
        dailyPlaytimeDao.deleteByDifficulty(difficulty.firebaseKey)
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
        isStandardMode: Boolean,
        timestamp: Long = System.currentTimeMillis(),
    ) {
        gameHistoryDao.insert(
            GameHistoryEntity(
                difficulty = difficulty.firebaseKey,
                timeSeconds = timeSeconds,
                errors = errors,
                isWin = isWin,
                hintsUsed = hintsUsed,
                isDaily = isDaily,
                isStandardMode = isStandardMode,
                timestamp = timestamp,
            )
        )
    }

    fun observeRecentGames(difficulty: Difficulty, limit: Int = 7): Flow<List<GameHistoryEntity>> =
        gameHistoryDao.getRecentGames(difficulty.firebaseKey, limit)

    fun observeDailyPlaytime(): Flow<List<DailyPlaytime>> {
        val zone = ZoneId.systemDefault()
        return dailyPlaytimeDao.observeAll().map { rows ->
            aggregateDailyPlaytime(rows, zone)
        }
    }

    suspend fun addPlaytime(difficulty: Difficulty, seconds: Int) {
        if (seconds <= 0) return
        dailyPlaytimeDao.addSeconds(
            dateKey = LocalDate.now(ZoneId.systemDefault()).toString(),
            difficulty = difficulty.firebaseKey,
            seconds = seconds,
        )
    }

    private fun aggregateDailyPlaytime(
        rows: List<DailyPlaytimeEntity>,
        zone: ZoneId,
    ): List<DailyPlaytime> {
        if (rows.isEmpty()) return emptyList()

        val totalsByDate = rows
            .mapNotNull { row -> runCatching { LocalDate.parse(row.dateKey) }.getOrNull()?.to(row.totalSeconds) }
            .groupBy({ it.first }, { it.second })
            .mapValues { (_, list) -> list.sum() }
        if (totalsByDate.isEmpty()) return emptyList()

        val today = LocalDate.now(zone)
        val startDate = totalsByDate.keys.min()
        val lastDate = maxOf(today, totalsByDate.keys.max())
        val totalDays = ChronoUnit.DAYS.between(startDate, lastDate).toInt() + 1

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
                isDailyChallenge = data.isDailyChallenge,
                dailyDateKey = data.dailyDateKey,
            )
        )
        syncToCloud.trigger()
        reengagementScheduler.scheduleGameResumeAfterSave()
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
                isDailyChallenge = entity.isDailyChallenge,
                dailyDateKey = entity.dailyDateKey,
                timestamp = entity.timestamp,
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
                deviceId = stableIdProvider.current(),
                difficulty = stat.difficulty,
                stat = stat.toFirebaseDto(),
            )
        } catch (_: Exception) { }
    }

    private suspend fun clearFirebaseStatistic(difficulty: Difficulty) = withContext(Dispatchers.IO) {
        try {
            firebaseApi.uploadStatistic(
                deviceId = stableIdProvider.current(),
                difficulty = difficulty.firebaseKey,
                stat = FirebaseStatDto(),
            )
        } catch (_: Exception) { }
    }

    suspend fun fetchPercentile(difficulty: Difficulty, userAverageTime: Int): PercentileResult =
        withContext(Dispatchers.IO) {
            try {
                val allStats = firebaseApi.getAllStats() ?: return@withContext PercentileResult(-1, 0)
                val selfKey = stableIdProvider.current()
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
            val deviceKey = stableIdProvider.deviceKey()
            val playerKey = stableIdProvider.pgsKey(playerId)
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
}
