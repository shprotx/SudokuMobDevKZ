package ru.shprot.sudokumobdevkz.feature.game.presentation.components.screencontent
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.*

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.presentation.util.deviceFitsTwoRowInPortrait
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameUIEvent
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameUIState

@Composable
internal fun GamePortraitContent(
    uiState: GameUIState,
    onEvent: (GameUIEvent) -> Unit,
) {
    val deviceFitsTwoRow = deviceFitsTwoRowInPortrait()
    val useCompactPad = uiState.compactNumberPadPreference && deviceFitsTwoRow

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
            themePopupExpanded = uiState.themePopupExpanded,
            selectedThemeId = uiState.selectedThemeId,
            onBackClick = { onEvent(GameUIEvent.BackClicked) },
            onRestartClick = { onEvent(GameUIEvent.NewGameClicked) },
            onPauseClick = { onEvent(GameUIEvent.ShowPauseDialog) },
            onPaletteClick = { onEvent(GameUIEvent.PaletteClicked) },
            onThemeSelected = { onEvent(GameUIEvent.ThemeSelected(it)) },
            onDismissThemePopup = { onEvent(GameUIEvent.DismissThemePopup) },
            onSettingsClick = { onEvent(GameUIEvent.SettingsClicked) },
        )

        WeightSpacer()

        GameStatusBar(
            modifier = Modifier,
            difficultyLabel = stringResource(uiState.difficulty.titleRes),
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

        if (useCompactPad) {
            NumberPadTwoRow(
                modifier = Modifier.padding(horizontal = AppTheme.paddings.medium),
                availableNumbers = uiState.availableNumbers,
                isNotesMode = uiState.isNotesEnabled,
                onNumberClick = { onEvent(GameUIEvent.NumberClicked(it)) },
            )
        } else {
            NumberPanel(
                modifier = Modifier.padding(horizontal = AppTheme.paddings.medium),
                availableNumbers = uiState.availableNumbers,
                isNotesMode = uiState.isNotesEnabled,
                onNumberClick = { onEvent(GameUIEvent.NumberClicked(it)) },
            )
        }

        WeightSpacer()

        GameActionsBar(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(horizontal = if (useCompactPad) AppTheme.paddings.medium else AppTheme.paddings.large)
                .padding(bottom = AppTheme.paddings.medium),
            isNotesEnabled = uiState.isNotesEnabled,
            hintsRemaining = uiState.hintsRemaining,
            isHintModeActive = uiState.isHintModeActive,
            stretched = useCompactPad,
            onUndoClick = { onEvent(GameUIEvent.UndoClicked) },
            onEraseClick = { onEvent(GameUIEvent.EraseClicked) },
            onNotesClick = { onEvent(GameUIEvent.NotesToggled) },
            onHintClick = { onEvent(GameUIEvent.HintClicked) },
        )
    }
}

@Composable
internal fun ColumnScope.WeightSpacer() {
    Spacer(modifier = Modifier.weight(1f))
}