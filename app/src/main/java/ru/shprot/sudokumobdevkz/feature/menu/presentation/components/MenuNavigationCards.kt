package ru.shprot.sudokumobdevkz.feature.menu.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
