package ru.shprot.sudokumobdevkz.feature.achievements.presentation.components

import androidx.annotation.StringRes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
internal fun AchievementsSectionTitle(
    modifier: Modifier,
    @StringRes titleRes: Int,
    count: Int,
) {
    Text(
        modifier = modifier,
        text = stringResource(titleRes) + "  ·  $count",
        style = AppTheme.typography.h3,
        color = AppTheme.colors.text,
    )
}