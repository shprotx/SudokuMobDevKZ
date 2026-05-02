package ru.shprot.sudokumobdevkz.feature.game.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.GameActionsBar
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.GameStatusBar
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.GameToolbar
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.NewGameDialog
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.NumberPanel
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.PauseDialog
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.SudokuGrid
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameEffect
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameEvent
import ru.shprot.sudokumobdevkz.feature.game.presentation.viewmodel.GameViewModel

private val difficultyLabels = listOf("Лёгкая", "Средняя", "Экспертная")

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

    Scaffold(containerColor = AppTheme.colors.background) { paddingValues ->
        if (state.isGenerating) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = AppTheme.colors.primary)

                    Text(
                        modifier = Modifier.padding(top = AppTheme.paddings.large),
                        text = "Генерация...",
                        style = AppTheme.typography.body1,
                        color = AppTheme.colors.textSecondary,
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            ) {
                GameToolbar(
                    onBackClick = onNavigateBack,
                    onRestartClick = { showNewGameDialog = true },
                    onPauseClick = {
                        viewModel.setEvent(GameEvent.PauseClicked)
                        showPauseDialog = true
                    },
                    onSettingsClick = { },
                )

                WeightSpacer()

                GameStatusBar(
                    difficultyLabel = difficultyLabels.getOrElse(state.difficulty) { "Лёгкая" },
                    errors = state.errors,
                    maxErrors = state.maxErrors,
                    lives = state.maxErrors - state.errors,
                    timer = state.timer,
                )

                SudokuGrid(
                    modifier = Modifier.padding(
                        top = AppTheme.paddings.default,
                        start = AppTheme.paddings.medium,
                        end = AppTheme.paddings.medium,
                    ),
                    cells = state.cells,
                    selectedRow = state.selectedRow,
                    selectedCol = state.selectedCol,
                    onCellClick = { row, col ->
                        viewModel.setEvent(GameEvent.CellClicked(row, col))
                    },
                )

                WeightSpacer()

                NumberPanel(
                    modifier = Modifier.padding(horizontal = AppTheme.paddings.medium),
                    availableNumbers = state.availableNumbers,
                    onNumberClick = { number ->
                        viewModel.setEvent(GameEvent.NumberClicked(number))
                    },
                )

                WeightSpacer()

                GameActionsBar(
                    modifier = Modifier
                        .padding(horizontal = AppTheme.paddings.large)
                        .padding(bottom = AppTheme.paddings.medium),
                    isNotesEnabled = state.isNotesEnabled,
                    hintsRemaining = state.hintsRemaining,
                    onUndoClick = { viewModel.setEvent(GameEvent.UndoClicked) },
                    onEraseClick = { viewModel.setEvent(GameEvent.EraseClicked) },
                    onNotesClick = { viewModel.setEvent(GameEvent.NotesToggled) },
                    onHintClick = { viewModel.setEvent(GameEvent.HintClicked) },
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.WeightSpacer() {
    Spacer(modifier = Modifier.weight(1f))
}
