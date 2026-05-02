package ru.shprot.sudokumobdevkz.feature.menu.presentation.navigation

import kotlinx.serialization.Serializable
import ru.shprot.sudokumobdevkz.core.base.presentation.navigation.NavRoute

@Serializable
sealed class MenuRoutes : NavRoute() {

    @Serializable
    data object MenuScreen : MenuRoutes()
}
