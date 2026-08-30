package ru.shprot.sudokumobdevkz.feature.game.presentation.components.screencontent
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.*

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.domain.model.GameBlockId
import ru.shprot.sudokumobdevkz.core.base.presentation.util.deviceFitsTwoRowInPortrait
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.button.ButtonText
import ru.shprot.sudokumobdevkz.core.uicommon.layout.FitHeightBox
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameUIEvent
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameUIState
import sh.calvin.reorderable.ReorderableColumn

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
            isLayoutEditMode = uiState.isLayoutEditMode,
            onBackClick = { onEvent(GameUIEvent.BackClicked) },
            onRestartClick = { onEvent(GameUIEvent.NewGameClicked) },
            onPauseClick = { onEvent(GameUIEvent.ShowPauseDialog) },
            onLayoutEditClick = { onEvent(GameUIEvent.LayoutEditClicked) },
            onPaletteClick = { onEvent(GameUIEvent.PaletteClicked) },
            onThemeSelected = { onEvent(GameUIEvent.ThemeSelected(it)) },
            onDismissThemePopup = { onEvent(GameUIEvent.DismissThemePopup) },
            onSettingsClick = { onEvent(GameUIEvent.SettingsClicked) },
        )

        if (uiState.isLayoutEditMode) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.paddings.large),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.layout_edit_subtitle),
                    style = AppTheme.typography.body2,
                    color = AppTheme.colors.textSecondary,
                )

                ButtonText(
                    modifier = Modifier,
                    text = stringResource(R.string.layout_reset),
                    onClick = { onEvent(GameUIEvent.LayoutResetClicked) },
                )
            }

            FitHeightBox(modifier = Modifier.weight(1f)) {
                ReorderableColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = AppTheme.paddings.medium,
                            vertical = AppTheme.paddings.small,
                        ),
                    list = uiState.blockOrder,
                    verticalArrangement = Arrangement.spacedBy(AppTheme.paddings.small),
                    onSettle = { fromIndex, toIndex ->
                        onEvent(GameUIEvent.BlockMoved(fromIndex, toIndex))
                    },
                ) { index, blockId, isDragging ->
                    key(blockId) {
                        val isExpanded = uiState.expandedEditBlock == blockId

                        LayoutEditableElement(
                            modifier = Modifier,
                            overlayModifier = Modifier.draggableHandle(),
                            wiggleIndex = index,
                            isDragging = isDragging,
                            isExpanded = isExpanded,
                            onTap = { onEvent(GameUIEvent.EditBlockTapped(blockId)) },
                        ) {
                            when {
                                blockId.isSpacer ->
                                    LayoutSpacerPlaceholder(modifier = Modifier)

                                isExpanded && blockId == GameBlockId.STATUS_BAR ->
                                    StatusBarEditContent(
                                        modifier = Modifier,
                                        uiState = uiState,
                                        onItemMoved = { from, to ->
                                            onEvent(GameUIEvent.InnerItemMoved(blockId, from, to))
                                        },
                                    )

                                isExpanded && blockId == GameBlockId.ACTIONS_BAR ->
                                    ActionsBarEditContent(
                                        modifier = Modifier,
                                        uiState = uiState,
                                        onItemMoved = { from, to ->
                                            onEvent(GameUIEvent.InnerItemMoved(blockId, from, to))
                                        },
                                    )

                                else -> GameBlockContent(
                                    blockId = blockId,
                                    uiState = uiState,
                                    useCompactPad = useCompactPad,
                                    onGridPositioned = {},
                                    onEvent = onEvent,
                                )
                            }
                        }
                    }
                }
            }
        } else {
            uiState.blockOrder.forEach { blockId ->
                if (blockId.isSpacer) {
                    WeightSpacer()
                } else {
                    GameBlockContent(
                        blockId = blockId,
                        uiState = uiState,
                        useCompactPad = useCompactPad,
                        onGridPositioned = { gridBounds = it },
                        onEvent = onEvent,
                    )
                }
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
