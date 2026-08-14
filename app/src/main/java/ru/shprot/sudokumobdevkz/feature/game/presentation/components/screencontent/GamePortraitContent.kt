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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import ru.shprot.sudokumobdevkz.core.base.domain.model.GameBlockId
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
    var gridBounds by remember { mutableStateOf(Rect.Zero) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .navigationBarsPadding()
            .padding(bottom = AppTheme.paddings.medium)
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

        if (uiState.isLayoutEditMode) {
            LayoutEditContent(
                modifier = Modifier,
                blockOrder = uiState.blockOrder,
                onBlockMoved = { from, to -> onEvent(GameUIEvent.BlockMoved(from, to)) },
                onReset = { onEvent(GameUIEvent.LayoutResetClicked) },
                onDone = { onEvent(GameUIEvent.LayoutEditDone) },
            )
        } else {
            uiState.blockOrder.forEachIndexed { index, blockId ->
                if (uiState.blockOrder.getOrNull(index - 1) != GameBlockId.STATUS_BAR) {
                    WeightSpacer()
                }

                GameBlockContent(
                    blockId = blockId,
                    uiState = uiState,
                    useCompactPad = useCompactPad,
                    onGridPositioned = { gridBounds = it },
                    onEvent = onEvent,
                )
            }
        }

        if (uiState.draftPopupVisible && uiState.draftPopupRow in 0..8 && uiState.draftPopupCol in 0..8) {
            DraftNotesPopup(
                row = uiState.draftPopupRow,
                col = uiState.draftPopupCol,
                notes = uiState.cells[uiState.draftPopupRow][uiState.draftPopupCol].notes,
                gridBounds = gridBounds,
                onNumberClick = { onEvent(GameUIEvent.DraftNoteToggled(it)) },
                onDismiss = { onEvent(GameUIEvent.DismissDraftPopup) },
            )
        }
    }
}

@Composable
internal fun ColumnScope.WeightSpacer() {
    Spacer(modifier = Modifier.weight(1f))
}
