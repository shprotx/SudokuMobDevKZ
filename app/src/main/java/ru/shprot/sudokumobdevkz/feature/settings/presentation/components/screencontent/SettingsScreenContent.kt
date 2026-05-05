package ru.shprot.sudokumobdevkz.feature.settings.presentation.components.screencontent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.button.ButtonDefault
import ru.shprot.sudokumobdevkz.core.uicommon.toolbar.ToolbarDefault
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.SettingsLockedDialog
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.SettingsResetDialog
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.settings.SettingsCard
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.settings.SettingsDivider
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.settings.SettingsNavItem
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.settings.SettingsSectionHeader
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.settings.SettingsToggleItem
import ru.shprot.sudokumobdevkz.feature.settings.presentation.contract.SettingsUIEvent
import ru.shprot.sudokumobdevkz.feature.settings.presentation.contract.SettingsUIState

@Composable
fun SettingsScreenContent(
    uiState: SettingsUIState,
    onEvent: (SettingsUIEvent) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .verticalScroll(rememberScrollState()),
    ) {

        ToolbarDefault(
            modifier = Modifier,
            title = stringResource(R.string.settings),
            onLeadIconClick = { onEvent(SettingsUIEvent.BackClicked) },
        )

        Column(modifier = Modifier.padding(horizontal = AppTheme.paddings.large)) {

            SettingsSectionHeader(title = stringResource(R.string.game_label))

            SettingsCard {
                SettingsToggleItem(
                    icon = Icons.Filled.CheckCircle,
                    title = stringResource(R.string.check_errors),
                    checked = uiState.settings.checkErrors,
                    onCheckedChange = { onEvent(SettingsUIEvent.ToggleCheckErrors) },
                )

                SettingsDivider()

                SettingsToggleItem(
                    icon = Icons.Filled.ContentCopy,
                    title = stringResource(R.string.highlight_duplicates),
                    checked = uiState.settings.highlightDuplicates,
                    onCheckedChange = { onEvent(SettingsUIEvent.ToggleHighlightDuplicates) },
                )

                SettingsDivider()

                SettingsToggleItem(
                    icon = Icons.Filled.Save,
                    title = stringResource(R.string.auto_save),
                    checked = uiState.settings.autoSave,
                    onCheckedChange = { onEvent(SettingsUIEvent.ToggleAutoSave) },
                )

                SettingsDivider()

                SettingsToggleItem(
                    icon = Icons.Filled.Schedule,
                    title = stringResource(R.string.show_timer),
                    checked = uiState.settings.showTimer,
                    onCheckedChange = { onEvent(SettingsUIEvent.ToggleShowTimer) },
                )

                SettingsDivider()

                SettingsToggleItem(
                    icon = Icons.Filled.Favorite,
                    iconTint = AppTheme.colors.error,
                    title = stringResource(R.string.show_errors),
                    checked = uiState.settings.showErrors,
                    onCheckedChange = { onEvent(SettingsUIEvent.ToggleShowErrors) },
                )

                SettingsDivider()

                SettingsToggleItem(
                    icon = Icons.Filled.Favorite,
                    iconTint = AppTheme.colors.warning,
                    title = stringResource(R.string.unlimited_errors),
                    checked = uiState.settings.unlimitedErrors,
                    onCheckedChange = { onEvent(SettingsUIEvent.ToggleUnlimitedErrors) },
                )

                SettingsDivider()

                SettingsToggleItem(
                    icon = Icons.Filled.Lightbulb,
                    iconTint = AppTheme.colors.warning,
                    title = stringResource(R.string.unlimited_hints),
                    checked = uiState.settings.unlimitedHints,
                    onCheckedChange = { onEvent(SettingsUIEvent.ToggleUnlimitedHints) },
                )

                SettingsDivider()

                SettingsToggleItem(
                    icon = Icons.Filled.BarChart,
                    title = stringResource(R.string.track_statistics),
                    checked = uiState.settings.trackStatistics && uiState.settings.isStandardMode,
                    enabled = uiState.settings.isStandardMode,
                    onCheckedChange = { onEvent(SettingsUIEvent.ToggleTrackStatistics) },
                )
            }

            SettingsSectionHeader(title = stringResource(R.string.appearance))

            SettingsCard {
                SettingsToggleItem(
                    icon = Icons.Filled.Palette,
                    title = stringResource(R.string.dark_theme_label),
                    checked = uiState.settings.isDarkTheme,
                    onCheckedChange = { onEvent(SettingsUIEvent.ToggleDarkTheme) },
                )
            }

            SettingsSectionHeader(title = stringResource(R.string.other))

            SettingsCard {
                SettingsNavItem(
                    icon = Icons.Filled.Security,
                    title = stringResource(R.string.privacy_policy),
                    onClick = { onEvent(SettingsUIEvent.NavigateToPrivacyPolicy) },
                )
            }

            ButtonDefault(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(
                        top = AppTheme.paddings.xxl,
                        bottom = AppTheme.paddings.xxxl,
                    ),
                text = stringResource(R.string.reset_statistics),
                containerColor = AppTheme.colors.error,
                onClick = { onEvent(SettingsUIEvent.ShowResetDialog) },
            )
        }
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
