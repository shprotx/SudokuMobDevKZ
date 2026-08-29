package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
internal fun ActionButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    badge: String? = null,
    isHighlighted: Boolean = false,
    contentDescription: String = label,
    onClick: () -> Unit,
) {
    val iconTint = when (isHighlighted) {
        true -> AppTheme.colors.primary
        false -> AppTheme.colors.iconTint
    }
    val labelColor = when (isHighlighted) {
        true -> AppTheme.colors.primary
        false -> AppTheme.colors.textSecondary
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(AppTheme.sizes.cornerRadiusMedium))
            .clickable(onClick = onClick)
            .padding(
                horizontal = AppTheme.paddings.medium,
                vertical = AppTheme.paddings.small,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box {
            Icon(
                modifier = Modifier.size(AppTheme.sizes.iconMedium),
                imageVector = icon,
                contentDescription = contentDescription,
                tint = iconTint,
            )

            if (badge != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(
                            x = AppTheme.paddings.small,
                            y = -AppTheme.paddings.small,
                        )
                        .defaultMinSize(
                            minWidth = AppTheme.sizes.badgeSize,
                            minHeight = AppTheme.sizes.badgeSize,
                        )
                        .clip(CircleShape)
                        .background(AppTheme.colors.primary)
                        .padding(horizontal = AppTheme.paddings.extraSmall),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = badge,
                        style = AppTheme.typography.caption2,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.colors.textOnPrimary,
                    )
                }
            }
        }

        Text(
            modifier = Modifier.padding(top = AppTheme.paddings.extraSmall),
            text = label,
            style = AppTheme.typography.caption2,
            color = labelColor,
        )
    }
}