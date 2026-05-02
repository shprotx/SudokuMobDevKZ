package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
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
            label = "Заметки",
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
    val shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusMedium)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .width(72.dp)
                .height(56.dp)
                .border(1.dp, AppTheme.colors.divider, shape),
            contentAlignment = Alignment.Center,
        ) {
            IconButton(onClick = onClick) {
                if (badge != null) {
                    BadgedBox(
                        badge = {
                            Badge(
                                containerColor = AppTheme.colors.primary,
                                contentColor = AppTheme.colors.textOnPrimary,
                            ) {
                                Text(
                                    text = badge,
                                    style = AppTheme.typography.caption2,
                                )
                            }
                        },
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = AppTheme.colors.iconTint,
                            modifier = Modifier.size(AppTheme.sizes.iconMedium),
                        )
                    }
                } else {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = AppTheme.colors.iconTint,
                        modifier = Modifier.size(AppTheme.sizes.iconMedium),
                    )
                }
            }
        }

        Text(
            modifier = Modifier.padding(top = AppTheme.paddings.small),
            text = label,
            style = AppTheme.typography.caption2,
            color = AppTheme.colors.textSecondary,
        )
    }
}
