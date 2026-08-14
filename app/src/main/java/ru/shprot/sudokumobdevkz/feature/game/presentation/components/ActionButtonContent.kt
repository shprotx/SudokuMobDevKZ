package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.domain.model.ActionButtonId

@Composable
internal fun ActionButtonContent(
    modifier: Modifier,
    buttonId: ActionButtonId,
    isNotesEnabled: Boolean,
    hintsRemaining: Int,
    isHintModeActive: Boolean,
    stretched: Boolean,
    onClick: () -> Unit,
) {
    when (buttonId) {
        ActionButtonId.UNDO -> ActionButton(
            modifier = modifier,
            icon = Icons.AutoMirrored.Filled.Undo,
            label = stringResource(R.string.undo),
            stretched = stretched,
            onClick = onClick,
        )

        ActionButtonId.ERASE -> ActionButton(
            modifier = modifier,
            icon = Icons.Outlined.Delete,
            label = stringResource(R.string.erase),
            stretched = stretched,
            onClick = onClick,
        )

        ActionButtonId.NOTE -> ActionButton(
            modifier = modifier,
            icon = Icons.Filled.Edit,
            label = stringResource(R.string.note),
            stretched = stretched,
            badge = if (isNotesEnabled) stringResource(R.string.on_label) else stringResource(R.string.off_label),
            isHighlighted = isNotesEnabled,
            onClick = onClick,
        )

        ActionButtonId.HINT -> {
            val hintLabel = stringResource(R.string.hint)
            ActionButton(
                modifier = modifier,
                icon = Icons.Filled.Lightbulb,
                label = hintLabel,
                stretched = stretched,
                badge = if (hintsRemaining == Int.MAX_VALUE) null else hintsRemaining.toString(),
                isHighlighted = isHintModeActive,
                contentDescription = if (isHintModeActive) {
                    stringResource(R.string.hint_mode_active_description)
                } else {
                    hintLabel
                },
                onClick = onClick,
            )
        }
    }
}
