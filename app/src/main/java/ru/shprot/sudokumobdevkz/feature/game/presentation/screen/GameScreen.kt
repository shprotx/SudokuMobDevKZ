package ru.shprot.sudokumobdevkz.feature.game.presentation.screen

import androidx.compose.foundation.layout.Column
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
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.NumberPanel
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.SudokuGrid

private val difficultyLabels = listOf("Лёгкая", "Средняя", "Экспертная")

@Composable
fun GameScreen(
    difficulty: Int,
    onNavigateToGameOver: (isWin: Boolean, time: String, errors: Int) -> Unit,
    onNavigateBack: () -> Unit,
) {
    var selectedRow by rememberSaveable { mutableIntStateOf(-1) }
    var selectedCol by rememberSaveable { mutableIntStateOf(-1) }
    var isNotesEnabled by rememberSaveable { mutableStateOf(false) }

    Scaffold(containerColor = AppTheme.colors.background) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            GameToolbar(
                onBackClick = onNavigateBack,
                onRestartClick = { },
                onPauseClick = { },
                onSettingsClick = { },
            )

            GameStatusBar(
                modifier = Modifier.padding(top = AppTheme.paddings.medium),
                difficultyLabel = difficultyLabels.getOrElse(difficulty) { "Лёгкая" },
                errors = 0,
                maxErrors = 3,
                lives = 3,
                timer = "00:00",
            )

            SudokuGrid(
                modifier = Modifier.padding(
                    top = AppTheme.paddings.large,
                    start = AppTheme.paddings.large,
                    end = AppTheme.paddings.large,
                ),
                selectedRow = selectedRow,
                selectedCol = selectedCol,
                onCellClick = { row, col ->
                    selectedRow = row
                    selectedCol = col
                },
            )

            NumberPanel(
                modifier = Modifier.padding(
                    top = AppTheme.paddings.extraLarge,
                    start = AppTheme.paddings.large,
                    end = AppTheme.paddings.large,
                ),
                onNumberClick = { },
                onEraseClick = { },
            )

            GameActionsBar(
                modifier = Modifier.padding(
                    top = AppTheme.paddings.large,
                    start = AppTheme.paddings.large,
                    end = AppTheme.paddings.large,
                ),
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
