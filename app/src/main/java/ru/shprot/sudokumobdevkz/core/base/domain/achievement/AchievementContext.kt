package ru.shprot.sudokumobdevkz.core.base.domain.achievement

import ru.shprot.sudokumobdevkz.core.base.data.database.entity.GameHistoryEntity
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.StatisticEntity
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty

data class AchievementContext(
    val statsByDifficulty: Map<Difficulty, StatisticEntity>,
    val dailyCompletedCount: Int,
    val dailyCurrentStreak: Int,
    val dailyBestStreak: Int,
    val recentWins: List<GameHistoryEntity>,
    val noHintsWinsCount: Int = 0,
    val flags: AchievementFlags = AchievementFlags(),
)