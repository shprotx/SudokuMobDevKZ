package ru.shprot.sudokumobdevkz.feature.menu.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun MenuNavigationCards(
    modifier: Modifier,
    onStatisticClick: () -> Unit,
    onHowToPlayClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(AppTheme.paddings.default),
    ) {
        MenuNavCard(
            icon = Icons.Filled.BarChart,
            title = stringResource(R.string.statistic),
            subtitle = stringResource(R.string.statistic_desc),
            onClick = onStatisticClick,
        )

        MenuNavCard(
            icon = Icons.AutoMirrored.Filled.MenuBook,
            title = stringResource(R.string.how_to_play),
            subtitle = stringResource(R.string.how_to_play_desc),
            onClick = onHowToPlayClick,
        )

        // TODO: Achievements — uncomment when implemented
        // MenuNavCard(
        //     icon = Icons.Filled.Star,
        //     title = stringResource(R.string.achievements),
        //     subtitle = stringResource(R.string.achievements_desc),
        //     onClick = { },
        // )

        MenuNavCard(
            icon = Icons.Filled.Settings,
            title = stringResource(R.string.settings),
            subtitle = stringResource(R.string.settings_desc),
            onClick = onSettingsClick,
        )
    }
}

@Composable
internal fun MenuNavCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.backgroundCard),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.paddings.large),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(AppTheme.sizes.cornerRadiusMedium))
                    .background(AppTheme.colors.primaryLight),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = AppTheme.colors.primary,
                    modifier = Modifier.size(AppTheme.sizes.iconMedium),
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
                    text = subtitle,
                    style = AppTheme.typography.caption1,
                    color = AppTheme.colors.textSecondary,
                )
            }

            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = AppTheme.colors.textSecondary,
            )
        }
    }
}
