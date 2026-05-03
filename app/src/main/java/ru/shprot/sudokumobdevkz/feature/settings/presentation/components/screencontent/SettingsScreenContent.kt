package ru.shprot.sudokumobdevkz.feature.settings.presentation.components.screencontent
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.settings.presentation.contract.SettingsUIEvent
import ru.shprot.sudokumobdevkz.feature.settings.presentation.contract.SettingsUIState

@Composable
fun SettingsScreenContent(
    uiState: SettingsUIState,
    onEvent: (SettingsUIEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val settings = uiState.settings

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .verticalScroll(rememberScrollState()),
    ) {
        SettingsToolbar(onBackClick = { onEvent(SettingsUIEvent.BackClicked) })

        Column(modifier = Modifier.padding(horizontal = AppTheme.paddings.large)) {

            SettingsSectionHeader(title = stringResource(R.string.game_label))

            SettingsCard {
                SettingsToggleItem(
                    icon = Icons.Filled.CheckCircle,
                    title = stringResource(R.string.check_errors),
                    checked = settings.checkErrors,
                    onCheckedChange = { v ->
                        onEvent(SettingsUIEvent.SettingChanged { copy(checkErrors = v) })
                    },
                )

                SettingsDivider()

                SettingsToggleItem(
                    icon = Icons.Filled.ContentCopy,
                    title = stringResource(R.string.highlight_duplicates),
                    checked = settings.highlightDuplicates,
                    onCheckedChange = { v ->
                        onEvent(SettingsUIEvent.SettingChanged { copy(highlightDuplicates = v) })
                    },
                )

                SettingsDivider()

                SettingsToggleItem(
                    icon = Icons.Filled.Save,
                    title = stringResource(R.string.auto_save),
                    checked = settings.autoSave,
                    onCheckedChange = { v ->
                        onEvent(SettingsUIEvent.SettingChanged { copy(autoSave = v) })
                    },
                )

                SettingsDivider()

                SettingsToggleItem(
                    icon = Icons.Filled.Schedule,
                    title = stringResource(R.string.show_timer),
                    checked = settings.showTimer,
                    onCheckedChange = { v ->
                        onEvent(SettingsUIEvent.SettingChanged { copy(showTimer = v) })
                    },
                )

                SettingsDivider()

                SettingsToggleItem(
                    icon = Icons.Filled.Favorite,
                    iconTint = AppTheme.colors.error,
                    title = stringResource(R.string.show_errors),
                    checked = settings.showErrors,
                    onCheckedChange = { v ->
                        onEvent(SettingsUIEvent.SettingChanged { copy(showErrors = v) })
                    },
                )

                SettingsDivider()

                SettingsToggleItem(
                    icon = Icons.Filled.Favorite,
                    iconTint = AppTheme.colors.warning,
                    title = stringResource(R.string.unlimited_errors),
                    checked = settings.unlimitedErrors,
                    onCheckedChange = { v ->
                        onEvent(SettingsUIEvent.SettingChanged { copy(unlimitedErrors = v) })
                    },
                )

                SettingsDivider()

                SettingsToggleItem(
                    icon = Icons.Filled.Lightbulb,
                    iconTint = AppTheme.colors.warning,
                    title = stringResource(R.string.unlimited_hints),
                    checked = settings.unlimitedHints,
                    onCheckedChange = { v ->
                        onEvent(SettingsUIEvent.SettingChanged { copy(unlimitedHints = v) })
                    },
                )

                SettingsDivider()

                SettingsToggleItem(
                    icon = Icons.Filled.BarChart,
                    title = stringResource(R.string.track_statistics),
                    checked = settings.effectiveTrackStatistics,
                    enabled = !settings.hasCheats,
                    onCheckedChange = { v ->
                        onEvent(SettingsUIEvent.SettingChanged { copy(trackStatistics = v) })
                    },
                )
            }

            SettingsSectionHeader(title = stringResource(R.string.appearance))

            SettingsCard {
                SettingsToggleItem(
                    icon = Icons.Filled.Palette,
                    title = stringResource(R.string.dark_theme_label),
                    checked = settings.isDarkTheme,
                    onCheckedChange = { v ->
                        onEvent(SettingsUIEvent.SettingChanged { copy(isDarkTheme = v) })
                    },
                )
            }

            SettingsSectionHeader(title = stringResource(R.string.sound))

            SettingsCard {
                SettingsToggleItem(
                    icon = Icons.Filled.VolumeUp,
                    title = stringResource(R.string.sounds),
                    checked = settings.soundsEnabled,
                    onCheckedChange = { v ->
                        onEvent(SettingsUIEvent.SettingChanged { copy(soundsEnabled = v) })
                    },
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

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = AppTheme.paddings.xxl,
                        bottom = AppTheme.paddings.xxxl,
                    ),
                onClick = { onEvent(SettingsUIEvent.ShowResetDialog) },
                shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.error),
            ) {
                Text(
                    text = stringResource(R.string.reset_statistics),
                    style = AppTheme.typography.button,
                    color = AppTheme.colors.textOnPrimary,
                )
            }
        }
    }
}
