package ru.shprot.sudokumobdevkz.feature.settings.presentation.navigation

import kotlinx.serialization.Serializable
import ru.shprot.sudokumobdevkz.core.base.presentation.navigation.NavRoute

@Serializable
sealed class SettingsRoutes : NavRoute() {

    @Serializable
    data object SettingsScreen : SettingsRoutes()
}
