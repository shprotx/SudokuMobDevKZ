package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.shprot.sudokumobdevkz.core.base.domain.model.ActionButtonId
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.glass.glassBar

@Composable
fun GameActionsBar(
    modifier: Modifier,
    order: List<ActionButtonId>,
    isNotesEnabled: Boolean,
    hintsRemaining: Int,
    isHintModeActive: Boolean = false,
    stretched: Boolean = false,
    onUndoClick: () -> Unit,
    onEraseClick: () -> Unit,
    onNotesClick: () -> Unit,
    onHintClick: () -> Unit,
) {
    val clickFor: (ActionButtonId) -> (() -> Unit) = { buttonId ->
        when (buttonId) {
            ActionButtonId.UNDO -> onUndoClick
            ActionButtonId.ERASE -> onEraseClick
            ActionButtonId.NOTE -> onNotesClick
            ActionButtonId.HINT -> onHintClick
        }
    }

    val barShape = RoundedCornerShape(AppTheme.sizes.cornerRadiusFull)

    if (stretched) {
        val spacing = AppTheme.paddings.small
        BoxWithConstraints(
            modifier = modifier
                .fillMaxWidth()
                .glassBar(shape = barShape)
                .padding(
                    horizontal = AppTheme.paddings.small,
                    vertical = AppTheme.paddings.small,
                ),
        ) {
            val sidePadding = (maxWidth + spacing) / 10
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = sidePadding),
                horizontalArrangement = Arrangement.spacedBy(spacing),
            ) {
                order.forEach { buttonId ->
                    ActionButtonContent(
                        modifier = Modifier.weight(1f),
                        buttonId = buttonId,
                        isNotesEnabled = isNotesEnabled,
                        hintsRemaining = hintsRemaining,
                        isHintModeActive = isHintModeActive,
                        onClick = clickFor(buttonId),
                    )
                }
            }
        }
    } else {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .glassBar(shape = barShape)
                .padding(
                    horizontal = AppTheme.paddings.medium,
                    vertical = AppTheme.paddings.small,
                ),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            order.forEach { buttonId ->
                ActionButtonContent(
                    modifier = Modifier,
                    buttonId = buttonId,
                    isNotesEnabled = isNotesEnabled,
                    hintsRemaining = hintsRemaining,
                    isHintModeActive = isHintModeActive,
                    onClick = clickFor(buttonId),
                )
            }
        }
    }
}
