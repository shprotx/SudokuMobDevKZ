package ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun StreakBadge(
    modifier: Modifier,
    streak: Int,
    label: String,
    icon: ImageVector,
    iconTint: Color,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.backgroundCard),
        elevation = CardDefaults.cardElevation(defaultElevation = AppTheme.sizes.elevationSmall),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.paddings.large),
        ) {
            Icon(
                modifier = Modifier.size(AppTheme.sizes.iconMedium),
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
            )

            Text(
                modifier = Modifier.padding(top = AppTheme.paddings.medium),
                text = label,
                style = AppTheme.typography.caption1,
                color = AppTheme.colors.textSecondary,
            )

            Text(
                modifier = Modifier.padding(top = AppTheme.paddings.small),
                text = streak.toString(),
                style = AppTheme.typography.statValue,
                color = AppTheme.colors.text,
            )
        }
    }
}
