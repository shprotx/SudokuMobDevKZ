package ru.shprot.sudokumobdevkz.feature.settings.presentation.screen

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
import ru.shprot.sudokumobdevkz.feature.settings.presentation.viewmodel.SettingsViewModel
import ru.shprot.sudokumobdevkz.core.base.data.repository.AppSettings

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToPrivacyPolicy: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    var showResetDialog by rememberSaveable { mutableStateOf(false) }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Сбросить статистику?") },
            text = { Text("Статистика для всех сложностей будет удалена безвозвратно.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.resetAllStatistics()
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

    SettingsScreenContent(
        settings = settings,
        onSettingChanged = viewModel::updateSetting,
        onNavigateBack = onNavigateBack,
        onNavigateToPrivacyPolicy = onNavigateToPrivacyPolicy,
        onResetClick = { showResetDialog = true },
    )
}
