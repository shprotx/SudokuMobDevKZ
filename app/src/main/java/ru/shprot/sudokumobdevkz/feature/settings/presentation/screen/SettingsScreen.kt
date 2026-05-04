package ru.shprot.sudokumobdevkz.feature.settings.presentation.screen

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.screencontent.SettingsScreenContent
import ru.shprot.sudokumobdevkz.feature.settings.presentation.contract.SettingsUIEffect
import ru.shprot.sudokumobdevkz.feature.settings.presentation.contract.SettingsUIEvent
import ru.shprot.sudokumobdevkz.feature.settings.presentation.navigation.SettingsRoutes
import ru.shprot.sudokumobdevkz.feature.settings.presentation.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                SettingsUIEffect.NavigateBack ->
                    navController.popBackStack()

                SettingsUIEffect.NavigateToPrivacyPolicy ->
                    navController.navigate(SettingsRoutes.PrivacyPolicyScreen)
            }
        }
    }

    if (state.showResetDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.setEvent(SettingsUIEvent.DismissResetDialog) },
            title = { Text(stringResource(R.string.reset_statistics) + "?") },
            text = { Text(stringResource(R.string.reset_statistics_confirm)) },
            confirmButton = {
                TextButton(onClick = { viewModel.setEvent(SettingsUIEvent.ResetConfirmed) }) {
                    Text(stringResource(R.string.reset), color = AppTheme.colors.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.setEvent(SettingsUIEvent.DismissResetDialog) }) {
                    Text(stringResource(R.string.cancel))
                }
            },
        )
    }

    if (state.showLockedSettingDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.setEvent(SettingsUIEvent.DismissLockedDialog) },
            title = { Text(stringResource(R.string.settings)) },
            text = { Text(stringResource(R.string.settings_locked_during_game)) },
            confirmButton = {
                TextButton(onClick = { viewModel.setEvent(SettingsUIEvent.DismissLockedDialog) }) {
                    Text("OK")
                }
            },
        )
    }

    SettingsScreenContent(
        uiState = state,
        onEvent = viewModel::setEvent,
    )
}
