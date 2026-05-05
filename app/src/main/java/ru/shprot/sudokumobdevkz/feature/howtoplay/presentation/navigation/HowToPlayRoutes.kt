package ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.navigation

import kotlinx.serialization.Serializable
import ru.shprot.sudokumobdevkz.core.base.presentation.navigation.NavRoute

@Serializable
sealed class HowToPlayRoutes : NavRoute() {

    @Serializable
    data object HowToPlayScreen : HowToPlayRoutes()
}
