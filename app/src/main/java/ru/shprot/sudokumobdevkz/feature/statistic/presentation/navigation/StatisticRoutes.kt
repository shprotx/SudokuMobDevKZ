package ru.shprot.sudokumobdevkz.feature.statistic.presentation.navigation

import kotlinx.serialization.Serializable
import ru.shprot.sudokumobdevkz.core.base.presentation.navigation.NavRoute

@Serializable
sealed class StatisticRoutes : NavRoute() {

    @Serializable
    data object StatisticScreen : StatisticRoutes()
}
