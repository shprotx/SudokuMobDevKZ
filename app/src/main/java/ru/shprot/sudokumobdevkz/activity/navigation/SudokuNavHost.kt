package ru.shprot.sudokumobdevkz.activity.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.shprot.sudokumobdevkz.feature.game.presentation.navigation.GameRoutes
import ru.shprot.sudokumobdevkz.feature.game.presentation.screen.GameScreen
import ru.shprot.sudokumobdevkz.feature.game.presentation.viewmodel.GameViewModel
import ru.shprot.sudokumobdevkz.feature.gameover.presentation.navigation.GameOverRoutes
import ru.shprot.sudokumobdevkz.feature.gameover.presentation.screen.GameOverScreen
import ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.navigation.HowToPlayRoutes
import ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.screen.HowToPlayScreen
import ru.shprot.sudokumobdevkz.feature.menu.presentation.navigation.MenuRoutes
import ru.shprot.sudokumobdevkz.feature.menu.presentation.screen.MenuScreen
import ru.shprot.sudokumobdevkz.feature.menu.presentation.viewmodel.MenuViewModel
import ru.shprot.sudokumobdevkz.feature.settings.presentation.navigation.SettingsRoutes
import ru.shprot.sudokumobdevkz.feature.settings.presentation.screen.PrivacyPolicyScreen
import ru.shprot.sudokumobdevkz.feature.settings.presentation.screen.SettingsScreen
import ru.shprot.sudokumobdevkz.feature.settings.presentation.viewmodel.SettingsViewModel
import ru.shprot.sudokumobdevkz.feature.splash.presentation.navigation.SplashRoutes
import ru.shprot.sudokumobdevkz.feature.splash.presentation.screen.SplashScreen
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.navigation.StatisticRoutes
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.screen.StatisticScreen
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.viewmodel.StatisticViewModel

@Composable
fun SudokuNavHost(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = SplashRoutes.SplashScreen,
    ) {
        composable<SplashRoutes.SplashScreen> {
            SplashScreen(navController = navController)
        }

        composable<MenuRoutes.MenuScreen> {
            val viewModel: MenuViewModel = hiltViewModel()
            MenuScreen(navController = navController, viewModel = viewModel)
        }

        composable<GameRoutes.GameScreen> {
            val viewModel: GameViewModel = hiltViewModel()
            GameScreen(navController = navController, viewModel = viewModel)
        }

        composable<GameOverRoutes.GameOverScreen> {
            GameOverScreen(navController = navController)
        }

        composable<StatisticRoutes.StatisticScreen> {
            val viewModel: StatisticViewModel = hiltViewModel()
            StatisticScreen(navController = navController, viewModel = viewModel)
        }

        composable<SettingsRoutes.SettingsScreen> {
            val viewModel: SettingsViewModel = hiltViewModel()
            SettingsScreen(navController = navController, viewModel = viewModel)
        }

        composable<SettingsRoutes.PrivacyPolicyScreen> {
            PrivacyPolicyScreen(navController = navController)
        }

        composable<HowToPlayRoutes.HowToPlayScreen> {
            HowToPlayScreen(navController = navController)
        }
    }
}
