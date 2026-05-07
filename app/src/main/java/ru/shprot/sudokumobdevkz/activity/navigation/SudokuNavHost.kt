package ru.shprot.sudokumobdevkz.activity.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.shprot.sudokumobdevkz.core.base.presentation.navigation.NavRoute
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.navigation.AchievementsRoutes
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.screen.AchievementsScreen
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.viewmodel.AchievementsViewModel
import ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.navigation.DailyChallengeRoutes
import ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.screen.DailyChallengeScreen
import ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.viewmodel.DailyChallengeViewModel
import ru.shprot.sudokumobdevkz.feature.game.presentation.navigation.GameRoutes
import ru.shprot.sudokumobdevkz.feature.game.presentation.screen.GameScreen
import ru.shprot.sudokumobdevkz.feature.game.presentation.viewmodel.GameViewModel
import ru.shprot.sudokumobdevkz.feature.gameover.presentation.navigation.GameOverRoutes
import ru.shprot.sudokumobdevkz.feature.gameover.presentation.screen.GameOverScreen
import ru.shprot.sudokumobdevkz.feature.gameover.presentation.viewmodel.GameOverViewModel
import ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.navigation.HowToPlayRoutes
import ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.screen.HowToPlayScreen
import ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.viewmodel.HowToPlayViewModel
import ru.shprot.sudokumobdevkz.feature.menu.presentation.navigation.MenuRoutes
import ru.shprot.sudokumobdevkz.feature.menu.presentation.screen.MenuScreen
import ru.shprot.sudokumobdevkz.feature.menu.presentation.viewmodel.MenuViewModel
import ru.shprot.sudokumobdevkz.feature.settings.presentation.navigation.SettingsRoutes
import ru.shprot.sudokumobdevkz.feature.settings.presentation.screen.PrivacyPolicyScreen
import ru.shprot.sudokumobdevkz.feature.settings.presentation.screen.SettingsScreen
import ru.shprot.sudokumobdevkz.feature.settings.presentation.viewmodel.PrivacyPolicyViewModel
import ru.shprot.sudokumobdevkz.feature.settings.presentation.viewmodel.SettingsViewModel
import ru.shprot.sudokumobdevkz.feature.splash.presentation.navigation.SplashRoutes
import ru.shprot.sudokumobdevkz.feature.splash.presentation.screen.SplashScreen
import ru.shprot.sudokumobdevkz.feature.splash.presentation.viewmodel.SplashViewModel
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.navigation.StatisticRoutes
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.screen.StatisticScreen
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.viewmodel.StatisticViewModel

@Composable
fun SudokuNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: NavRoute = SplashRoutes.SplashScreen,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable<SplashRoutes.SplashScreen> {
            val viewModel: SplashViewModel = hiltViewModel()
            SplashScreen(navController = navController, viewModel = viewModel)
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
            val viewModel: GameOverViewModel = hiltViewModel()
            GameOverScreen(navController = navController, viewModel = viewModel)
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
            val viewModel: PrivacyPolicyViewModel = hiltViewModel()
            PrivacyPolicyScreen(navController = navController, viewModel = viewModel)
        }

        composable<HowToPlayRoutes.HowToPlayScreen> {
            val viewModel: HowToPlayViewModel = hiltViewModel()
            HowToPlayScreen(navController = navController, viewModel = viewModel)
        }

        composable<AchievementsRoutes.AchievementsScreen> {
            val viewModel: AchievementsViewModel = hiltViewModel()
            AchievementsScreen(navController = navController, viewModel = viewModel)
        }

        composable<DailyChallengeRoutes.DailyChallengeScreen> {
            val viewModel: DailyChallengeViewModel = hiltViewModel()
            DailyChallengeScreen(navController = navController, viewModel = viewModel)
        }
    }
}
