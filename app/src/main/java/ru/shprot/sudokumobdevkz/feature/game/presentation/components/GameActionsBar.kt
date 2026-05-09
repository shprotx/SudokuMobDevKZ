package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun GameActionsBar(
    modifier: Modifier,
    isNotesEnabled: Boolean,
    hintsRemaining: Int,
    showUndo: Boolean = true,
    stretched: Boolean = false,
    onUndoClick: () -> Unit,
    onEraseClick: () -> Unit,
    onNotesClick: () -> Unit,
    onHintClick: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = if (stretched) {
            Arrangement.spacedBy(AppTheme.paddings.small)
        } else {
            Arrangement.SpaceEvenly
        },
    ) {
        if (showUndo) {
            ActionButtonSlot(stretched = stretched) {
                ActionButton(
                    modifier = it,
                    icon = Icons.AutoMirrored.Filled.Undo,
                    label = stringResource(R.string.undo),
                    stretched = stretched,
                    onClick = onUndoClick,
                )
            }
        }

        ActionButtonSlot(stretched = stretched) {
            ActionButton(
                modifier = it,
                icon = Icons.Outlined.Delete,
                label = stringResource(R.string.erase),
                stretched = stretched,
                onClick = onEraseClick,
            )
        }

        ActionButtonSlot(stretched = stretched) {
            ActionButton(
                modifier = it,
                icon = Icons.Filled.Edit,
                label = stringResource(R.string.note),
                stretched = stretched,
                badge = if (isNotesEnabled) stringResource(R.string.on_label) else stringResource(R.string.off_label),
                isHighlighted = isNotesEnabled,
                onClick = onNotesClick,
            )
        }

        ActionButtonSlot(stretched = stretched) {
            ActionButton(
                modifier = it,
                icon = Icons.Filled.Lightbulb,
                label = stringResource(R.string.hint),
                stretched = stretched,
                badge = hintsRemaining.toString(),
                onClick = onHintClick,
            )
        }
    }
}

@Composable
internal fun RowScope.ActionButtonSlot(
    stretched: Boolean,
    content: @Composable (Modifier) -> Unit,
) {
    val mod = if (stretched) Modifier.weight(1f) else Modifier
    content(mod)
}