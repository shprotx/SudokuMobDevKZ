package ru.shprot.sudokumobdevkz.feature.game.presentation.navigation

import kotlinx.serialization.Serializable
import ru.shprot.sudokumobdevkz.core.base.presentation.navigation.NavRoute

@Serializable
sealed class GameRoutes : NavRoute() {

    @Serializable
    data class GameScreen(val difficulty: Int = 0) : GameRoutes()
}
