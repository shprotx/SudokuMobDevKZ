package ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
fun TipCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    description: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge))
            .background(AppTheme.colors.primaryLight)
            .padding(AppTheme.paddings.large),
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            modifier = Modifier
                .size(AppTheme.sizes.iconLarge)
                .clip(RoundedCornerShape(AppTheme.sizes.cornerRadiusMedium))
                .background(AppTheme.colors.primary),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AppTheme.colors.textOnPrimary,
                modifier = Modifier.size(AppTheme.sizes.iconSmall),
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = AppTheme.paddings.default),
        ) {
            Text(
                text = title,
                style = AppTheme.typography.body2,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.colors.text,
            )

            Text(
                modifier = Modifier.padding(top = AppTheme.paddings.small),
                text = description,
                style = AppTheme.typography.caption1,
                color = AppTheme.colors.textSecondary,
            )
        }
    }
}
