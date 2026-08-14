package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.core.base.domain.model.StatusItemId
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameUIEvent
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameUIState
import sh.calvin.reorderable.ReorderableRow

@Composable
internal fun StatusBarEditContent(
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
        list = uiState.statusItemOrder,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        onSettle = { fromIndex, toIndex ->
            onItemMoved(fromIndex, toIndex)
        },
    ) { index, itemId, isDragging ->
        key(itemId) {
            InnerEditableItem(
                modifier = Modifier,
                overlayModifier = Modifier.draggableHandle(),
                wiggleIndex = index,
                isDragging = isDragging,
            ) {
                StatusItemContent(
                    modifier = Modifier,
                    itemId = itemId,
                    difficultyLabel = stringResource(uiState.difficulty.titleRes),
                    errors = uiState.errors,
                    maxErrors = uiState.maxErrors,
                    lives = uiState.maxErrors - uiState.errors,
                    timer = uiState.timer,
                )
            }
        }
    }
}
