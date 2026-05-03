package ru.shprot.sudokumobdevkz.feature.statistic.presentation.screen

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.components.screencontent.StatisticScreenContent
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.contract.StatisticUIEvent
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.viewmodel.StatisticViewModel

@Composable
fun StatisticScreen(
    navController: NavController,
    viewModel: StatisticViewModel,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val tabs = listOf(
        stringResource(R.string.difficulty_easy),
        stringResource(R.string.difficulty_middle),
        stringResource(R.string.difficulty_expert),
    )

    if (state.showResetDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.setEvent(StatisticUIEvent.DismissResetDialog) },
            title = { Text(stringResource(R.string.reset_statistics) + "?") },
            text = { Text(stringResource(R.string.reset_statistics_diff_confirm, tabs[state.selectedTab])) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.setEvent(StatisticUIEvent.ResetRequested(state.selectedTab))
                    viewModel.setEvent(StatisticUIEvent.DismissResetDialog)
                }) {
                    Text(stringResource(R.string.reset), color = AppTheme.colors.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.setEvent(StatisticUIEvent.DismissResetDialog) }) {
                    Text(stringResource(R.string.cancel))
                }
            },
        )
    }

    StatisticScreenContent(
        state = state,
        tabs = tabs,
        onEvent = viewModel::setEvent,
        onNavigateBack = { navController.popBackStack() },
        onResetClick = { viewModel.setEvent(StatisticUIEvent.ShowResetDialog) },
    )
}
