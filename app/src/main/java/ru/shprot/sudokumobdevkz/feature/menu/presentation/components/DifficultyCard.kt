package ru.shprot.sudokumobdevkz.feature.menu.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
internal fun DifficultyCard(
    modifier: Modifier,
    title: String,
    subtitle: String,
    icon: String,
    dotCount: Int,
    dotColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val bgColor = if (isSelected)
        AppTheme.colors.primaryLight
    else
        AppTheme.colors.backgroundCard

    Card(
        modifier = modifier
            .shadow(
                elevation = if (isSelected) AppTheme.sizes.elevationMedium else AppTheme.sizes.elevationSmall,
                shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = AppTheme.sizes.elevationSmall),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = AppTheme.paddings.large,
                    horizontal = AppTheme.paddings.medium,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = icon, style = AppTheme.typography.h2)

            Text(
                modifier = Modifier.padding(top = AppTheme.paddings.medium),
                text = title,
                style = AppTheme.typography.body2,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.colors.text,
            )

            Text(
                text = subtitle,
                style = AppTheme.typography.caption2,
                color = AppTheme.colors.textSecondary,
            )

            Row(
                modifier = Modifier.padding(top = AppTheme.paddings.medium),
                horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.small),
            ) {
                repeat(4) { index ->
                    Box(
                        modifier = Modifier
                            .size(AppTheme.sizes.difficultyDot)
                            .clip(CircleShape)
                            .background(
                                if (index < dotCount) dotColor
                                else AppTheme.colors.divider
                            ),
                    )
                }
            }
        }
    }
}