package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
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
            isHighlighted = isNotesEnabled,
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
    isHighlighted: Boolean = false,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusMedium)
    val borderColor = if (isHighlighted) AppTheme.colors.primary else AppTheme.colors.divider
    val iconTint = if (isHighlighted) AppTheme.colors.primary else AppTheme.colors.iconTint

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .width(72.dp)
                    .height(56.dp)
                    .border(1.dp, borderColor, shape)
                    .clickable(onClick = onClick),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconTint,
                    modifier = Modifier.size(AppTheme.sizes.iconMedium),
                )
            }

            if (badge != null) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 4.dp, y = (-4).dp),
                    shape = RoundedCornerShape(8.dp),
                    color = AppTheme.colors.primary,
                ) {
                    Text(
                        modifier = Modifier.padding(
                            horizontal = 5.dp,
                            vertical = 1.dp,
                        ),
                        text = badge,
                        style = AppTheme.typography.caption2,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.colors.textOnPrimary,
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
