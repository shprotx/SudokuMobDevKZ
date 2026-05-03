package ru.shprot.sudokumobdevkz.feature.splash.presentation.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import ru.shprot.sudokumobdevkz.feature.menu.presentation.navigation.MenuRoutes
import ru.shprot.sudokumobdevkz.feature.splash.presentation.components.screencontent.SplashScreenContent
import ru.shprot.sudokumobdevkz.feature.splash.presentation.navigation.SplashRoutes

@Composable
fun SplashScreen(navController: NavController) {
    SplashScreenContent(
        onNavigateToMenu = {
            navController.navigate(MenuRoutes.MenuScreen) {
                popUpTo<SplashRoutes.SplashScreen> { inclusive = true }
            }
        },
    )
}
