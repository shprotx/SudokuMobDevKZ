package ru.shprot.sudokumobdevkz.feature.gameover.presentation.components

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconTint: Color,
    label: String,
    value: String,
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
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(AppTheme.sizes.iconMedium),
            )

            Text(
                modifier = Modifier.padding(top = AppTheme.paddings.medium),
                text = value,
                style = AppTheme.typography.h2,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.text,
            )

            Text(
                modifier = Modifier.padding(top = AppTheme.paddings.small),
                text = label,
                style = AppTheme.typography.caption1,
                color = AppTheme.colors.textSecondary,
            )
        }
    }
}
