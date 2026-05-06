package ru.shprot.sudokumobdevkz.feature.achievements.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.core.base.domain.achievement.AchievementState
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
internal fun AchievementCard(
    modifier: Modifier,
    state: AchievementState,
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(AppTheme.paddings.medium),
        text = stringResource(state.achievement.titleRes),
        style = AppTheme.typography.body2,
        color = AppTheme.colors.text,
    )
}