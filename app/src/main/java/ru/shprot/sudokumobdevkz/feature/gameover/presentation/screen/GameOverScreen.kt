package ru.shprot.sudokumobdevkz.feature.gameover.presentation.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.toRoute
import ru.shprot.sudokumobdevkz.feature.game.presentation.navigation.GameRoutes
import ru.shprot.sudokumobdevkz.feature.gameover.presentation.components.screencontent.GameOverScreenContent
import ru.shprot.sudokumobdevkz.feature.gameover.presentation.navigation.GameOverRoutes
import ru.shprot.sudokumobdevkz.feature.menu.presentation.navigation.MenuRoutes

@Composable
fun GameOverScreen(navController: NavController) {
    val route = navController.currentBackStackEntry?.toRoute<GameOverRoutes.GameOverScreen>()
        ?: return

    GameOverScreenContent(
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
