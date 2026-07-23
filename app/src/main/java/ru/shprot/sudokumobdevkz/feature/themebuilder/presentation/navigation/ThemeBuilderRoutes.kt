package ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.navigation

import kotlinx.serialization.Serializable
import ru.shprot.sudokumobdevkz.core.base.presentation.navigation.NavRoute

@Serializable
sealed class ThemeBuilderRoutes : NavRoute() {

    @Serializable
    data class ThemeBuilderScreen(
        val themeId: String? = null,
    ) : ThemeBuilderRoutes()
}