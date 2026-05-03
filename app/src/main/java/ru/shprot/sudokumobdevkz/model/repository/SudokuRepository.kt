package ru.shprot.sudokumobdevkz.model.repository

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.shprot.sudokumobdevkz.model.database.dao.GameHistoryDao
import ru.shprot.sudokumobdevkz.model.database.dao.StatisticDao
import ru.shprot.sudokumobdevkz.model.database.entity.GameHistoryEntity
import ru.shprot.sudokumobdevkz.model.database.entity.StatisticEntity
import ru.shprot.sudokumobdevkz.model.remote.FirebaseApi
import ru.shprot.sudokumobdevkz.model.remote.FirebaseStatDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SudokuRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val statisticDao: StatisticDao,
    private val gameHistoryDao: GameHistoryDao,
    private val firebaseApi: FirebaseApi,
) {
    // --- Statistics ---

    suspend fun getStatistic(difficulty: Int): StatisticEntity? =
        statisticDao.getByDifficulty(difficulty)

    fun observeStatistic(difficulty: Int): Flow<StatisticEntity?> =
        statisticDao.observeByDifficulty(difficulty)

    suspend fun updateStatistic(
        difficulty: Int,
        isWin: Boolean,
        timeSeconds: Int,
        errorCount: Int,
    ) {
        val existing = statisticDao.getByDifficulty(difficulty) ?: StatisticEntity(difficulty)
        val updated = existing.updated(isWin, timeSeconds, errorCount)
        statisticDao.upsert(updated)
        syncToFirebase(updated)
    }

    suspend fun resetStatistic(difficulty: Int) {
        statisticDao.deleteByDifficulty(difficulty)
        gameHistoryDao.deleteByDifficulty(difficulty)
        clearFirebaseStatistic(difficulty)
    }

    // --- Game History (for bar chart) ---

    suspend fun saveGameResult(
        difficulty: Int,
        timeSeconds: Int,
        errors: Int,
        isWin: Boolean,
    ) {
        gameHistoryDao.insert(
            GameHistoryEntity(
                difficulty = difficulty,
                timeSeconds = timeSeconds,
                errors = errors,
                isWin = isWin,
            )
        )
    }

    fun observeRecentWins(difficulty: Int, limit: Int = 7): Flow<List<GameHistoryEntity>> =
        gameHistoryDao.getRecentWins(difficulty, limit)

    // --- Firebase ---

    private suspend fun syncToFirebase(stat: StatisticEntity) = withContext(Dispatchers.IO) {
        try {
            firebaseApi.uploadStatistic(
                deviceId = getDeviceId(),
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

    private suspend fun clearFirebaseStatistic(difficulty: Int) = withContext(Dispatchers.IO) {
        try {
            firebaseApi.uploadStatistic(
                deviceId = getDeviceId(),
                difficulty = difficulty,
                stat = FirebaseStatDto(),
            )
        } catch (_: Exception) { }
    }

    suspend fun fetchPercentile(difficulty: Int, userAverageTime: Int): PercentileResult =
        withContext(Dispatchers.IO) {
            try {
                val allStats = firebaseApi.getAllStats() ?: return@withContext PercentileResult(-1, 0)
                val deviceId = getDeviceId()
                val diffKey = difficulty.toString()
                var totalPlayers = 0
                var slowerCount = 0

                for ((uid, userData) in allStats) {
                    val diffData = userData[diffKey] ?: continue
                    val avgTime = diffData.averageTime
                    if (avgTime <= 0 || uid == deviceId) continue
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

    @SuppressLint("HardwareIds")
    private fun getDeviceId(): String =
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
}

data class PercentileResult(val percentile: Int, val totalPlayers: Int)
