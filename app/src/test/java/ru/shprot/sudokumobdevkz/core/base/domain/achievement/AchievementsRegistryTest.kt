package ru.shprot.sudokumobdevkz.core.base.domain.achievement

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.GameHistoryEntity
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.StatisticEntity
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

class AchievementsRegistryTest {

    private fun stat(
        diff: Difficulty,
        gamesWon: Int = 0,
        bestTime: Int = 0,
        perfect: Int = 0,
        bestLine: Int = 0,
    ): StatisticEntity = StatisticEntity(
        difficulty = diff.ordinal,
        gamesWon = gamesWon,
        bestTime = bestTime,
        winsWithoutErrors = perfect,
        bestWinsLine = bestLine,
    )

    private fun ctx(
        easy: StatisticEntity = stat(Difficulty.EASY),
        medium: StatisticEntity = stat(Difficulty.MEDIUM),
        hard: StatisticEntity = stat(Difficulty.HARD),
        dailyCompletedCount: Int = 0,
        dailyCurrentStreak: Int = 0,
        dailyBestStreak: Int = 0,
        recentWins: List<GameHistoryEntity> = emptyList(),
    ) = AchievementContext(
        statsByDifficulty = mapOf(
            Difficulty.EASY to easy,
            Difficulty.MEDIUM to medium,
            Difficulty.HARD to hard,
        ),
        dailyCompletedCount = dailyCompletedCount,
        dailyCurrentStreak = dailyCurrentStreak,
        dailyBestStreak = dailyBestStreak,
        recentWins = recentWins,
    )

    private fun byId(id: String): Achievement =
        AchievementsRegistry.all.first { it.id == id }

    private fun winAt(localHour: Int): GameHistoryEntity {
        val zone = ZoneId.systemDefault()
        val ts = ZonedDateTime.of(LocalDate.now(), LocalTime.of(localHour, 0), zone)
            .toInstant().toEpochMilli()
        return GameHistoryEntity(
            difficulty = 0,
            timeSeconds = 60,
            errors = 0,
            isWin = true,
            timestamp = ts,
        )
    }

    @Test
    fun registry_hasNoDuplicateIds() {
        val ids = AchievementsRegistry.all.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
    }

    @Test
    fun registry_has24Achievements() {
        assertEquals(24, AchievementsRegistry.all.size)
    }

    @Test
    fun winsFirst_unlocksAfterOneWin() {
        val context = ctx(easy = stat(Difficulty.EASY, gamesWon = 1))
        assertTrue(byId("wins_first").evaluate(context).isUnlocked)
    }

    @Test
    fun wins10_aggregatesAcrossDifficulties() {
        val context = ctx(
            easy = stat(Difficulty.EASY, gamesWon = 4),
            medium = stat(Difficulty.MEDIUM, gamesWon = 3),
            hard = stat(Difficulty.HARD, gamesWon = 3),
        )
        assertTrue(byId("wins_10").evaluate(context).isUnlocked)
    }

    @Test
    fun wins10_doesNotUnlockBelowTarget() {
        val context = ctx(easy = stat(Difficulty.EASY, gamesWon = 9))
        assertFalse(byId("wins_10").evaluate(context).isUnlocked)
    }

    @Test
    fun universal_requires10OnEachDifficulty() {
        val belowOnHard = ctx(
            easy = stat(Difficulty.EASY, gamesWon = 50),
            medium = stat(Difficulty.MEDIUM, gamesWon = 50),
            hard = stat(Difficulty.HARD, gamesWon = 9),
        )
        assertFalse(byId("diff_universal").evaluate(belowOnHard).isUnlocked)

        val allMet = ctx(
            easy = stat(Difficulty.EASY, gamesWon = 10),
            medium = stat(Difficulty.MEDIUM, gamesWon = 10),
            hard = stat(Difficulty.HARD, gamesWon = 10),
        )
        assertTrue(byId("diff_universal").evaluate(allMet).isUnlocked)
    }

    @Test
    fun perfect10_aggregatesAcrossDifficulties() {
        val context = ctx(
            easy = stat(Difficulty.EASY, perfect = 5),
            medium = stat(Difficulty.MEDIUM, perfect = 5),
        )
        assertTrue(byId("perfect_10").evaluate(context).isUnlocked)
    }

    @Test
    fun speedEasy_unlocksOnlyWhenBestTimeAtOrBelowTarget() {
        val tooSlow = ctx(easy = stat(Difficulty.EASY, bestTime = 200))
        assertFalse(byId("speed_easy").evaluate(tooSlow).isUnlocked)

        val fast = ctx(easy = stat(Difficulty.EASY, bestTime = 150))
        assertTrue(byId("speed_easy").evaluate(fast).isUnlocked)
    }

    @Test
    fun speedEasy_doesNotUnlockOnZeroBestTime() {
        val noWins = ctx(easy = stat(Difficulty.EASY, bestTime = 0))
        assertFalse(byId("speed_easy").evaluate(noWins).isUnlocked)
    }

    @Test
    fun streak10_takesMaxAcrossDifficulties() {
        val context = ctx(
            easy = stat(Difficulty.EASY, bestLine = 4),
            medium = stat(Difficulty.MEDIUM, bestLine = 11),
        )
        assertTrue(byId("streak_10").evaluate(context).isUnlocked)
    }

    @Test
    fun dailyStreak30_usesBestStreak() {
        val context = ctx(dailyBestStreak = 30)
        assertTrue(byId("daily_streak_30").evaluate(context).isUnlocked)
    }

    @Test
    fun nightOwl_unlocksWhenWinAt03Local() {
        val context = ctx(recentWins = listOf(winAt(3)))
        assertTrue(byId("secret_night_owl").evaluate(context).isUnlocked)
    }

    @Test
    fun nightOwl_doesNotUnlockAt09Local() {
        val context = ctx(recentWins = listOf(winAt(9)))
        assertFalse(byId("secret_night_owl").evaluate(context).isUnlocked)
    }

    @Test
    fun earlyBird_unlocksWhenWinAt06Local() {
        val context = ctx(recentWins = listOf(winAt(6)))
        assertTrue(byId("secret_early_bird").evaluate(context).isUnlocked)
    }
}