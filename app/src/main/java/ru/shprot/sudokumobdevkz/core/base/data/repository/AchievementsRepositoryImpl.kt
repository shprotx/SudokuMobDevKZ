package ru.shprot.sudokumobdevkz.core.base.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.AchievementUnlockedDao
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.DailyChallengeDao
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.GameHistoryDao
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.StatisticDao
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.AchievementUnlockedEntity
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.DailyChallengeEntity
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.GameHistoryEntity
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.StatisticEntity
import ru.shprot.sudokumobdevkz.core.base.domain.achievement.AchievementContext
import ru.shprot.sudokumobdevkz.core.base.domain.achievement.AchievementState
import ru.shprot.sudokumobdevkz.core.base.domain.achievement.AchievementsRegistry
import ru.shprot.sudokumobdevkz.core.base.domain.achievement.UnlockedAchievement
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AchievementsRepositoryImpl @Inject constructor(
    private val statisticDao: StatisticDao,
    private val dailyChallengeDao: DailyChallengeDao,
    private val gameHistoryDao: GameHistoryDao,
    private val achievementUnlockedDao: AchievementUnlockedDao,
) : AchievementsRepository {

    private val _newlyUnlocked = MutableSharedFlow<UnlockedAchievement>(
        replay = 0,
        extraBufferCapacity = 16,
    )
    override val newlyUnlocked: SharedFlow<UnlockedAchievement> = _newlyUnlocked.asSharedFlow()

    override val achievementsState: Flow<List<AchievementState>> =
        combine(
            statisticDao.observeAll(),
            dailyChallengeDao.observeAllCompleted(),
            gameHistoryDao.observeRecentWins(RECENT_WINS_LIMIT),
            achievementUnlockedDao.observeAll(),
        ) { stats, dailies, recentWins, unlocked ->
            val context = buildContext(
                stats = stats,
                dailies = dailies,
                recentWins = recentWins,
            )
            val unlockedById = unlocked.associateBy { it.id }
            AchievementsRegistry.all.map { achievement ->
                AchievementState(
                    achievement = achievement,
                    progress = achievement.evaluate(context),
                    unlockedAt = unlockedById[achievement.id]?.unlockedAt,
                )
            }
        }

    override suspend fun checkAndUnlock(emitToFlow: Boolean): List<UnlockedAchievement> {
        val context = buildContextOnce()
        val alreadyUnlockedIds = achievementUnlockedDao.getAll().map { it.id }.toSet()
        val now = System.currentTimeMillis()

        val newly = mutableListOf<UnlockedAchievement>()
        AchievementsRegistry.all.forEach { achievement ->
            if (achievement.id in alreadyUnlockedIds) return@forEach
            val progress = achievement.evaluate(context)
            if (progress.isUnlocked) {
                achievementUnlockedDao.insert(
                    AchievementUnlockedEntity(id = achievement.id, unlockedAt = now),
                )
                val event = UnlockedAchievement(achievement = achievement, unlockedAt = now)
                newly.add(event)
                if (emitToFlow) _newlyUnlocked.emit(event)
            }
        }
        return newly
    }

    private suspend fun buildContextOnce(): AchievementContext {
        val stats = statisticDao.getAll()
        val dailies = dailyChallengeDao.getAllCompleted()
        val recentWins = gameHistoryDao.getRecentWins(RECENT_WINS_LIMIT)
        return buildContext(stats = stats, dailies = dailies, recentWins = recentWins)
    }

    private fun buildContext(
        stats: List<StatisticEntity>,
        dailies: List<DailyChallengeEntity>,
        recentWins: List<GameHistoryEntity>,
    ): AchievementContext {
        val statsMap = Difficulty.entries.associateWith { diff ->
            stats.firstOrNull { it.difficulty == diff.ordinal }
                ?: StatisticEntity(diff.ordinal)
        }
        val dailyCompletedCount = dailies.count { it.isCompleted }
        val dailyStreaks = computeDailyStreaks(dailies)
        return AchievementContext(
            statsByDifficulty = statsMap,
            dailyCompletedCount = dailyCompletedCount,
            dailyCurrentStreak = dailyStreaks.current,
            dailyBestStreak = dailyStreaks.best,
            recentWins = recentWins,
        )
    }

    private fun computeDailyStreaks(dailies: List<DailyChallengeEntity>): DailyStreaks {
        val completedDates = dailies.filter { it.isCompleted }.map { it.dateKey }.toSortedSet()
        if (completedDates.isEmpty()) return DailyStreaks(current = 0, best = 0)

        val list = completedDates.toList()
        var best = 1
        var run = 1
        for (i in 1 until list.size) {
            val prev = LocalDate.parse(list[i - 1])
            val cur = LocalDate.parse(list[i])
            run = if (prev.plusDays(1) == cur) run + 1 else 1
            if (run > best) best = run
        }

        val today = LocalDate.now().toString()
        val yesterday = LocalDate.now().minusDays(1).toString()
        val current = if (list.last() == today || list.last() == yesterday) {
            var c = 1
            for (i in list.size - 2 downTo 0) {
                val prev = LocalDate.parse(list[i])
                val cur = LocalDate.parse(list[i + 1])
                if (prev.plusDays(1) == cur) c++ else break
            }
            c
        } else {
            0
        }
        return DailyStreaks(current = current, best = best)
    }

    private data class DailyStreaks(val current: Int, val best: Int)

    private companion object {
        const val RECENT_WINS_LIMIT = 200
    }
}