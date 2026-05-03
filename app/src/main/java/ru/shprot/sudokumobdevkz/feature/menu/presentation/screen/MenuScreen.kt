package ru.shprot.sudokumobdevkz.feature.menu.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ru.shprot.sudokumobdevkz.feature.game.presentation.navigation.GameRoutes
import ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.navigation.HowToPlayRoutes
import ru.shprot.sudokumobdevkz.feature.menu.presentation.components.screencontent.MenuScreenContent
import ru.shprot.sudokumobdevkz.feature.menu.presentation.viewmodel.MenuViewModel
import ru.shprot.sudokumobdevkz.feature.settings.presentation.navigation.SettingsRoutes
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.navigation.StatisticRoutes

@Composable
fun MenuScreen(
    navController: NavController,
    viewModel: MenuViewModel,
) {
    var selectedDifficulty by rememberSaveable { mutableIntStateOf(0) }
    val hasSavedGame by viewModel.hasSavedGame.collectAsStateWithLifecycle()

    MenuScreenContent(
        hasSavedGame = hasSavedGame,
        selectedDifficulty = selectedDifficulty,
        onDifficultySelected = { selectedDifficulty = it },
        onNavigateToGame = { difficulty ->
            navController.navigate(GameRoutes.GameScreen(difficulty))
        },
        onContinueGame = {
            navController.navigate(GameRoutes.GameScreen(continueGame = true))
        },
        onNavigateToStatistic = {
            navController.navigate(StatisticRoutes.StatisticScreen)
        },
        onNavigateToSettings = {
            navController.navigate(SettingsRoutes.SettingsScreen)
        },
        onNavigateToHowToPlay = {
            navController.navigate(HowToPlayRoutes.HowToPlayScreen)
        },
    )
}
