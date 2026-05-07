package ru.shprot.sudokumobdevkz.feature.achievements.presentation.navigation

import kotlinx.serialization.Serializable
import ru.shprot.sudokumobdevkz.core.base.presentation.navigation.NavRoute

@Serializable
sealed class AchievementsRoutes : NavRoute() {

    @Serializable
    data object AchievementsScreen : AchievementsRoutes()
}
