package ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.navigation

import kotlinx.serialization.Serializable
import ru.shprot.sudokumobdevkz.core.base.presentation.navigation.NavRoute

@Serializable
sealed class LeaderboardsRoutes : NavRoute() {

    @Serializable
    data class LeaderboardsScreen(val difficultyOrdinal: Int = 0) : LeaderboardsRoutes()
}
