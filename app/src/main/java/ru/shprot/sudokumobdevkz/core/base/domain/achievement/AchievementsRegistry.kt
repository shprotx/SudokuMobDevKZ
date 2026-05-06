package ru.shprot.sudokumobdevkz.core.base.domain.achievement

import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import java.time.Instant
import java.time.ZoneId

object AchievementsRegistry {

    private val winsAchievements: List<Achievement> = listOf(
        Achievement(
            id = "wins_first",
            titleRes = R.string.achievement_wins_first_title,
            descRes = R.string.achievement_wins_first_desc,
            iconKey = "trophy_bronze",
            category = AchievementCategory.WINS,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = totalWins(ctx), target = 1) },
        ),
        Achievement(
            id = "wins_10",
            titleRes = R.string.achievement_wins_10_title,
            descRes = R.string.achievement_wins_10_desc,
            iconKey = "medal_bronze",
            category = AchievementCategory.WINS,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = totalWins(ctx), target = 10) },
        ),
        Achievement(
            id = "wins_50",
            titleRes = R.string.achievement_wins_50_title,
            descRes = R.string.achievement_wins_50_desc,
            iconKey = "medal_silver",
            category = AchievementCategory.WINS,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = totalWins(ctx), target = 50) },
        ),
        Achievement(
            id = "wins_200",
            titleRes = R.string.achievement_wins_200_title,
            descRes = R.string.achievement_wins_200_desc,
            iconKey = "medal_gold",
            category = AchievementCategory.WINS,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = totalWins(ctx), target = 200) },
        ),
        Achievement(
            id = "wins_500",
            titleRes = R.string.achievement_wins_500_title,
            descRes = R.string.achievement_wins_500_desc,
            iconKey = "crown",
            category = AchievementCategory.WINS,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = totalWins(ctx), target = 500) },
        ),
    )

    private val difficultyAchievements: List<Achievement> = listOf(
        Achievement(
            id = "diff_easy_25",
            titleRes = R.string.achievement_diff_easy_25_title,
            descRes = R.string.achievement_diff_easy_25_desc,
            iconKey = "leaf",
            category = AchievementCategory.DIFFICULTY,
            hidden = false,
            evaluate = { ctx ->
                AchievementProgress(current = winsAt(ctx, Difficulty.EASY), target = 25)
            },
        ),
        Achievement(
            id = "diff_medium_25",
            titleRes = R.string.achievement_diff_medium_25_title,
            descRes = R.string.achievement_diff_medium_25_desc,
            iconKey = "sun",
            category = AchievementCategory.DIFFICULTY,
            hidden = false,
            evaluate = { ctx ->
                AchievementProgress(current = winsAt(ctx, Difficulty.MEDIUM), target = 25)
            },
        ),
        Achievement(
            id = "diff_hard_25",
            titleRes = R.string.achievement_diff_hard_25_title,
            descRes = R.string.achievement_diff_hard_25_desc,
            iconKey = "mountain",
            category = AchievementCategory.DIFFICULTY,
            hidden = false,
            evaluate = { ctx ->
                AchievementProgress(current = winsAt(ctx, Difficulty.HARD), target = 25)
            },
        ),
        Achievement(
            id = "diff_universal",
            titleRes = R.string.achievement_diff_universal_title,
            descRes = R.string.achievement_diff_universal_desc,
            iconKey = "compass",
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
    )

    private val perfectAchievements: List<Achievement> = listOf(
        Achievement(
            id = "perfect_1",
            titleRes = R.string.achievement_perfect_1_title,
            descRes = R.string.achievement_perfect_1_desc,
            iconKey = "check",
            category = AchievementCategory.PERFECT,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = totalPerfect(ctx), target = 1) },
        ),
        Achievement(
            id = "perfect_10",
            titleRes = R.string.achievement_perfect_10_title,
            descRes = R.string.achievement_perfect_10_desc,
            iconKey = "target",
            category = AchievementCategory.PERFECT,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = totalPerfect(ctx), target = 10) },
        ),
        Achievement(
            id = "perfect_50",
            titleRes = R.string.achievement_perfect_50_title,
            descRes = R.string.achievement_perfect_50_desc,
            iconKey = "gem",
            category = AchievementCategory.PERFECT,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = totalPerfect(ctx), target = 50) },
        ),
    )

    private val speedAchievements: List<Achievement> = listOf(
        Achievement(
            id = "speed_easy",
            titleRes = R.string.achievement_speed_easy_title,
            descRes = R.string.achievement_speed_easy_desc,
            iconKey = "bolt",
            category = AchievementCategory.SPEED,
            hidden = false,
            evaluate = { ctx -> speedProgress(ctx, Difficulty.EASY, targetSeconds = 150) },
        ),
        Achievement(
            id = "speed_medium",
            titleRes = R.string.achievement_speed_medium_title,
            descRes = R.string.achievement_speed_medium_desc,
            iconKey = "bolt_double",
            category = AchievementCategory.SPEED,
            hidden = false,
            evaluate = { ctx -> speedProgress(ctx, Difficulty.MEDIUM, targetSeconds = 240) },
        ),
        Achievement(
            id = "speed_hard",
            titleRes = R.string.achievement_speed_hard_title,
            descRes = R.string.achievement_speed_hard_desc,
            iconKey = "bolt_triple",
            category = AchievementCategory.SPEED,
            hidden = false,
            evaluate = { ctx -> speedProgress(ctx, Difficulty.HARD, targetSeconds = 420) },
        ),
    )

    private val streakAchievements: List<Achievement> = listOf(
        Achievement(
            id = "streak_3",
            titleRes = R.string.achievement_streak_3_title,
            descRes = R.string.achievement_streak_3_desc,
            iconKey = "fire_small",
            category = AchievementCategory.STREAK,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = maxBestLine(ctx), target = 3) },
        ),
        Achievement(
            id = "streak_10",
            titleRes = R.string.achievement_streak_10_title,
            descRes = R.string.achievement_streak_10_desc,
            iconKey = "fire_medium",
            category = AchievementCategory.STREAK,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = maxBestLine(ctx), target = 10) },
        ),
        Achievement(
            id = "streak_25",
            titleRes = R.string.achievement_streak_25_title,
            descRes = R.string.achievement_streak_25_desc,
            iconKey = "fire_big",
            category = AchievementCategory.STREAK,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = maxBestLine(ctx), target = 25) },
        ),
    )

    private val dailyAchievements: List<Achievement> = listOf(
        Achievement(
            id = "daily_1",
            titleRes = R.string.achievement_daily_1_title,
            descRes = R.string.achievement_daily_1_desc,
            iconKey = "calendar_check",
            category = AchievementCategory.DAILY,
            hidden = false,
            evaluate = { ctx ->
                AchievementProgress(current = ctx.dailyCompletedCount, target = 1)
            },
        ),
        Achievement(
            id = "daily_streak_7",
            titleRes = R.string.achievement_daily_streak_7_title,
            descRes = R.string.achievement_daily_streak_7_desc,
            iconKey = "calendar_week",
            category = AchievementCategory.DAILY,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = ctx.dailyBestStreak, target = 7) },
        ),
        Achievement(
            id = "daily_streak_30",
            titleRes = R.string.achievement_daily_streak_30_title,
            descRes = R.string.achievement_daily_streak_30_desc,
            iconKey = "calendar_month",
            category = AchievementCategory.DAILY,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = ctx.dailyBestStreak, target = 30) },
        ),
        Achievement(
            id = "daily_streak_100",
            titleRes = R.string.achievement_daily_streak_100_title,
            descRes = R.string.achievement_daily_streak_100_desc,
            iconKey = "calendar_year",
            category = AchievementCategory.DAILY,
            hidden = false,
            evaluate = { ctx -> AchievementProgress(current = ctx.dailyBestStreak, target = 100) },
        ),
    )

    private val secretAchievements: List<Achievement> = listOf(
        Achievement(
            id = "secret_night_owl",
            titleRes = R.string.achievement_secret_night_owl_title,
            descRes = R.string.achievement_secret_night_owl_desc,
            iconKey = "moon",
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
            titleRes = R.string.achievement_secret_early_bird_title,
            descRes = R.string.achievement_secret_early_bird_desc,
            iconKey = "sunrise",
            category = AchievementCategory.SECRET,
            hidden = true,
            evaluate = { ctx ->
                val target = 1
                val current = if (hasWinInLocalHourRange(ctx, 5..7)) target else 0
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
}