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
        noHintsWinsCount: Int = 0,
        bestVisitStreak: Int = 0,
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
        noHintsWinsCount = noHintsWinsCount,
        bestVisitStreak = bestVisitStreak,
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

    private fun winWithErrors(errors: Int): GameHistoryEntity =
        GameHistoryEntity(
            difficulty = 0,
            timeSeconds = 60,
            errors = errors,
            isWin = true,
        )

    @Test
    fun registry_hasNoDuplicateIds() {
        val ids = AchievementsRegistry.all.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
    }

    @Test
    fun registry_has50Achievements() {
        assertEquals(50, AchievementsRegistry.all.size)
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

    private val pgsIdPendingAchievementIds = setOf(
        "visit_streak_5",
        "visit_streak_15",
        "visit_streak_25",
        "visit_streak_50",
        "visit_streak_100",
        "visit_streak_150",
        "visit_streak_200",
        "visit_streak_300",
        "visit_streak_365",
        "visit_streak_730",
    )

    @Test
    fun allAchievements_havePgsId() {
        val missing = AchievementsRegistry.all
            .filterNot { it.id in pgsIdPendingAchievementIds }
            .filter { it.pgsId.isNullOrBlank() }
            .map { it.id }
            .toSet()
        assertEquals(
            "All achievements must have a PGS id",
            emptySet<String>(),
            missing,
        )
    }

    @Test
    fun pgsIdPendingAchievements_stillHaveNoPgsId() {
        val stillPending = AchievementsRegistry.all
            .filter { it.id in pgsIdPendingAchievementIds }
            .filter { it.pgsId.isNullOrBlank() }
            .map { it.id }
            .toSet()
        assertEquals(pgsIdPendingAchievementIds, stillPending)
    }

    @Test
    fun allAchievements_havePgsIdInExpectedFormat() {
        AchievementsRegistry.all.mapNotNull { it.pgsId }.forEach { pgsId ->
            assertTrue("pgsId=$pgsId не начинается с CgkI", pgsId.startsWith("CgkI"))
        }
    }

    @Test
    fun allAchievements_havePgsIdsUnique() {
        val ids = AchievementsRegistry.all.mapNotNull { it.pgsId }
        assertEquals(
            "Duplicate PGS ids detected",
            ids.size,
            ids.toSet().size,
        )
    }

    @Test
    fun noHints25_usesDedicatedCounter_notRecentWins() {
        val below = ctx(noHintsWinsCount = 24, recentWins = List(200) { winAt(10) })
        assertFalse(byId("no_hints_25").evaluate(below).isUnlocked)

        val met = ctx(noHintsWinsCount = 25, recentWins = emptyList())
        assertTrue(byId("no_hints_25").evaluate(met).isUnlocked)
    }

    @Test
    fun onTheEdge_unlocksOnWinWithExactlyTwoErrors() {
        val context = ctx(recentWins = listOf(winWithErrors(2)))
        assertTrue(byId("secret_on_the_edge").evaluate(context).isUnlocked)
    }

    @Test
    fun onTheEdge_doesNotUnlockOnWinWithDifferentErrorCount() {
        val oneError = ctx(recentWins = listOf(winWithErrors(1)))
        assertFalse(byId("secret_on_the_edge").evaluate(oneError).isUnlocked)

        val threeErrors = ctx(recentWins = listOf(winWithErrors(3)))
        assertFalse(byId("secret_on_the_edge").evaluate(threeErrors).isUnlocked)
    }

    @Test
    fun marathon10h_sumsAllTimeAcrossDifficulties() {
        val below = ctx(
            easy = StatisticEntity(difficulty = Difficulty.EASY.ordinal, allTime = 10000),
            medium = StatisticEntity(difficulty = Difficulty.MEDIUM.ordinal, allTime = 10000),
            hard = StatisticEntity(difficulty = Difficulty.HARD.ordinal, allTime = 10000),
        )
        assertFalse(byId("marathon_10h").evaluate(below).isUnlocked)

        val met = ctx(
            easy = StatisticEntity(difficulty = Difficulty.EASY.ordinal, allTime = 12000),
            medium = StatisticEntity(difficulty = Difficulty.MEDIUM.ordinal, allTime = 12000),
            hard = StatisticEntity(difficulty = Difficulty.HARD.ordinal, allTime = 12000),
        )
        assertTrue(byId("marathon_10h").evaluate(met).isUnlocked)
    }

    @Test
    fun diffUniversal50_requires50OnEachDifficulty() {
        val belowOnHard = ctx(
            easy = stat(Difficulty.EASY, gamesWon = 60),
            medium = stat(Difficulty.MEDIUM, gamesWon = 60),
            hard = stat(Difficulty.HARD, gamesWon = 49),
        )
        assertFalse(byId("diff_universal_50").evaluate(belowOnHard).isUnlocked)

        val allMet = ctx(
            easy = stat(Difficulty.EASY, gamesWon = 50),
            medium = stat(Difficulty.MEDIUM, gamesWon = 50),
            hard = stat(Difficulty.HARD, gamesWon = 50),
        )
        assertTrue(byId("diff_universal_50").evaluate(allMet).isUnlocked)
    }

    @Test
    fun speedEliteEasy_unlocksOnlyWhenBestTimeAtOrBelowTarget() {
        val tooSlow = ctx(easy = stat(Difficulty.EASY, bestTime = 91))
        assertFalse(byId("speed_elite_easy").evaluate(tooSlow).isUnlocked)

        val fast = ctx(easy = stat(Difficulty.EASY, bestTime = 90))
        assertTrue(byId("speed_elite_easy").evaluate(fast).isUnlocked)
    }

    @Test
    fun speedEliteMedium_unlocksOnlyWhenBestTimeAtOrBelowTarget() {
        val tooSlow = ctx(medium = stat(Difficulty.MEDIUM, bestTime = 181))
        assertFalse(byId("speed_elite_medium").evaluate(tooSlow).isUnlocked)

        val fast = ctx(medium = stat(Difficulty.MEDIUM, bestTime = 180))
        assertTrue(byId("speed_elite_medium").evaluate(fast).isUnlocked)
    }

    @Test
    fun speedEliteHard_unlocksOnlyWhenBestTimeAtOrBelowTarget() {
        val tooSlow = ctx(hard = stat(Difficulty.HARD, bestTime = 301))
        assertFalse(byId("speed_elite_hard").evaluate(tooSlow).isUnlocked)

        val fast = ctx(hard = stat(Difficulty.HARD, bestTime = 300))
        assertTrue(byId("speed_elite_hard").evaluate(fast).isUnlocked)
    }

    @Test
    fun wins750_aggregatesAcrossDifficulties() {
        val context = ctx(
            easy = stat(Difficulty.EASY, gamesWon = 300),
            medium = stat(Difficulty.MEDIUM, gamesWon = 300),
            hard = stat(Difficulty.HARD, gamesWon = 150),
        )
        assertTrue(byId("wins_750").evaluate(context).isUnlocked)
    }

    @Test
    fun perfect100_aggregatesAcrossDifficulties() {
        val context = ctx(
            easy = stat(Difficulty.EASY, perfect = 50),
            medium = stat(Difficulty.MEDIUM, perfect = 50),
        )
        assertTrue(byId("perfect_100").evaluate(context).isUnlocked)
    }

    @Test
    fun streak50_takesMaxAcrossDifficulties() {
        val context = ctx(
            easy = stat(Difficulty.EASY, bestLine = 12),
            medium = stat(Difficulty.MEDIUM, bestLine = 50),
        )
        assertTrue(byId("streak_50").evaluate(context).isUnlocked)
    }

    @Test
    fun dailyStreak14_usesBestStreak() {
        val context = ctx(dailyBestStreak = 14)
        assertTrue(byId("daily_streak_14").evaluate(context).isUnlocked)
    }

    @Test
    fun daily100_usesCompletedCount() {
        val context = ctx(dailyCompletedCount = 100)
        assertTrue(byId("daily_100").evaluate(context).isUnlocked)
    }

    @Test
    fun visitStreak5_unlocksAtBestVisitStreak() {
        assertFalse(byId("visit_streak_5").evaluate(ctx(bestVisitStreak = 4)).isUnlocked)
        assertTrue(byId("visit_streak_5").evaluate(ctx(bestVisitStreak = 5)).isUnlocked)
    }

    @Test
    fun visitStreak730_requiresFullTarget() {
        assertFalse(byId("visit_streak_730").evaluate(ctx(bestVisitStreak = 729)).isUnlocked)
        assertTrue(byId("visit_streak_730").evaluate(ctx(bestVisitStreak = 730)).isUnlocked)
    }

    @Test
    fun visitStreakAchievements_haveAscendingTargets() {
        val ids = listOf(
            "visit_streak_5",
            "visit_streak_15",
            "visit_streak_25",
            "visit_streak_50",
            "visit_streak_100",
            "visit_streak_150",
            "visit_streak_200",
            "visit_streak_300",
            "visit_streak_365",
            "visit_streak_730",
        )
        val targets = ids.map { id -> byId(id).evaluate(ctx(bestVisitStreak = Int.MAX_VALUE)).target }
        assertEquals(targets.sorted(), targets)
        assertEquals(targets.toSet().size, targets.size)
    }

    @Test
    fun visitStreakAchievements_belongToVisitCategory() {
        val allBelongToVisit = AchievementsRegistry.all
            .filter { it.id.startsWith("visit_streak_") }
            .all { it.category == AchievementCategory.VISIT }
        assertTrue(allBelongToVisit)
    }
}