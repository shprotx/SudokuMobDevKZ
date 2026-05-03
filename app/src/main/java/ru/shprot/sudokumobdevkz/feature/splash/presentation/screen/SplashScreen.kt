package ru.shprot.sudokumobdevkz.feature.splash.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ru.shprot.sudokumobdevkz.feature.menu.presentation.navigation.MenuRoutes
import ru.shprot.sudokumobdevkz.feature.splash.presentation.components.screencontent.SplashScreenContent
import ru.shprot.sudokumobdevkz.feature.splash.presentation.contract.SplashUIEffect
import ru.shprot.sudokumobdevkz.feature.splash.presentation.navigation.SplashRoutes
import ru.shprot.sudokumobdevkz.feature.splash.presentation.viewmodel.SplashViewModel

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                SplashUIEffect.NavigateToMenu ->
                    navController.navigate(MenuRoutes.MenuScreen) {
                        popUpTo<SplashRoutes.SplashScreen> { inclusive = true }
                    }
            }
        }
    }

    SplashScreenContent(
        uiState = state,
        onEvent = viewModel::setEvent,
    )
}
