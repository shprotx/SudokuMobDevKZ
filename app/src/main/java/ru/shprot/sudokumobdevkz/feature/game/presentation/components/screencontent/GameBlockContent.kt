package ru.shprot.sudokumobdevkz.feature.game.presentation.components.screencontent

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.core.base.domain.model.GameBlockId
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.GameActionsBar
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.GameStatusBar
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.NumberPadTwoRow
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.NumberPanel
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.SudokuGrid
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameUIEvent
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameUIState

@Composable
internal fun GameBlockContent(
    blockId: GameBlockId,
    uiState: GameUIState,
    useCompactPad: Boolean,
    onGridPositioned: (Rect) -> Unit,
    onEvent: (GameUIEvent) -> Unit,
) {
    when (blockId) {
        GameBlockId.STATUS_BAR -> GameStatusBar(
            modifier = Modifier.padding(vertical = AppTheme.paddings.medium),
            difficultyLabel = stringResource(uiState.difficulty.titleRes),
            errors = uiState.errors,
            maxErrors = uiState.maxErrors,
            lives = uiState.maxErrors - uiState.errors,
            timer = uiState.timer,
        )

        GameBlockId.GRID -> SudokuGrid(
            modifier = Modifier
                .padding(
                    horizontal = AppTheme.paddings.medium,
                    vertical = AppTheme.paddings.small,
                )
                .onGloballyPositioned { coords ->
                    onGridPositioned(coords.boundsInWindow())
                },
            cells = uiState.cells,
            selectedRow = uiState.selectedRow,
            selectedCol = uiState.selectedCol,
            isPaused = uiState.isPaused,
            highlightedNumber = uiState.highlightedNumber,
            onCellClick = { row, col ->
                onEvent(GameUIEvent.CellClicked(row, col))
            },
            onCellLongClick = { row, col ->
                onEvent(GameUIEvent.CellLongPressed(row, col))
            },
        )

        GameBlockId.NUMBER_PAD -> if (useCompactPad) {
            NumberPadTwoRow(
                modifier = Modifier.padding(
                    horizontal = AppTheme.paddings.medium,
                    vertical = AppTheme.paddings.small,
                ),
                availableNumbers = uiState.availableNumbers,
                isNotesMode = uiState.isNotesEnabled,
                onNumberClick = { onEvent(GameUIEvent.NumberClicked(it)) },
            )
        } else {
            NumberPanel(
                modifier = Modifier.padding(
                    horizontal = AppTheme.paddings.medium,
                    vertical = AppTheme.paddings.small,
                ),
                availableNumbers = uiState.availableNumbers,
                isNotesMode = uiState.isNotesEnabled,
                onNumberClick = { onEvent(GameUIEvent.NumberClicked(it)) },
            )
        }

        GameBlockId.ACTIONS_BAR -> GameActionsBar(
            modifier = Modifier
                .padding(
                    horizontal = if (useCompactPad) AppTheme.paddings.medium else AppTheme.paddings.large,
                    vertical = if (useCompactPad) AppTheme.paddings.small else AppTheme.paddings.medium,
                ),
            isNotesEnabled = uiState.isNotesEnabled,
            hintsRemaining = uiState.hintsRemaining,
            isHintModeActive = uiState.isHintModeActive,
            stretched = useCompactPad,
            onUndoClick = { onEvent(GameUIEvent.UndoClicked) },
            onEraseClick = { onEvent(GameUIEvent.EraseClicked) },
            onNotesClick = { onEvent(GameUIEvent.NotesToggled) },
            onHintClick = { onEvent(GameUIEvent.HintClicked) },
        )

        GameBlockId.SPACER_1,
        GameBlockId.SPACER_2,
        GameBlockId.SPACER_3,
        -> Unit
    }
}
