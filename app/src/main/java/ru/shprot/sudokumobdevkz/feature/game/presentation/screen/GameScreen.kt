package ru.shprot.sudokumobdevkz.feature.game.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.GameActionsBar
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.GameStatusBar
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.GameToolbar
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.NewGameDialog
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.NumberPanel
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.PauseDialog
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.SudokuGrid

private val difficultyLabels = listOf("Лёгкая", "Средняя", "Экспертная")

@Composable
fun GameScreen(
    difficulty: Int,
    onNavigateToGameOver: (isWin: Boolean, time: String, errors: Int) -> Unit,
    onNavigateToGame: (difficulty: Int) -> Unit,
    onNavigateBack: () -> Unit,
) {
    var selectedRow by rememberSaveable { mutableIntStateOf(-1) }
    var selectedCol by rememberSaveable { mutableIntStateOf(-1) }
    var isNotesEnabled by rememberSaveable { mutableStateOf(false) }
    var showPauseDialog by rememberSaveable { mutableStateOf(false) }
    var showNewGameDialog by rememberSaveable { mutableStateOf(false) }

    if (showPauseDialog) {
        PauseDialog(
            timer = "00:00",
            errors = 0,
            maxErrors = 3,
            onResume = { showPauseDialog = false },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            GameToolbar(
                onBackClick = onNavigateBack,
                onRestartClick = { showNewGameDialog = true },
                onPauseClick = { showPauseDialog = true },
                onSettingsClick = { },
            )

            WeightSpacer()

            GameStatusBar(
                difficultyLabel = difficultyLabels.getOrElse(difficulty) { "Лёгкая" },
                errors = 0,
                maxErrors = 3,
                lives = 3,
                timer = "00:00",
            )

            SudokuGrid(
                modifier = Modifier.padding(
                    top = AppTheme.paddings.default,
                    start = AppTheme.paddings.medium,
                    end = AppTheme.paddings.medium,
                ),
                selectedRow = selectedRow,
                selectedCol = selectedCol,
                onCellClick = { row, col ->
                    selectedRow = row
                    selectedCol = col
                },
            )

            WeightSpacer()

            NumberPanel(
                modifier = Modifier.padding(horizontal = AppTheme.paddings.medium),
                onNumberClick = { },
            )

            WeightSpacer()

            GameActionsBar(
                modifier = Modifier
                    .padding(horizontal = AppTheme.paddings.large)
                    .padding(bottom = AppTheme.paddings.medium),
                isNotesEnabled = isNotesEnabled,
                hintsRemaining = 1,
                onUndoClick = { },
                onEraseClick = { },
                onNotesClick = { isNotesEnabled = !isNotesEnabled },
                onHintClick = { },
            )
        }
    }
}

@Composable
private fun ColumnScope.WeightSpacer() {
    Spacer(modifier = Modifier.weight(1f))
}
