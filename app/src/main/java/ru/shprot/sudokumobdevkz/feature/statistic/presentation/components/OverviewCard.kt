package ru.shprot.sudokumobdevkz.feature.statistic.presentation.components

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
internal fun OverviewCard(
    modifier: Modifier,
    icon: ImageVector,
    iconTint: Color,
    label: String,
    value: String,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.backgroundCard),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.paddings.large),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(AppTheme.sizes.iconMedium),
            )

            Text(
                modifier = Modifier.padding(top = AppTheme.paddings.medium),
                text = label,
                style = AppTheme.typography.caption1,
                color = AppTheme.colors.textSecondary,
            )

            Text(
                modifier = Modifier.padding(top = AppTheme.paddings.small),
                text = value,
                style = AppTheme.typography.statValue,
                color = AppTheme.colors.text,
            )
        }
    }
}
