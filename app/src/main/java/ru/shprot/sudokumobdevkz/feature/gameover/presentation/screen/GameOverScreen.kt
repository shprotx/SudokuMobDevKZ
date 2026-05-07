package ru.shprot.sudokumobdevkz.feature.gameover.presentation.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ru.shprot.sudokumobdevkz.core.uicommon.confetti.ConfettiOverlay
import ru.shprot.sudokumobdevkz.feature.game.presentation.navigation.GameRoutes
import ru.shprot.sudokumobdevkz.feature.gameover.presentation.components.screencontent.GameOverScreenContent
import ru.shprot.sudokumobdevkz.feature.gameover.presentation.contract.GameOverUIEffect
import ru.shprot.sudokumobdevkz.feature.gameover.presentation.contract.GameOverUIEvent
import ru.shprot.sudokumobdevkz.feature.gameover.presentation.viewmodel.GameOverViewModel
import ru.shprot.sudokumobdevkz.feature.menu.presentation.navigation.MenuRoutes

@Composable
fun GameOverScreen(
    navController: NavController,
    viewModel: GameOverViewModel,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler {
        viewModel.setEvent(GameOverUIEvent.BackToMenuClicked)
    }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                GameOverUIEffect.NavigateToMenu ->
                    navController.navigate(MenuRoutes.MenuScreen) {
                        popUpTo<MenuRoutes.MenuScreen> { inclusive = true }
                    }

                is GameOverUIEffect.NavigateToNewGame ->
                    navController.navigate(GameRoutes.GameScreen(difficultyOrdinal = effect.difficultyOrdinal)) {
                        popUpTo<MenuRoutes.MenuScreen>()
                    }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GameOverScreenContent(
            uiState = state,
            onEvent = viewModel::setEvent,
        )

        if (state.isWin) {
            ConfettiOverlay(modifier = Modifier.fillMaxSize())
        }
    }
}
