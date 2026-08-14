package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dialpad
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.domain.model.GameBlockId
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.button.ButtonDefault
import ru.shprot.sudokumobdevkz.core.uicommon.button.ButtonText
import sh.calvin.reorderable.ReorderableColumn

@Composable
internal fun LayoutEditContent(
    modifier: Modifier,
    blockOrder: List<GameBlockId>,
    onBlockMoved: (Int, Int) -> Unit,
    onReset: () -> Unit,
    onDone: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.paddings.large),
    ) {
        Text(
            modifier = Modifier.padding(top = AppTheme.paddings.large),
            text = stringResource(R.string.layout_edit_title),
            style = AppTheme.typography.h2,
            color = AppTheme.colors.text,
        )

        Text(
            modifier = Modifier.padding(top = AppTheme.paddings.small),
            text = stringResource(R.string.layout_edit_subtitle),
            style = AppTheme.typography.body2,
            color = AppTheme.colors.textSecondary,
        )

        ReorderableColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = AppTheme.paddings.extraLarge),
            list = blockOrder,
            verticalArrangement = Arrangement.spacedBy(AppTheme.paddings.medium),
            onSettle = { fromIndex, toIndex ->
                onBlockMoved(fromIndex, toIndex)
            },
        ) { _, blockId, isDragging ->
            key(blockId) {
                LayoutBlockCard(
                    modifier = Modifier,
                    handleModifier = Modifier.draggableHandle(),
                    icon = blockId.editIcon(),
                    title = stringResource(blockId.editTitleRes()),
                    isDragging = isDragging,
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = AppTheme.paddings.extraLarge),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            ButtonText(
                modifier = Modifier,
                text = stringResource(R.string.layout_reset),
                onClick = onReset,
            )

            ButtonDefault(
                modifier = Modifier,
                text = stringResource(R.string.layout_done),
                onClick = onDone,
            )
        }
    }
}

internal fun GameBlockId.editIcon(): ImageVector =
    when (this) {
        GameBlockId.STATUS_BAR -> Icons.Filled.Timer
        GameBlockId.GRID -> Icons.Filled.GridOn
        GameBlockId.NUMBER_PAD -> Icons.Filled.Dialpad
        GameBlockId.ACTIONS_BAR -> Icons.Filled.TouchApp
    }

internal fun GameBlockId.editTitleRes(): Int =
    when (this) {
        GameBlockId.STATUS_BAR -> R.string.layout_block_status
        GameBlockId.GRID -> R.string.layout_block_grid
        GameBlockId.NUMBER_PAD -> R.string.layout_block_number_pad
        GameBlockId.ACTIONS_BAR -> R.string.layout_block_actions
    }
