package ru.shprot.sudokumobdevkz.activity.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import ru.shprot.sudokumobdevkz.feature.game.presentation.navigation.GameRoutes
import ru.shprot.sudokumobdevkz.feature.game.presentation.screen.GameScreen
import ru.shprot.sudokumobdevkz.feature.gameover.presentation.navigation.GameOverRoutes
import ru.shprot.sudokumobdevkz.feature.gameover.presentation.screen.GameOverScreen
import ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.navigation.HowToPlayRoutes
import ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.screen.HowToPlayScreen
import ru.shprot.sudokumobdevkz.feature.menu.presentation.navigation.MenuRoutes
import ru.shprot.sudokumobdevkz.feature.menu.presentation.screen.MenuScreen
import ru.shprot.sudokumobdevkz.feature.settings.presentation.navigation.SettingsRoutes
import ru.shprot.sudokumobdevkz.feature.settings.presentation.screen.SettingsScreen
import ru.shprot.sudokumobdevkz.feature.splash.presentation.navigation.SplashRoutes
import ru.shprot.sudokumobdevkz.feature.splash.presentation.screen.SplashScreen
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.navigation.StatisticRoutes
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.screen.StatisticScreen

@Composable
fun SudokuNavHost(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = SplashRoutes.SplashScreen,
    ) {
        composable<SplashRoutes.SplashScreen> {
            SplashScreen(
                onNavigateToMenu = {
                    navController.navigate(MenuRoutes.MenuScreen) {
                        popUpTo<SplashRoutes.SplashScreen> { inclusive = true }
                    }
                },
            )
        }

        composable<MenuRoutes.MenuScreen> {
            MenuScreen(
                onNavigateToGame = { difficulty ->
                    navController.navigate(GameRoutes.GameScreen(difficulty))
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

        composable<GameRoutes.GameScreen> { backStackEntry ->
            val route = backStackEntry.toRoute<GameRoutes.GameScreen>()
            GameScreen(
                difficulty = route.difficulty,
                onNavigateToGameOver = { isWin, time, errors ->
                    navController.navigate(
                        GameOverRoutes.GameOverScreen(
                            isWin = isWin,
                            time = time,
                            errors = errors,
                            difficulty = route.difficulty,
                        )
                    ) {
                        popUpTo<GameRoutes.GameScreen> { inclusive = true }
                    }
                },
                onNavigateToGame = { newDifficulty ->
                    navController.navigate(GameRoutes.GameScreen(newDifficulty)) {
                        popUpTo<GameRoutes.GameScreen> { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() },
            )
        }

        composable<GameOverRoutes.GameOverScreen> { backStackEntry ->
            val route = backStackEntry.toRoute<GameOverRoutes.GameOverScreen>()
            GameOverScreen(
                isWin = route.isWin,
                time = route.time,
                errors = route.errors,
                difficulty = route.difficulty,
                onNavigateToMenu = {
                    navController.navigate(MenuRoutes.MenuScreen) {
                        popUpTo<MenuRoutes.MenuScreen> { inclusive = true }
                    }
                },
                onNavigateToNewGame = { difficulty ->
                    navController.navigate(GameRoutes.GameScreen(difficulty)) {
                        popUpTo<MenuRoutes.MenuScreen>()
                    }
                },
            )
        }

        composable<StatisticRoutes.StatisticScreen> {
            StatisticScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }

        composable<SettingsRoutes.SettingsScreen> {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }

        composable<HowToPlayRoutes.HowToPlayScreen> {
            HowToPlayScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}
