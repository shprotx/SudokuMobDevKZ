package ru.shprot.sudokumobdevkz.feature.menu.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.icon.AppIcons

@Composable
fun MenuNavigationCards(
    modifier: Modifier,
    onStatisticClick: () -> Unit,
    onAchievementsClick: () -> Unit,
    onHowToPlayClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(AppTheme.paddings.default),
    ) {
        MenuNavCard(
            icon = AppIcons.BarChart,
            title = stringResource(R.string.statistic),
            subtitle = stringResource(R.string.statistic_desc),
            onClick = onStatisticClick,
        )

        MenuNavCard(
            icon = AppIcons.Trophy,
            title = stringResource(R.string.achievements),
            subtitle = stringResource(R.string.achievements_desc),
            onClick = onAchievementsClick,
        )

        MenuNavCard(
            icon = AppIcons.Book,
            title = stringResource(R.string.how_to_play),
            subtitle = stringResource(R.string.how_to_play_desc),
            onClick = onHowToPlayClick,
        )

        MenuNavCard(
            icon = AppIcons.Settings,
            title = stringResource(R.string.settings),
            subtitle = stringResource(R.string.settings_desc),
            onClick = onSettingsClick,
        )
    }
}