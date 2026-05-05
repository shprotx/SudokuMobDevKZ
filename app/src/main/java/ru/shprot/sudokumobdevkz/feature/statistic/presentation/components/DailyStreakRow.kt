package ru.shprot.sudokumobdevkz.feature.statistic.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

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
            icon = Icons.Filled.LocalFireDepartment,
            iconTint = Color(0xFFFF9500),
            label = stringResource(R.string.daily_streak_label),
            value = currentStreak.toString(),
        )

        OverviewCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Filled.MilitaryTech,
            iconTint = AppTheme.colors.primary,
            label = stringResource(R.string.daily_best_streak_label),
            value = bestStreak.toString(),
        )
    }
}