package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun GameActionsBar(
    modifier: Modifier = Modifier,
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
            label = "Отменить",
            onClick = onUndoClick,
        )

        ActionButton(
            icon = Icons.Outlined.Delete,
            label = "Стереть",
            onClick = onEraseClick,
        )

        ActionButton(
            icon = Icons.Filled.Edit,
            label = if (isNotesEnabled) "Заметки" else "Заметки",
            badge = if (isNotesEnabled) "ON" else "OFF",
            onClick = onNotesClick,
        )

        ActionButton(
            icon = Icons.Filled.Lightbulb,
            label = "Подсказка",
            badge = "$hintsRemaining",
            onClick = onHintClick,
        )
    }
}

@Composable
private fun ActionButton(
    icon: ImageVector,
    label: String,
    badge: String? = null,
    onClick: () -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = AppTheme.colors.iconTint,
                modifier = Modifier.size(AppTheme.sizes.iconMedium),
            )
        }

        Text(
            text = label,
            style = AppTheme.typography.caption2,
            color = AppTheme.colors.textSecondary,
        )

        if (badge != null) {
            Text(
                text = badge,
                style = AppTheme.typography.caption2,
                color = AppTheme.colors.primary,
            )
        }
    }
}
