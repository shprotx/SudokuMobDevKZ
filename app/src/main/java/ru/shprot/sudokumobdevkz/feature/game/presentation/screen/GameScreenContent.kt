package ru.shprot.sudokumobdevkz.feature.game.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.GameActionsBar
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.GameStatusBar
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.GameToolbar
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.NumberPanel
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.SudokuGrid
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameEvent
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameUiState

private val difficultyLabels = listOf("Лёгкая", "Средняя", "Экспертная")

@Composable
fun GameScreenContent(
    modifier: Modifier = Modifier,
    state: GameUiState,
    onEvent: (GameEvent) -> Unit,
    onNavigateBack: () -> Unit,
    onNewGameClick: () -> Unit,
    onPauseClick: () -> Unit,
) {
    Scaffold(containerColor = AppTheme.colors.background) { paddingValues ->
        if (state.isGenerating) {
            Box(
                modifier = modifier
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
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) {
                        onEvent(GameEvent.DeselectClicked)
                    },
            ) {
                GameToolbar(
                    onBackClick = onNavigateBack,
                    onRestartClick = onNewGameClick,
                    onPauseClick = onPauseClick,
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
                    isPaused = state.isPaused,
                    highlightedNumber = state.highlightedNumber,
                    onCellClick = { row, col ->
                        onEvent(GameEvent.CellClicked(row, col))
                    },
                )

                WeightSpacer()

                NumberPanel(
                    modifier = Modifier.padding(horizontal = AppTheme.paddings.medium),
                    availableNumbers = state.availableNumbers,
                    isNotesMode = state.isNotesEnabled,
                    onNumberClick = { number ->
                        onEvent(GameEvent.NumberClicked(number))
                    },
                )

                WeightSpacer()

                GameActionsBar(
                    modifier = Modifier
                        .padding(horizontal = AppTheme.paddings.large)
                        .padding(bottom = AppTheme.paddings.medium),
                    isNotesEnabled = state.isNotesEnabled,
                    hintsRemaining = state.hintsRemaining,
                    onUndoClick = { onEvent(GameEvent.UndoClicked) },
                    onEraseClick = { onEvent(GameEvent.EraseClicked) },
                    onNotesClick = { onEvent(GameEvent.NotesToggled) },
                    onHintClick = { onEvent(GameEvent.HintClicked) },
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.WeightSpacer() {
    Spacer(modifier = Modifier.weight(1f))
}
