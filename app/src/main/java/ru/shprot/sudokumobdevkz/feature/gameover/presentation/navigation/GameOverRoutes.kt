package ru.shprot.sudokumobdevkz.feature.gameover.presentation.navigation

import kotlinx.serialization.Serializable
import ru.shprot.sudokumobdevkz.core.base.presentation.navigation.NavRoute

@Serializable
sealed class GameOverRoutes : NavRoute() {

    @Serializable
    data class GameOverScreen(
        val isWin: Boolean = false,
        val time: String = "00:00",
        val errors: Int = 0,
        val difficultyOrdinal: Int = 0,
        val isDailyChallenge: Boolean = false,
        val newStreak: Int = 0,
    ) : GameOverRoutes()
}