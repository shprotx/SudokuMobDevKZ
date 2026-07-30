package ru.shprot.sudokumobdevkz.core.base.domain.achievement

import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import java.time.Instant
import java.time.ZoneId

object AchievementsRegistry {

    private val winsAchievements: List<Achievement> = listOf(
        Achievement(
            id = "wins_first",
            pgsId = "CgkIqffM1tUYEAIQUA",
            titleRes = R.string.achievement_wins_first_title,
            descRes = R.string.achievement_wins_first_desc,
            iconKey = AchievementIconKey.TROPHY_BRONZE,
            category = AchievementCategory.WINS,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = totalWins(ctx), target = 1) },
        ),
        Achievement(
            id = "wins_10",
            pgsId = "CgkIqffM1tUYEAIQSg",
            titleRes = R.string.achievement_wins_10_title,
            descRes = R.string.achievement_wins_10_desc,
            iconKey = AchievementIconKey.MEDAL_BRONZE,
            category = AchievementCategory.WINS,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = totalWins(ctx), target = 10) },
        ),
        Achievement(
            id = "wins_50",
            pgsId = "CgkIqffM1tUYEAIQWw",
            titleRes = R.string.achievement_wins_50_title,
            descRes = R.string.achievement_wins_50_desc,
            iconKey = AchievementIconKey.MEDAL_SILVER,
            category = AchievementCategory.WINS,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = totalWins(ctx), target = 50) },
        ),
        Achievement(
            id = "wins_200",
            pgsId = "CgkIqffM1tUYEAIQWQ",
            titleRes = R.string.achievement_wins_200_title,
            descRes = R.string.achievement_wins_200_desc,
            iconKey = AchievementIconKey.MEDAL_GOLD,
            category = AchievementCategory.WINS,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = totalWins(ctx), target = 200) },
        ),
        Achievement(
            id = "wins_500",
            pgsId = "CgkIqffM1tUYEAIQTA",
            titleRes = R.string.achievement_wins_500_title,
            descRes = R.string.achievement_wins_500_desc,
            iconKey = AchievementIconKey.CROWN,
            category = AchievementCategory.WINS,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = totalWins(ctx), target = 500) },
        ),
        Achievement(
            id = "wins_100",
            pgsId = null,
            titleRes = R.string.achievement_wins_100_title,
            descRes = R.string.achievement_wins_100_desc,
            iconKey = AchievementIconKey.TROPHY_SILVER,
            category = AchievementCategory.WINS,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = totalWins(ctx), target = 100) },
        ),
        Achievement(
            id = "wins_300",
            pgsId = null,
            titleRes = R.string.achievement_wins_300_title,
            descRes = R.string.achievement_wins_300_desc,
            iconKey = AchievementIconKey.TROPHY_GOLD,
            category = AchievementCategory.WINS,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = totalWins(ctx), target = 300) },
        ),
        Achievement(
            id = "wins_750",
            pgsId = null,
            titleRes = R.string.achievement_wins_750_title,
            descRes = R.string.achievement_wins_750_desc,
            iconKey = AchievementIconKey.TROPHY_PLATINUM,
            category = AchievementCategory.WINS,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = totalWins(ctx), target = 750) },
        ),
        Achievement(
            id = "marathon_10h",
            pgsId = null,
            titleRes = R.string.achievement_marathon_10h_title,
            descRes = R.string.achievement_marathon_10h_desc,
            iconKey = AchievementIconKey.STOPWATCH,
            category = AchievementCategory.WINS,
            hidden = false,
            evaluate = { ctx ->
                AchievementProgress(current = totalPlaySeconds(ctx), target = MARATHON_TARGET_SECONDS)
            },
        ),
    )

    private val difficultyAchievements: List<Achievement> = listOf(
        Achievement(
            id = "diff_easy_25",
            pgsId = "CgkIqffM1tUYEAIQVA",
            titleRes = R.string.achievement_diff_easy_25_title,
            descRes = R.string.achievement_diff_easy_25_desc,
            iconKey = AchievementIconKey.LEAF,
            category = AchievementCategory.DIFFICULTY,
            hidden = false,
            evaluate = { ctx ->
                AchievementProgress(current = winsAt(ctx, Difficulty.EASY), target = 25)
            },
        ),
        Achievement(
            id = "diff_medium_25",
            pgsId = "CgkIqffM1tUYEAIQTQ",
            titleRes = R.string.achievement_diff_medium_25_title,
            descRes = R.string.achievement_diff_medium_25_desc,
            iconKey = AchievementIconKey.SUN,
            category = AchievementCategory.DIFFICULTY,
            hidden = false,
            evaluate = { ctx ->
                AchievementProgress(current = winsAt(ctx, Difficulty.MEDIUM), target = 25)
            },
        ),
        Achievement(
            id = "diff_hard_25",
            pgsId = "CgkIqffM1tUYEAIQTg",
            titleRes = R.string.achievement_diff_hard_25_title,
            descRes = R.string.achievement_diff_hard_25_desc,
            iconKey = AchievementIconKey.MOUNTAIN,
            category = AchievementCategory.DIFFICULTY,
            hidden = false,
            evaluate = { ctx ->
                AchievementProgress(current = winsAt(ctx, Difficulty.HARD), target = 25)
            },
        ),
        Achievement(
            id = "diff_universal",
            pgsId = "CgkIqffM1tUYEAIQXQ",
            titleRes = R.string.achievement_diff_universal_title,
            descRes = R.string.achievement_diff_universal_desc,
            iconKey = AchievementIconKey.COMPASS,
            category = AchievementCategory.DIFFICULTY,
            hidden = false,
            evaluate = { ctx ->
                val minPerDiff = minOf(
                    winsAt(ctx, Difficulty.EASY),
                    winsAt(ctx, Difficulty.MEDIUM),
                    winsAt(ctx, Difficulty.HARD),
                )
                AchievementProgress(current = minPerDiff, target = 10)
            },
        ),
        Achievement(
            id = "diff_universal_50",
            pgsId = null,
            titleRes = R.string.achievement_diff_universal_50_title,
            descRes = R.string.achievement_diff_universal_50_desc,
            iconKey = AchievementIconKey.GLOBE,
            category = AchievementCategory.DIFFICULTY,
            hidden = false,
            evaluate = { ctx ->
                val minPerDiff = minOf(
                    winsAt(ctx, Difficulty.EASY),
                    winsAt(ctx, Difficulty.MEDIUM),
                    winsAt(ctx, Difficulty.HARD),
                )
                AchievementProgress(current = minPerDiff, target = 50)
            },
        ),
    )

    private val perfectAchievements: List<Achievement> = listOf(
        Achievement(
            id = "perfect_1",
            pgsId = "CgkIqffM1tUYEAIQVQ",
            titleRes = R.string.achievement_perfect_1_title,
            descRes = R.string.achievement_perfect_1_desc,
            iconKey = AchievementIconKey.CHECK,
            category = AchievementCategory.PERFECT,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = totalPerfect(ctx), target = 1) },
        ),
        Achievement(
            id = "perfect_10",
            pgsId = "CgkIqffM1tUYEAIQWA",
            titleRes = R.string.achievement_perfect_10_title,
            descRes = R.string.achievement_perfect_10_desc,
            iconKey = AchievementIconKey.TARGET,
            category = AchievementCategory.PERFECT,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = totalPerfect(ctx), target = 10) },
        ),
        Achievement(
            id = "perfect_50",
            pgsId = "CgkIqffM1tUYEAIQVw",
            titleRes = R.string.achievement_perfect_50_title,
            descRes = R.string.achievement_perfect_50_desc,
            iconKey = AchievementIconKey.GEM,
            category = AchievementCategory.PERFECT,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = totalPerfect(ctx), target = 50) },
        ),
        Achievement(
            id = "perfect_25",
            pgsId = null,
            titleRes = R.string.achievement_perfect_25_title,
            descRes = R.string.achievement_perfect_25_desc,
            iconKey = AchievementIconKey.DIAMOND_PINK,
            category = AchievementCategory.PERFECT,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = totalPerfect(ctx), target = 25) },
        ),
        Achievement(
            id = "perfect_100",
            pgsId = null,
            titleRes = R.string.achievement_perfect_100_title,
            descRes = R.string.achievement_perfect_100_desc,
            iconKey = AchievementIconKey.DIAMOND_RING,
            category = AchievementCategory.PERFECT,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = totalPerfect(ctx), target = 100) },
        ),
        Achievement(
            id = "no_hints_25",
            pgsId = null,
            titleRes = R.string.achievement_no_hints_25_title,
            descRes = R.string.achievement_no_hints_25_desc,
            iconKey = AchievementIconKey.OWL,
            category = AchievementCategory.PERFECT,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = ctx.noHintsWinsCount, target = 25) },
        ),
    )

    private val speedAchievements: List<Achievement> = listOf(
        Achievement(
            id = "speed_easy",
            pgsId = "CgkIqffM1tUYEAIQWg",
            titleRes = R.string.achievement_speed_easy_title,
            descRes = R.string.achievement_speed_easy_desc,
            iconKey = AchievementIconKey.BOLT,
            category = AchievementCategory.SPEED,
            hidden = false,
            evaluate = { ctx -> speedProgress(ctx, Difficulty.EASY, targetSeconds = 150) },
        ),
        Achievement(
            id = "speed_medium",
            pgsId = "CgkIqffM1tUYEAIQUg",
            titleRes = R.string.achievement_speed_medium_title,
            descRes = R.string.achievement_speed_medium_desc,
            iconKey = AchievementIconKey.BOLT_DOUBLE,
            category = AchievementCategory.SPEED,
            hidden = false,
            evaluate = { ctx -> speedProgress(ctx, Difficulty.MEDIUM, targetSeconds = 240) },
        ),
        Achievement(
            id = "speed_hard",
            pgsId = "CgkIqffM1tUYEAIQXA",
            titleRes = R.string.achievement_speed_hard_title,
            descRes = R.string.achievement_speed_hard_desc,
            iconKey = AchievementIconKey.BOLT_TRIPLE,
            category = AchievementCategory.SPEED,
            hidden = false,
            evaluate = { ctx -> speedProgress(ctx, Difficulty.HARD, targetSeconds = 420) },
        ),
        Achievement(
            id = "speed_elite_easy",
            pgsId = null,
            titleRes = R.string.achievement_speed_elite_easy_title,
            descRes = R.string.achievement_speed_elite_easy_desc,
            iconKey = AchievementIconKey.COMET_GREEN,
            category = AchievementCategory.SPEED,
            hidden = false,
            evaluate = { ctx -> speedProgress(ctx, Difficulty.EASY, targetSeconds = 90) },
        ),
        Achievement(
            id = "speed_elite_medium",
            pgsId = null,
            titleRes = R.string.achievement_speed_elite_medium_title,
            descRes = R.string.achievement_speed_elite_medium_desc,
            iconKey = AchievementIconKey.COMET_ORANGE,
            category = AchievementCategory.SPEED,
            hidden = false,
            evaluate = { ctx -> speedProgress(ctx, Difficulty.MEDIUM, targetSeconds = 180) },
        ),
        Achievement(
            id = "speed_elite_hard",
            pgsId = null,
            titleRes = R.string.achievement_speed_elite_hard_title,
            descRes = R.string.achievement_speed_elite_hard_desc,
            iconKey = AchievementIconKey.COMET_PURPLE,
            category = AchievementCategory.SPEED,
            hidden = false,
            evaluate = { ctx -> speedProgress(ctx, Difficulty.HARD, targetSeconds = 300) },
        ),
    )

    private val streakAchievements: List<Achievement> = listOf(
        Achievement(
            id = "streak_3",
            pgsId = "CgkIqffM1tUYEAIQTw",
            titleRes = R.string.achievement_streak_3_title,
            descRes = R.string.achievement_streak_3_desc,
            iconKey = AchievementIconKey.FIRE_SMALL,
            category = AchievementCategory.STREAK,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = maxBestLine(ctx), target = 3) },
        ),
        Achievement(
            id = "streak_10",
            pgsId = "CgkIqffM1tUYEAIQVg",
            titleRes = R.string.achievement_streak_10_title,
            descRes = R.string.achievement_streak_10_desc,
            iconKey = AchievementIconKey.FIRE_MEDIUM,
            category = AchievementCategory.STREAK,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = maxBestLine(ctx), target = 10) },
        ),
        Achievement(
            id = "streak_25",
            pgsId = "CgkIqffM1tUYEAIQSw",
            titleRes = R.string.achievement_streak_25_title,
            descRes = R.string.achievement_streak_25_desc,
            iconKey = AchievementIconKey.FIRE_BIG,
            category = AchievementCategory.STREAK,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = maxBestLine(ctx), target = 25) },
        ),
        Achievement(
            id = "streak_50",
            pgsId = null,
            titleRes = R.string.achievement_streak_50_title,
            descRes = R.string.achievement_streak_50_desc,
            iconKey = AchievementIconKey.FIRE_BLUE,
            category = AchievementCategory.STREAK,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = maxBestLine(ctx), target = 50) },
        ),
    )

    private val dailyAchievements: List<Achievement> = listOf(
        Achievement(
            id = "daily_1",
            pgsId = "CgkIqffM1tUYEAIQYQ",
            titleRes = R.string.achievement_daily_1_title,
            descRes = R.string.achievement_daily_1_desc,
            iconKey = AchievementIconKey.CALENDAR_CHECK,
            category = AchievementCategory.DAILY,
            hidden = false,
            evaluate = { ctx ->
                AchievementProgress(current = ctx.dailyCompletedCount, target = 1)
            },
        ),
        Achievement(
            id = "daily_streak_7",
            pgsId = "CgkIqffM1tUYEAIQUw",
            titleRes = R.string.achievement_daily_streak_7_title,
            descRes = R.string.achievement_daily_streak_7_desc,
            iconKey = AchievementIconKey.CALENDAR_WEEK,
            category = AchievementCategory.DAILY,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = ctx.dailyBestStreak, target = 7) },
        ),
        Achievement(
            id = "daily_streak_30",
            pgsId = "CgkIqffM1tUYEAIQXg",
            titleRes = R.string.achievement_daily_streak_30_title,
            descRes = R.string.achievement_daily_streak_30_desc,
            iconKey = AchievementIconKey.CALENDAR_MONTH,
            category = AchievementCategory.DAILY,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = ctx.dailyBestStreak, target = 30) },
        ),
        Achievement(
            id = "daily_streak_100",
            pgsId = "CgkIqffM1tUYEAIQUQ",
            titleRes = R.string.achievement_daily_streak_100_title,
            descRes = R.string.achievement_daily_streak_100_desc,
            iconKey = AchievementIconKey.CALENDAR_YEAR,
            category = AchievementCategory.DAILY,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = ctx.dailyBestStreak, target = 100) },
        ),
        Achievement(
            id = "daily_streak_14",
            pgsId = null,
            titleRes = R.string.achievement_daily_streak_14_title,
            descRes = R.string.achievement_daily_streak_14_desc,
            iconKey = AchievementIconKey.CALENDAR_FORTNIGHT,
            category = AchievementCategory.DAILY,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = ctx.dailyBestStreak, target = 14) },
        ),
        Achievement(
            id = "daily_25",
            pgsId = null,
            titleRes = R.string.achievement_daily_25_title,
            descRes = R.string.achievement_daily_25_desc,
            iconKey = AchievementIconKey.CALENDAR_STACK,
            category = AchievementCategory.DAILY,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = ctx.dailyCompletedCount, target = 25) },
        ),
        Achievement(
            id = "daily_100",
            pgsId = null,
            titleRes = R.string.achievement_daily_100_title,
            descRes = R.string.achievement_daily_100_desc,
            iconKey = AchievementIconKey.HOURGLASS_GOLD,
            category = AchievementCategory.DAILY,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = ctx.dailyCompletedCount, target = 100) },
        ),
    )

    private val secretAchievements: List<Achievement> = listOf(
        Achievement(
            id = "secret_night_owl",
            pgsId = "CgkIqffM1tUYEAIQXw",
            titleRes = R.string.achievement_secret_night_owl_title,
            descRes = R.string.achievement_secret_night_owl_desc,
            iconKey = AchievementIconKey.MOON,
            category = AchievementCategory.SECRET,
            hidden = true,
            evaluate = { ctx ->
                val target = 1
                val current = if (hasWinInLocalHourRange(ctx, 0..4)) target else 0
                AchievementProgress(current = current, target = target)
            },
        ),
        Achievement(
            id = "secret_early_bird",
            pgsId = "CgkIqffM1tUYEAIQYA",
            titleRes = R.string.achievement_secret_early_bird_title,
            descRes = R.string.achievement_secret_early_bird_desc,
            iconKey = AchievementIconKey.SUNRISE,
            category = AchievementCategory.SECRET,
            hidden = true,
            evaluate = { ctx ->
                val target = 1
                val current = if (hasWinInLocalHourRange(ctx, 5..7)) target else 0
                AchievementProgress(current = current, target = target)
            },
        ),
        Achievement(
            id = "secret_on_the_edge",
            pgsId = null,
            titleRes = R.string.achievement_secret_on_the_edge_title,
            descRes = R.string.achievement_secret_on_the_edge_desc,
            iconKey = AchievementIconKey.SHIELD_CRACKED,
            category = AchievementCategory.SECRET,
            hidden = true,
            evaluate = { ctx ->
                val target = 1
                val current = if (ctx.recentWins.any { it.isWin && it.errors == 2 }) target else 0
                AchievementProgress(current = current, target = target)
            },
        ),
    )

    val all: List<Achievement> =
        winsAchievements +
            difficultyAchievements +
            perfectAchievements +
            speedAchievements +
            streakAchievements +
            dailyAchievements +
            secretAchievements

    private fun totalWins(ctx: AchievementContext): Int =
        ctx.statsByDifficulty.values.sumOf { it.gamesWon }

    private fun winsAt(ctx: AchievementContext, difficulty: Difficulty): Int =
        ctx.statsByDifficulty[difficulty]?.gamesWon ?: 0

    private fun totalPerfect(ctx: AchievementContext): Int =
        ctx.statsByDifficulty.values.sumOf { it.winsWithoutErrors }

    private fun totalPlaySeconds(ctx: AchievementContext): Int =
        ctx.statsByDifficulty.values.sumOf { it.allTime }
            .coerceAtMost(Int.MAX_VALUE.toLong())
            .toInt()

    private fun speedProgress(
        ctx: AchievementContext,
        difficulty: Difficulty,
        targetSeconds: Int,
    ): AchievementProgress {
        val best = ctx.statsByDifficulty[difficulty]?.bestTime ?: 0
        val current = if (best in 1..targetSeconds) targetSeconds else 0
        return AchievementProgress(current = current, target = targetSeconds)
    }

    private fun maxBestLine(ctx: AchievementContext): Int =
        ctx.statsByDifficulty.values.maxOfOrNull { it.bestWinsLine } ?: 0

    private fun hasWinInLocalHourRange(
        ctx: AchievementContext,
        hours: IntRange,
    ): Boolean {
        val zone = ZoneId.systemDefault()
        return ctx.recentWins.any { entity ->
            val hour = Instant.ofEpochMilli(entity.timestamp).atZone(zone).hour
            hour in hours
        }
    }

    private const val MARATHON_TARGET_SECONDS = 36000
}