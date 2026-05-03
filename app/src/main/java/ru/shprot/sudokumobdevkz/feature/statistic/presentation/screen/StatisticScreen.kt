package ru.shprot.sudokumobdevkz.feature.statistic.presentation.screen

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
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

    StatisticScreenContent(
        state = state,
        onEvent = viewModel::setEvent,
        onNavigateBack = onNavigateBack,
        onResetClick = { showResetDialog = true },
    )
}
