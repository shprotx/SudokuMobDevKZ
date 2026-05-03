package ru.shprot.sudokumobdevkz.feature.settings.presentation.screen

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
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.screencontent.SettingsScreenContent
import ru.shprot.sudokumobdevkz.feature.settings.presentation.navigation.SettingsRoutes
import ru.shprot.sudokumobdevkz.feature.settings.presentation.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel,
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val showResetDialog by viewModel.showResetDialog.collectAsStateWithLifecycle()

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissResetDialog() },
            title = { Text(stringResource(R.string.reset_statistics) + "?") },
            text = { Text(stringResource(R.string.reset_statistics_confirm)) },
            confirmButton = {
                TextButton(onClick = { viewModel.resetAllStatistics() }) {
                    Text(stringResource(R.string.reset), color = AppTheme.colors.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissResetDialog() }) {
                    Text(stringResource(R.string.cancel))
                }
            },
        )
    }

    SettingsScreenContent(
        settings = settings,
        onSettingChanged = viewModel::updateSetting,
        onNavigateBack = { navController.popBackStack() },
        onNavigateToPrivacyPolicy = {
            navController.navigate(SettingsRoutes.PrivacyPolicyScreen)
        },
        onResetClick = { viewModel.showResetDialog() },
    )
}
