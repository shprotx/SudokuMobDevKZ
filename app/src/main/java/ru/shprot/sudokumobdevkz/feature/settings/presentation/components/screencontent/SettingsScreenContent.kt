package ru.shprot.sudokumobdevkz.feature.settings.presentation.components.screencontent

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.SettingsLockedDialog
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.SettingsResetDialog
import ru.shprot.sudokumobdevkz.feature.settings.presentation.contract.SettingsUIEvent
import ru.shprot.sudokumobdevkz.feature.settings.presentation.contract.SettingsUIState

@Composable
fun SettingsScreenContent(
    uiState: SettingsUIState,
    onEvent: (SettingsUIEvent) -> Unit,
) {
    when (LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_LANDSCAPE ->
            SettingsLandscapeContent(uiState = uiState, onEvent = onEvent)

        else ->
            SettingsPortraitContent(uiState = uiState, onEvent = onEvent)
    }

    if (uiState.showResetDialog) {
        SettingsResetDialog(
            onConfirm = { onEvent(SettingsUIEvent.ResetConfirmed) },
            onDismiss = { onEvent(SettingsUIEvent.DismissResetDialog) },
        )
    }

    if (uiState.showLockedSettingDialog) {
        SettingsLockedDialog(
            onDismiss = { onEvent(SettingsUIEvent.DismissLockedDialog) },
        )
    }
}
