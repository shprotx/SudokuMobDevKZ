package ru.shprot.sudokumobdevkz.feature.game.presentation.components.screencontent
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.*

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameUIEvent
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameUIState

@Composable
fun GameScreenContent(
    uiState: GameUIState,
    onEvent: (GameUIEvent) -> Unit,
) {

    if (uiState.isGenerating) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.background),
            contentAlignment = Alignment.Center,
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = AppTheme.colors.primary)

                Text(
                    modifier = Modifier.padding(top = AppTheme.paddings.large),
                    text = stringResource(R.string.generating),
                    style = AppTheme.typography.body1,
                    color = AppTheme.colors.textSecondary,
                )
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.background)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) {
                    onEvent(GameUIEvent.DeselectClicked)
                },
        ) {

            GameToolbar(
                modifier = Modifier,
                onBackClick = { onEvent(GameUIEvent.BackClicked) },
                onRestartClick = { onEvent(GameUIEvent.NewGameClicked) },
                onPauseClick = { onEvent(GameUIEvent.ShowPauseDialog) },
                onSettingsClick = { onEvent(GameUIEvent.SettingsClicked) },
            )

            WeightSpacer()

            GameStatusBar(
                modifier = Modifier,
                difficultyLabel = when (uiState.difficulty) {
                    Difficulty.EASY -> stringResource(R.string.difficulty_easy)
                    Difficulty.MEDIUM -> stringResource(R.string.difficulty_middle)
                    Difficulty.HARD -> stringResource(R.string.difficulty_expert)
                },
                errors = uiState.errors,
                maxErrors = uiState.maxErrors,
                lives = uiState.maxErrors - uiState.errors,
                timer = uiState.timer,
            )

            SudokuGrid(
                modifier = Modifier.padding(
                    top = AppTheme.paddings.default,
                    start = AppTheme.paddings.medium,
                    end = AppTheme.paddings.medium,
                ),
                cells = uiState.cells,
                selectedRow = uiState.selectedRow,
                selectedCol = uiState.selectedCol,
                isPaused = uiState.isPaused,
                highlightedNumber = uiState.highlightedNumber,
                onCellClick = { row, col ->
                    onEvent(GameUIEvent.CellClicked(row, col))
                },
            )

            WeightSpacer()

            NumberPanel(
                modifier = Modifier.padding(horizontal = AppTheme.paddings.medium),
                availableNumbers = uiState.availableNumbers,
                isNotesMode = uiState.isNotesEnabled,
                onNumberClick = { number ->
                    onEvent(GameUIEvent.NumberClicked(number))
                },
            )

            WeightSpacer()

            GameActionsBar(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(horizontal = AppTheme.paddings.large)
                    .padding(bottom = AppTheme.paddings.medium),
                isNotesEnabled = uiState.isNotesEnabled,
                hintsRemaining = uiState.hintsRemaining,
                onUndoClick = { onEvent(GameUIEvent.UndoClicked) },
                onEraseClick = { onEvent(GameUIEvent.EraseClicked) },
                onNotesClick = { onEvent(GameUIEvent.NotesToggled) },
                onHintClick = { onEvent(GameUIEvent.HintClicked) },
            )
        }
    }

    if (uiState.showPauseDialog) {
        PauseDialog(
            timer = uiState.timer,
            errors = uiState.errors,
            maxErrors = uiState.maxErrors,
            onResume = {
                onEvent(GameUIEvent.DismissPauseDialog)
                onEvent(GameUIEvent.ResumeClicked)
            },
            onRestart = {
                onEvent(GameUIEvent.ShowNewGameDialog)
            },
            onExit = {
                onEvent(GameUIEvent.ExitGame)
            },
        )
    }

    if (uiState.showNewGameDialog) {
        NewGameDialog(
            initialDifficulty = uiState.difficulty.ordinal,
            onStartGame = { newDifficulty ->
                onEvent(GameUIEvent.StartNewGame(newDifficulty))
            },
            onDismiss = { onEvent(GameUIEvent.DismissNewGameDialog) },
        )
    }
}

@Composable
internal fun ColumnScope.WeightSpacer() {
    Spacer(modifier = Modifier.weight(1f))
}
