package ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud

import kotlinx.coroutines.delay
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudGameServices
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.AchievementUnlockedDao
import ru.shprot.sudokumobdevkz.core.base.domain.achievement.AchievementsRegistry
import javax.inject.Inject

class BackfillAchievementsUseCase @Inject constructor(
    private val achievementUnlockedDao: AchievementUnlockedDao,
    private val cloud: CloudGameServices,
) {

    suspend operator fun invoke() {
        val unlockedIds = achievementUnlockedDao.getAll().map { it.id }.toSet()
        val pgsIds = AchievementsRegistry.all
            .filter { it.id in unlockedIds }
            .mapNotNull { it.pgsId }

        pgsIds.chunked(CHUNK_SIZE).forEach { chunk ->
            chunk.forEach { pgsId -> cloud.unlockAchievement(pgsId) }
            delay(CHUNK_DELAY_MS)
        }
    }

    private companion object {
        const val CHUNK_SIZE = 10
        const val CHUNK_DELAY_MS = 200L
    }
}
