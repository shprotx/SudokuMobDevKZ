package ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.navigation

import kotlinx.serialization.Serializable
import ru.shprot.sudokumobdevkz.core.base.presentation.navigation.NavRoute

@Serializable
sealed class DailyChallengeRoutes : NavRoute() {

    @Serializable
    data object DailyChallengeScreen : DailyChallengeRoutes()
}