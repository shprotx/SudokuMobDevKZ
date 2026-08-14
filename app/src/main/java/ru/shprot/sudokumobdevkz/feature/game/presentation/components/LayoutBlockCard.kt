package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragHandle
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
internal fun LayoutBlockCard(
    modifier: Modifier,
    handleModifier: Modifier,
    icon: ImageVector,
    title: String,
    isDragging: Boolean,
) {
    val borderColor = if (isDragging) AppTheme.colors.primary else AppTheme.colors.divider

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge))
            .background(AppTheme.colors.backgroundCard)
            .border(
                width = AppTheme.sizes.dividerThickness,
                color = borderColor,
                shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
            )
            .padding(AppTheme.paddings.large),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(AppTheme.sizes.iconXL)
                .clip(CircleShape)
                .background(AppTheme.colors.primaryLight),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                modifier = Modifier.size(AppTheme.sizes.iconMedium),
                imageVector = icon,
                contentDescription = null,
                tint = AppTheme.colors.primary,
            )
        }

        Text(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = AppTheme.paddings.large),
            text = title,
            style = AppTheme.typography.body1,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.colors.text,
        )

        Icon(
            modifier = handleModifier.size(AppTheme.sizes.iconLarge),
            imageVector = Icons.Filled.DragHandle,
            contentDescription = null,
            tint = AppTheme.colors.textSecondary,
        )
    }
}
