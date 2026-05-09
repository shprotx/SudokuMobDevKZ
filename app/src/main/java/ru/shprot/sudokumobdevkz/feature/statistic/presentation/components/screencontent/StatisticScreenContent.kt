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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.button.ButtonDefault
import ru.shprot.sudokumobdevkz.core.uicommon.button.ButtonText
import ru.shprot.sudokumobdevkz.core.uicommon.toolbar.ToolbarDefault
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.contract.StatisticUIEvent
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.contract.StatisticUIState

@Composable
fun StatisticScreenContent(
    uiState: StatisticUIState,
    onEvent: (StatisticUIEvent) -> Unit,
) {

    Column(
        modifier = Modifier
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
            modifier = Modifier,
            selectedTab = uiState.selectedTab,
            onTabSelected = { onEvent(StatisticUIEvent.TabSelected(it)) },
        )

        Column(
            modifier = Modifier
                .padding(horizontal = AppTheme.paddings.large)
        ) {
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

            if (uiState.percentile >= 0) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppTheme.paddings.large),
                    text = stringResource(R.string.faster_than, uiState.percentile),
                    style = AppTheme.typography.body2,
                    color = AppTheme.colors.primary,
                    textAlign = TextAlign.Center,
                )
            }

            GameStatisticsSection(
                modifier = Modifier.padding(top = AppTheme.paddings.xxl),
                gamesStarted = uiState.gamesStarted,
                gamesWon = uiState.gamesWon,
                percentOfWins = uiState.percentOfWins,
                winsWithoutErrors = uiState.winsWithoutErrors,
                bestWinsLine = uiState.bestWinsLine,
                currentWinsLine = uiState.currentWinsLine,
                casualGamesPlayed = uiState.casualGamesPlayed,
            )

            TimeChartSection(
                modifier = Modifier.padding(top = AppTheme.paddings.xxl),
                dailyPlaytimes = uiState.dailyPlaytimes,
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

                ButtonText(
                    modifier = Modifier,
                    text = stringResource(R.string.reset_statistics),
                    onClick = { onEvent(StatisticUIEvent.ResetClicked) },
                )
            }

            ButtonDefault(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(
                        top = AppTheme.paddings.default,
                        bottom = AppTheme.paddings.xxxl,
                    ),
                text = stringResource(R.string.go_to_main_page),
                icon = Icons.Filled.Home,
                onClick = { onEvent(StatisticUIEvent.BackClicked) },
            )
        }
    }

    if (uiState.showResetDialog) {
        val difficultyName = when (Difficulty.fromOrdinal(uiState.selectedTab)) {
            Difficulty.EASY -> stringResource(R.string.difficulty_easy)
            Difficulty.MEDIUM -> stringResource(R.string.difficulty_middle)
            Difficulty.HARD -> stringResource(R.string.difficulty_expert)
        }
        StatisticResetDialog(
            difficultyName = difficultyName,
            onConfirm = {
                onEvent(StatisticUIEvent.ResetRequested(uiState.selectedTab))
                onEvent(StatisticUIEvent.DismissResetDialog)
            },
            onDismiss = { onEvent(StatisticUIEvent.DismissResetDialog) },
        )
    }
}
