package ru.shprot.sudokumobdevkz.core.base.data.repository

import kotlinx.coroutines.flow.Flow
import ru.shprot.sudokumobdevkz.core.base.domain.model.VisitStreak

interface IVisitStreakRepository {
    val streak: Flow<VisitStreak>
    val currentStreak: VisitStreak
    suspend fun recordVisit(): VisitStreak
    suspend fun mergeFromCloud(cloudCurrentStreak: Int, cloudBestStreak: Int, cloudLastVisitDate: String?)
}