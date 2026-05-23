package ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud

import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudGameServices
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudProgressMappers.toDto
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudProgressMappers.toEntity
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudProgressSerializer
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.CloudProgress
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.AchievementUnlockedDao
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.DailyChallengeDao
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.SavedGameDao
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.StatisticDao
import javax.inject.Inject

class ImportFromCloudUseCase @Inject constructor(
    private val cloud: CloudGameServices,
    private val statisticDao: StatisticDao,
    private val achievementUnlockedDao: AchievementUnlockedDao,
    private val dailyChallengeDao: DailyChallengeDao,
    private val savedGameDao: SavedGameDao,
) {

    suspend fun loadCloudSnapshot(): CloudProgress? {
        if (!cloud.isAvailable) return null
        val bytes = cloud.readSnapshot(SyncToCloudUseCase.SNAPSHOT_NAME) ?: return null
        return CloudProgressSerializer.decode(bytes)
    }

    suspend fun currentLocalProgress(): CloudProgress = CloudProgress(
        schemaVersion = CloudProgress.SCHEMA_VERSION,
        statistics = statisticDao.getAll().associateBy({ it.difficulty }, { it.toDto() }),
        unlockedAchievements = achievementUnlockedDao.getAll().map { it.toDto() },
        dailyChallenges = dailyChallengeDao.getAllCompleted().map { it.toDto() },
        savedGame = savedGameDao.get()?.toDto(),
        lastSyncTimestamp = 0L,
    )

    suspend fun applyProgress(progress: CloudProgress) {
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
    }
}
