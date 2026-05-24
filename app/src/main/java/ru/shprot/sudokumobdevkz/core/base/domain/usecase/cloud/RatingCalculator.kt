package ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud

import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import kotlin.math.roundToLong

object RatingCalculator {

    fun scoreForWin(
        difficulty: Difficulty,
        timeSeconds: Int,
        errors: Int,
        hintsUsed: Int = 0,
        isDaily: Boolean = false,
    ): Long {
        if (timeSeconds <= 0) return 0L
        val base = base(difficulty)
        val target = targetTimeSeconds(difficulty)
        val rawSpeed = target.toDouble() / timeSeconds.toDouble()
        val speed = rawSpeed.coerceIn(MIN_SPEED, MAX_SPEED)
        val clean = if (errors == 0) CLEAN_BONUS else 1.0
        val hints = (1.0 - hintsUsed * HINTS_PENALTY_PER_USE).coerceAtLeast(MIN_HINTS_MULTIPLIER)
        val daily = if (isDaily) DAILY_BONUS else 1.0
        return (base * speed * clean * hints * daily).roundToLong()
    }

    fun base(difficulty: Difficulty): Int = when (difficulty) {
        Difficulty.EASY -> 100
        Difficulty.MEDIUM -> 250
        Difficulty.HARD -> 500
    }

    fun targetTimeSeconds(difficulty: Difficulty): Int = when (difficulty) {
        Difficulty.EASY -> 300
        Difficulty.MEDIUM -> 600
        Difficulty.HARD -> 1200
    }

    private const val MIN_SPEED = 0.5
    private const val MAX_SPEED = 2.0
    private const val CLEAN_BONUS = 1.3
    private const val HINTS_PENALTY_PER_USE = 0.1
    private const val MIN_HINTS_MULTIPLIER = 0.3
    private const val DAILY_BONUS = 1.5
}
