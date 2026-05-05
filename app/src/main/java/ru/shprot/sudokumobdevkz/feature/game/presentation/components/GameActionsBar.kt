package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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

@Composable
fun GameActionsBar(
    modifier: Modifier,
    isNotesEnabled: Boolean,
    hintsRemaining: Int,
    onUndoClick: () -> Unit,
    onEraseClick: () -> Unit,
    onNotesClick: () -> Unit,
    onHintClick: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        ActionButton(
            icon = Icons.AutoMirrored.Filled.Undo,
            label = stringResource(R.string.undo),
            onClick = onUndoClick,
        )

        ActionButton(
            icon = Icons.Outlined.Delete,
            label = stringResource(R.string.erase),
            onClick = onEraseClick,
        )

        ActionButton(
            icon = Icons.Filled.Edit,
            label = stringResource(R.string.note),
            badge = if (isNotesEnabled) stringResource(R.string.on_label) else stringResource(R.string.off_label),
            isHighlighted = isNotesEnabled,
            onClick = onNotesClick,
        )

        ActionButton(
            icon = Icons.Filled.Lightbulb,
            label = stringResource(R.string.hint),
            badge = hintsRemaining.toString(),
            onClick = onHintClick,
        )
    }
}
