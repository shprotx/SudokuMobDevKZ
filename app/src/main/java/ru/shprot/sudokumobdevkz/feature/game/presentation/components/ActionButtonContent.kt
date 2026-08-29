package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.domain.model.ActionButtonId
import ru.shprot.sudokumobdevkz.core.uicommon.icon.AppIcons

@Composable
internal fun ActionButtonContent(
    modifier: Modifier,
    buttonId: ActionButtonId,
    isNotesEnabled: Boolean,
    hintsRemaining: Int,
    isHintModeActive: Boolean,
    onClick: () -> Unit,
) {
    when (buttonId) {
        ActionButtonId.UNDO -> ActionButton(
            modifier = modifier,
            icon = AppIcons.Undo,
            label = stringResource(R.string.undo),
            onClick = onClick,
        )

        ActionButtonId.ERASE -> ActionButton(
            modifier = modifier,
            icon = AppIcons.Erase,
            label = stringResource(R.string.erase),
            onClick = onClick,
        )

        ActionButtonId.NOTE -> ActionButton(
            modifier = modifier,
            icon = AppIcons.Note,
            label = stringResource(R.string.note),
            isHighlighted = isNotesEnabled,
            onClick = onClick,
        )

        ActionButtonId.HINT -> {
            val hintLabel = stringResource(R.string.hint)
            ActionButton(
                modifier = modifier,
                icon = AppIcons.Hint,
                label = hintLabel,
                badge = when (hintsRemaining) {
                    Int.MAX_VALUE -> null
                    else -> hintsRemaining.toString()
                },
                isHighlighted = isHintModeActive,
                contentDescription = when (isHintModeActive) {
                    true -> stringResource(R.string.hint_mode_active_description)
                    false -> hintLabel
                },
                onClick = onClick,
            )
        }
    }
}