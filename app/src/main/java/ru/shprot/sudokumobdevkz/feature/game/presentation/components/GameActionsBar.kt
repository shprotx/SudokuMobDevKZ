package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
    stretched: Boolean = false,
    onUndoClick: () -> Unit,
    onEraseClick: () -> Unit,
    onNotesClick: () -> Unit,
    onHintClick: () -> Unit,
) {
    if (stretched) {
        val spacing = AppTheme.paddings.small
        BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
            val sidePadding = (maxWidth + spacing) / 10
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = sidePadding),
                horizontalArrangement = Arrangement.spacedBy(spacing),
            ) {
                ActionButtonsContent(
                    isNotesEnabled = isNotesEnabled,
                    hintsRemaining = hintsRemaining,
                    stretched = true,
                    onUndoClick = onUndoClick,
                    onEraseClick = onEraseClick,
                    onNotesClick = onNotesClick,
                    onHintClick = onHintClick,
                )
            }
        }
    } else {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            ActionButtonsContent(
                isNotesEnabled = isNotesEnabled,
                hintsRemaining = hintsRemaining,
                stretched = false,
                onUndoClick = onUndoClick,
                onEraseClick = onEraseClick,
                onNotesClick = onNotesClick,
                onHintClick = onHintClick,
            )
        }
    }
}

@Composable
internal fun RowScope.ActionButtonsContent(
    isNotesEnabled: Boolean,
    hintsRemaining: Int,
    stretched: Boolean,
    onUndoClick: () -> Unit,
    onEraseClick: () -> Unit,
    onNotesClick: () -> Unit,
    onHintClick: () -> Unit,
) {
    val slotModifier: @Composable () -> Modifier = {
        if (stretched) Modifier.weight(1f) else Modifier
    }

    ActionButton(
        modifier = slotModifier(),
        icon = Icons.AutoMirrored.Filled.Undo,
        label = stringResource(R.string.undo),
        stretched = stretched,
        onClick = onUndoClick,
    )

    ActionButton(
        modifier = slotModifier(),
        icon = Icons.Outlined.Delete,
        label = stringResource(R.string.erase),
        stretched = stretched,
        onClick = onEraseClick,
    )

    ActionButton(
        modifier = slotModifier(),
        icon = Icons.Filled.Edit,
        label = stringResource(R.string.note),
        stretched = stretched,
        badge = if (isNotesEnabled) stringResource(R.string.on_label) else stringResource(R.string.off_label),
        isHighlighted = isNotesEnabled,
        onClick = onNotesClick,
    )

    ActionButton(
        modifier = slotModifier(),
        icon = Icons.Filled.Lightbulb,
        label = stringResource(R.string.hint),
        stretched = stretched,
        badge = hintsRemaining.toString(),
        onClick = onHintClick,
    )
}
