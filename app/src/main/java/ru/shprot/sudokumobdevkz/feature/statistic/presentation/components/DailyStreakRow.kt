package ru.shprot.sudokumobdevkz.feature.statistic.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.icon.AppIcons

@Composable
fun DailyStreakRow(
    modifier: Modifier,
    currentStreak: Int,
    bestStreak: Int,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.default),
    ) {
        OverviewCard(
            modifier = Modifier.weight(1f),
            icon = AppIcons.Fire,
            iconTint = AppTheme.colors.warning,
            label = stringResource(R.string.daily_streak_label),
            value = currentStreak.toString(),
        )

        OverviewCard(
            modifier = Modifier.weight(1f),
            icon = AppIcons.Medal,
            iconTint = AppTheme.colors.primary,
            label = stringResource(R.string.daily_best_streak_label),
            value = bestStreak.toString(),
        )
    }
}