package ru.shprot.sudokumobdevkz.core.base.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import ru.shprot.sudokumobdevkz.core.base.domain.achievement.AchievementState
import ru.shprot.sudokumobdevkz.core.base.domain.achievement.UnlockedAchievement

interface AchievementsRepository {

    val newlyUnlocked: SharedFlow<UnlockedAchievement>

    val achievementsState: Flow<List<AchievementState>>

    suspend fun checkAndUnlock(emitToFlow: Boolean = true): List<UnlockedAchievement>
}