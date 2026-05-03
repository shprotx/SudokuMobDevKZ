package ru.shprot.sudokumobdevkz.feature.game.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.NewGameDialog
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.PauseDialog
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.screencontent.GameScreenContent
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameUIEffect
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameUIEvent
import ru.shprot.sudokumobdevkz.feature.game.presentation.viewmodel.GameViewModel
import ru.shprot.sudokumobdevkz.feature.gameover.presentation.navigation.GameOverRoutes
import ru.shprot.sudokumobdevkz.feature.game.presentation.navigation.GameRoutes

@Composable
fun GameScreen(
    navController: NavController,
    viewModel: GameViewModel,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is GameUIEffect.NavigateToGameOver -> {
                    navController.navigate(
                        GameOverRoutes.GameOverScreen(
                            isWin = effect.isWin,
                            time = effect.time,
                            errors = effect.errors,
                            difficulty = state.difficulty,
                        )
                    ) {
                        popUpTo<GameRoutes.GameScreen> { inclusive = true }
                    }
                }
                is GameUIEffect.NavigateToNewGame -> {
                    navController.navigate(GameRoutes.GameScreen(effect.difficulty)) {
                        popUpTo<GameRoutes.GameScreen> { inclusive = true }
                    }
                }
                is GameUIEffect.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    if (state.showPauseDialog) {
        PauseDialog(
            timer = state.timer,
            errors = state.errors,
            maxErrors = state.maxErrors,
            onResume = {
                viewModel.setEvent(GameUIEvent.DismissPauseDialog)
                viewModel.setEvent(GameUIEvent.ResumeClicked)
            },
            onRestart = {
                viewModel.setEvent(GameUIEvent.ShowNewGameDialog)
            },
            onExit = {
                viewModel.setEvent(GameUIEvent.DismissPauseDialog)
                navController.popBackStack()
            },
        )
    }

    if (state.showNewGameDialog) {
        NewGameDialog(
            initialDifficulty = state.difficulty,
            onStartGame = { newDifficulty ->
                viewModel.setEvent(GameUIEvent.DismissNewGameDialog)
                navController.navigate(GameRoutes.GameScreen(newDifficulty)) {
                    popUpTo<GameRoutes.GameScreen> { inclusive = true }
                }
            },
            onDismiss = { viewModel.setEvent(GameUIEvent.DismissNewGameDialog) },
        )
    }

    GameScreenContent(
        state = state,
        onEvent = viewModel::setEvent,
        onNavigateBack = { navController.popBackStack() },
        onNewGameClick = { viewModel.setEvent(GameUIEvent.ShowNewGameDialog) },
        onPauseClick = { viewModel.setEvent(GameUIEvent.ShowPauseDialog) },
    )
}
