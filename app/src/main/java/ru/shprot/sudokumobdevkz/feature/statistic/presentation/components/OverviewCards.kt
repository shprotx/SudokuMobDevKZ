package ru.shprot.sudokumobdevkz.feature.statistic.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun OverviewCards(
    modifier: Modifier,
    bestTime: String = "--:--",
    averageTime: String = "--:--",
    percentOfWins: String = "0%",
    winsWithoutErrors: String = "0",
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(AppTheme.paddings.default),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.default),
        ) {
            OverviewCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Filled.Timer,
                iconTint = Color(0xFF039FE0),
                label = stringResource(R.string.best_time_label),
                value = bestTime,
            )

            OverviewCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Filled.Schedule,
                iconTint = Color(0xFF636AE8),
                label = stringResource(R.string.average_time_label),
                value = averageTime,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.default),
        ) {
            OverviewCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Filled.EmojiEvents,
                iconTint = Color(0xFFFF9500),
                label = stringResource(R.string.win_percent_label),
                value = percentOfWins,
            )

            OverviewCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Filled.CheckCircle,
                iconTint = AppTheme.colors.primary,
                label = stringResource(R.string.wins_no_errors_label),
                value = winsWithoutErrors,
            )
        }
    }
}

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
