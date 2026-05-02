package ru.shprot.sudokumobdevkz.feature.splash.presentation.navigation

import kotlinx.serialization.Serializable
import ru.shprot.sudokumobdevkz.core.base.presentation.navigation.NavRoute

@Serializable
sealed class SplashRoutes : NavRoute() {

    @Serializable
    data object SplashScreen : SplashRoutes()
}
