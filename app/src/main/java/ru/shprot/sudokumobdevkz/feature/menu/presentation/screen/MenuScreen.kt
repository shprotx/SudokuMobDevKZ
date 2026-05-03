package ru.shprot.sudokumobdevkz.feature.menu.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ru.shprot.sudokumobdevkz.feature.game.presentation.navigation.GameRoutes
import ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.navigation.HowToPlayRoutes
import ru.shprot.sudokumobdevkz.feature.menu.presentation.components.screencontent.MenuScreenContent
import ru.shprot.sudokumobdevkz.feature.menu.presentation.contract.MenuUIEffect
import ru.shprot.sudokumobdevkz.feature.menu.presentation.viewmodel.MenuViewModel
import ru.shprot.sudokumobdevkz.feature.settings.presentation.navigation.SettingsRoutes
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.navigation.StatisticRoutes

@Composable
fun MenuScreen(
    navController: NavController,
    viewModel: MenuViewModel,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MenuUIEffect.NavigateToGame ->
                    navController.navigate(GameRoutes.GameScreen(effect.difficulty))
                is MenuUIEffect.NavigateToContinueGame ->
                    navController.navigate(GameRoutes.GameScreen(continueGame = true))
                is MenuUIEffect.NavigateToStatistic ->
                    navController.navigate(StatisticRoutes.StatisticScreen)
                is MenuUIEffect.NavigateToSettings ->
                    navController.navigate(SettingsRoutes.SettingsScreen)
                is MenuUIEffect.NavigateToHowToPlay ->
                    navController.navigate(HowToPlayRoutes.HowToPlayScreen)
            }
        }
    }

    MenuScreenContent(
        uiState = state,
        onEvent = viewModel::setEvent,
    )
}
