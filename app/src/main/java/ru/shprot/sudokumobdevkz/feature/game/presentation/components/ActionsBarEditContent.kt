package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import ru.shprot.sudokumobdevkz.core.base.domain.model.ActionButtonId
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameUIState
import sh.calvin.reorderable.ReorderableRow

@Composable
internal fun ActionsBarEditContent(
    modifier: Modifier,
    uiState: GameUIState,
    onItemMoved: (Int, Int) -> Unit,
) {
    ReorderableRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = AppTheme.paddings.large,
                vertical = AppTheme.paddings.medium,
            ),
        list = uiState.actionButtonOrder,
        horizontalArrangement = Arrangement.SpaceEvenly,
        onSettle = { fromIndex, toIndex ->
            onItemMoved(fromIndex, toIndex)
        },
    ) { index, buttonId, isDragging ->
        key(buttonId) {
            InnerEditableItem(
                modifier = Modifier,
                overlayModifier = Modifier.draggableHandle(),
                wiggleIndex = index,
                isDragging = isDragging,
            ) {
                ActionButtonContent(
                    modifier = Modifier,
                    buttonId = buttonId,
                    isNotesEnabled = uiState.isNotesEnabled,
                    hintsRemaining = uiState.hintsRemaining,
                    isHintModeActive = uiState.isHintModeActive,
                    stretched = false,
                    onClick = {},
                )
            }
        }
    }
}
