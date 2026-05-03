package ru.shprot.sudokumobdevkz.feature.game.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.NewGameDialog
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.PauseDialog
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameEffect
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameEvent
import ru.shprot.sudokumobdevkz.feature.game.presentation.viewmodel.GameViewModel

@Composable
fun GameScreen(
    difficulty: Int,
    onNavigateToGameOver: (isWin: Boolean, time: String, errors: Int) -> Unit,
    onNavigateToGame: (difficulty: Int) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: GameViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showPauseDialog by rememberSaveable { mutableStateOf(false) }
    var showNewGameDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is GameEffect.NavigateToGameOver -> {
                    onNavigateToGameOver(effect.isWin, effect.time, effect.errors)
                }
            }
        }
    }

    if (showPauseDialog) {
        PauseDialog(
            timer = state.timer,
            errors = state.errors,
            maxErrors = state.maxErrors,
            onResume = {
                showPauseDialog = false
                viewModel.setEvent(GameEvent.ResumeClicked)
            },
            onRestart = {
                showPauseDialog = false
                showNewGameDialog = true
            },
            onExit = {
                showPauseDialog = false
                onNavigateBack()
            },
        )
    }

    if (showNewGameDialog) {
        NewGameDialog(
            initialDifficulty = difficulty,
            onStartGame = { newDifficulty ->
                showNewGameDialog = false
                onNavigateToGame(newDifficulty)
            },
            onDismiss = { showNewGameDialog = false },
        )
    }

    GameScreenContent(
        state = state,
        onEvent = viewModel::setEvent,
        onNavigateBack = onNavigateBack,
        onNewGameClick = { showNewGameDialog = true },
        onPauseClick = {
            viewModel.setEvent(GameEvent.PauseClicked)
            showPauseDialog = true
        },
    )
}
