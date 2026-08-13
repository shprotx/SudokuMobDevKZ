package ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud

import android.util.Log
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudGameServices
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudProgressMappers.toDto
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudProgressMappers.toEntity
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudProgressSerializer
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.CloudProgress
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.CustomThemeDto
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.AchievementUnlockedDao
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.CustomThemeDao
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.DailyChallengeDao
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.SavedGameDao
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.StatisticDao
import ru.shprot.sudokumobdevkz.core.base.data.repository.IVisitStreakRepository
import kotlinx.serialization.json.Json
import ru.shprot.sudokumobdevkz.core.base.domain.model.ThemeColors
import javax.inject.Inject

class ImportFromCloudUseCase @Inject constructor(
    private val cloud: CloudGameServices,
    private val statisticDao: StatisticDao,
    private val achievementUnlockedDao: AchievementUnlockedDao,
    private val dailyChallengeDao: DailyChallengeDao,
    private val savedGameDao: SavedGameDao,
    private val customThemeDao: CustomThemeDao,
    private val visitStreakRepository: IVisitStreakRepository,
    private val syncToCloud: SyncToCloudUseCase,
) {
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    suspend fun loadCloudSnapshot(): CloudProgress? {
        if (!cloud.isAvailable) return null
        val bytes = cloud.readSnapshot(SyncToCloudUseCase.SNAPSHOT_NAME) ?: return null
        return CloudProgressSerializer.decode(bytes)
    }

    suspend fun currentLocalProgress(): CloudProgress {
        val visitStreak = visitStreakRepository.currentStreak
        return CloudProgress(
            schemaVersion = CloudProgress.SCHEMA_VERSION,
            statistics = statisticDao.getAll().associateBy({ it.difficulty }, { it.toDto() }),
            unlockedAchievements = achievementUnlockedDao.getAll().map { it.toDto() },
            dailyChallenges = dailyChallengeDao.getAllCompleted().map { it.toDto() },
            savedGame = savedGameDao.get()?.toDto(),
            customThemes = customThemeDao.getAll().filter { !it.isBuiltIn }.map { it.toDto() },
            currentVisitStreak = visitStreak.currentStreak,
            bestVisitStreak = visitStreak.bestStreak,
            lastVisitDate = visitStreak.lastVisitDate,
            lastSyncTimestamp = 0L,
        )
    }

    suspend fun applyProgress(progress: CloudProgress) = syncToCloud.withImport {
        progress.statistics.forEach { (difficultyKey, dto) ->
            statisticDao.upsert(dto.toEntity(difficultyKey))
        }
        progress.unlockedAchievements.forEach { dto ->
            achievementUnlockedDao.insert(dto.toEntity())
        }
        progress.dailyChallenges.forEach { dto ->
            dailyChallengeDao.upsert(dto.toEntity())
        }
        progress.savedGame?.let { savedGameDao.save(it.toEntity()) }
        progress.customThemes.forEach { dto ->
            if (isValidCustomThemeDto(dto)) {
                customThemeDao.upsert(dto.toEntity())
            } else {
                Log.w("ImportFromCloud", "Skipping invalid custom theme: ${dto.id}")
            }
        }
        visitStreakRepository.mergeFromCloud(
            cloudCurrentStreak = progress.currentVisitStreak,
            cloudBestStreak = progress.bestVisitStreak,
            cloudLastVisitDate = progress.lastVisitDate,
        )
    }

    private fun isValidCustomThemeDto(dto: CustomThemeDto): Boolean {
        if (dto.name.isBlank()) return false
        return runCatching { json.decodeFromString<ThemeColors>(dto.colorsJson) }.isSuccess
    }

    companion object {
        fun isEmpty(progress: CloudProgress): Boolean {
            val totalWins = progress.statistics.values.sumOf { it.gamesWon }
            return totalWins == 0 &&
                progress.unlockedAchievements.isEmpty() &&
                progress.dailyChallenges.none { it.isCompleted }
        }
    }
}
