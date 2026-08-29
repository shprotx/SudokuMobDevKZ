package ru.shprot.sudokumobdevkz.feature.statistic.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.icon.AppIcons

@Composable
fun OverviewCards(
    modifier: Modifier,
    bestTime: String,
    averageTime: String,
    percentOfWins: String,
    winsWithoutErrors: String,
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
                icon = AppIcons.Stopwatch,
                iconTint = Color(0xFF039FE0),
                label = stringResource(R.string.best_time_label),
                value = bestTime,
            )

            OverviewCard(
                modifier = Modifier.weight(1f),
                icon = AppIcons.Clock,
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
                icon = AppIcons.Trophy,
                iconTint = AppTheme.colors.warning,
                label = stringResource(R.string.win_percent_label),
                value = percentOfWins,
            )

            OverviewCard(
                modifier = Modifier.weight(1f),
                icon = AppIcons.CheckCircle,
                iconTint = AppTheme.colors.primary,
                label = stringResource(R.string.wins_no_errors_label),
                value = winsWithoutErrors,
            )
        }
    }
}
