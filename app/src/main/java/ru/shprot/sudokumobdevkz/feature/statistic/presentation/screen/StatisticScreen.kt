package ru.shprot.sudokumobdevkz.feature.statistic.presentation.screen

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.contract.StatisticEvent
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.viewmodel.StatisticViewModel

@Composable
fun StatisticScreen(
    onNavigateBack: () -> Unit,
    viewModel: StatisticViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showResetDialog by rememberSaveable { mutableStateOf(false) }

    val tabs = listOf(
        stringResource(R.string.difficulty_easy),
        stringResource(R.string.difficulty_middle),
        stringResource(R.string.difficulty_expert),
    )

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text(stringResource(R.string.reset_statistics) + "?") },
            text = { Text(stringResource(R.string.reset_statistics_diff_confirm, tabs[state.selectedTab])) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.setEvent(StatisticEvent.ResetRequested(state.selectedTab))
                    showResetDialog = false
                }) {
                    Text(stringResource(R.string.reset), color = AppTheme.colors.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            },
        )
    }

    StatisticScreenContent(
        state = state,
        tabs = tabs,
        onEvent = viewModel::setEvent,
        onNavigateBack = onNavigateBack,
        onResetClick = { showResetDialog = true },
    )
}
