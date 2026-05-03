package ru.shprot.sudokumobdevkz.feature.statistic.presentation.components.screencontent
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.components.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.toolbar.ToolbarDefault
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.contract.StatisticUIEvent
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.contract.StatisticUIState

@Composable
fun StatisticScreenContent(
    uiState: StatisticUIState,
    onEvent: (StatisticUIEvent) -> Unit,
    modifier: Modifier = Modifier,
    tabs: List<String> = emptyList(),
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .verticalScroll(rememberScrollState()),
    ) {

        ToolbarDefault(
            modifier = Modifier,
            title = stringResource(R.string.statistic),
            onLeadIconClick = { onEvent(StatisticUIEvent.BackClicked) },
        )

        DifficultyTabs(
            tabs = tabs,
            selectedTab = uiState.selectedTab,
            onTabSelected = { onEvent(StatisticUIEvent.TabSelected(it)) },
        )

        Column(modifier = Modifier.padding(horizontal = AppTheme.paddings.large)) {
            Text(
                modifier = Modifier.padding(top = AppTheme.paddings.extraLarge),
                text = stringResource(R.string.overview),
                style = AppTheme.typography.h4,
                color = AppTheme.colors.text,
            )

            OverviewCards(
                modifier = Modifier.padding(top = AppTheme.paddings.default),
                bestTime = uiState.bestTime,
                averageTime = uiState.averageTime,
                percentOfWins = uiState.percentOfWins,
                winsWithoutErrors = uiState.winsWithoutErrors,
            )

            GameStatisticsSection(
                modifier = Modifier.padding(top = AppTheme.paddings.xxl),
                gamesStarted = uiState.gamesStarted,
                gamesWon = uiState.gamesWon,
                percentOfWins = uiState.percentOfWins,
                winsWithoutErrors = uiState.winsWithoutErrors,
                bestWinsLine = uiState.bestWinsLine,
                currentWinsLine = uiState.currentWinsLine,
            )

            TimeChartSection(
                modifier = Modifier.padding(top = AppTheme.paddings.xxl),
                recentGames = uiState.recentGames,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.paddings.xxl),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = null,
                    tint = AppTheme.colors.textSecondary,
                    modifier = Modifier.size(AppTheme.sizes.iconSmall),
                )

                TextButton(onClick = { onEvent(StatisticUIEvent.ResetClicked) }) {
                    Text(
                        text = stringResource(R.string.reset_statistics),
                        style = AppTheme.typography.body3,
                        color = AppTheme.colors.textSecondary,
                    )
                }
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(
                        top = AppTheme.paddings.default,
                        bottom = AppTheme.paddings.xxxl,
                    ),
                onClick = { onEvent(StatisticUIEvent.BackClicked) },
                shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.primary),
            ) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = null,
                    tint = AppTheme.colors.textOnPrimary,
                    modifier = Modifier.size(AppTheme.sizes.iconMedium),
                )

                Text(
                    modifier = Modifier.padding(start = AppTheme.paddings.medium),
                    text = stringResource(R.string.go_to_main_page),
                    style = AppTheme.typography.button,
                    color = AppTheme.colors.textOnPrimary,
                )
            }
        }
    }
}
