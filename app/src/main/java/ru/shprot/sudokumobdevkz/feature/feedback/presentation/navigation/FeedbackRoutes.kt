package ru.shprot.sudokumobdevkz.feature.feedback.presentation.navigation

import kotlinx.serialization.Serializable
import ru.shprot.sudokumobdevkz.core.base.presentation.navigation.NavRoute

@Serializable
sealed class FeedbackRoutes : NavRoute() {

    @Serializable
    data object FeedbackScreen : FeedbackRoutes()
}