package ru.shprot.sudokumobdevkz.feature.statistic.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.components.DifficultyTabs
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.components.GameStatisticsSection
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.components.OverviewCards
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.components.StatisticToolbar
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.components.TimeChartSection
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.contract.StatisticEvent
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.viewmodel.StatisticViewModel

private val tabs = listOf("Лёгкая", "Средняя", "Экспертная")

@Composable
fun StatisticScreen(
    onNavigateBack: () -> Unit,
    viewModel: StatisticViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showResetDialog by rememberSaveable { mutableStateOf(false) }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Сбросить статистику?") },
            text = { Text("Статистика для сложности \"${tabs[state.selectedTab]}\" будет удалена.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.setEvent(StatisticEvent.ResetRequested(state.selectedTab))
                    showResetDialog = false
                }) {
                    Text("Сбросить", color = AppTheme.colors.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Отмена")
                }
            },
        )
    }

    Scaffold(containerColor = AppTheme.colors.background) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        ) {
            StatisticToolbar(onBackClick = onNavigateBack)

            DifficultyTabs(
                tabs = tabs,
                selectedTab = state.selectedTab,
                onTabSelected = { viewModel.setEvent(StatisticEvent.TabSelected(it)) },
            )

            Column(modifier = Modifier.padding(horizontal = AppTheme.paddings.large)) {
                Text(
                    modifier = Modifier.padding(top = AppTheme.paddings.extraLarge),
                    text = "Обзор",
                    style = AppTheme.typography.h4,
                    color = AppTheme.colors.text,
                )

                OverviewCards(
                    modifier = Modifier.padding(top = AppTheme.paddings.default),
                    bestTime = state.bestTime,
                    averageTime = state.averageTime,
                    percentOfWins = state.percentOfWins,
                    winsWithoutErrors = state.winsWithoutErrors,
                )

                GameStatisticsSection(
                    modifier = Modifier.padding(top = AppTheme.paddings.xxl),
                    gamesStarted = state.gamesStarted,
                    gamesWon = state.gamesWon,
                    percentOfWins = state.percentOfWins,
                    winsWithoutErrors = state.winsWithoutErrors,
                    bestWinsLine = state.bestWinsLine,
                    currentWinsLine = state.currentWinsLine,
                )

                TimeChartSection(
                    modifier = Modifier.padding(top = AppTheme.paddings.xxl),
                    recentGames = state.recentGames,
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

                    TextButton(onClick = { showResetDialog = true }) {
                        Text(
                            text = "Сбросить статистику",
                            style = AppTheme.typography.body3,
                            color = AppTheme.colors.textSecondary,
                        )
                    }
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = AppTheme.paddings.default,
                            bottom = AppTheme.paddings.xxxl,
                        ),
                    onClick = onNavigateBack,
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
                        text = "На главную",
                        style = AppTheme.typography.button,
                        color = AppTheme.colors.textOnPrimary,
                    )
                }
            }
        }
    }
}
