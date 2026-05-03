package ru.shprot.sudokumobdevkz.feature.settings.presentation.screen

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.SettingsCard
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.SettingsDivider
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.SettingsNavItem
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.SettingsSectionHeader
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.SettingsToggleItem
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.SettingsToolbar
import ru.shprot.sudokumobdevkz.core.base.data.repository.AppSettings

@Composable
fun SettingsScreenContent(
    modifier: Modifier = Modifier,
    settings: AppSettings,
    onSettingChanged: (AppSettings.() -> AppSettings) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToPrivacyPolicy: () -> Unit,
    onResetClick: () -> Unit,
) {
    Scaffold(containerColor = AppTheme.colors.background) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        ) {
            SettingsToolbar(onBackClick = onNavigateBack)

            Column(modifier = Modifier.padding(horizontal = AppTheme.paddings.large)) {

                SettingsSectionHeader(title = stringResource(R.string.game_label))

                SettingsCard {
                    SettingsToggleItem(
                        icon = Icons.Filled.CheckCircle,
                        title = stringResource(R.string.check_errors),
                        checked = settings.checkErrors,
                        onCheckedChange = { v -> onSettingChanged { copy(checkErrors = v) } },
                    )

                    SettingsDivider()

                    SettingsToggleItem(
                        icon = Icons.Filled.ContentCopy,
                        title = stringResource(R.string.highlight_duplicates),
                        checked = settings.highlightDuplicates,
                        onCheckedChange = { v -> onSettingChanged { copy(highlightDuplicates = v) } },
                    )

                    SettingsDivider()

                    SettingsToggleItem(
                        icon = Icons.Filled.Save,
                        title = stringResource(R.string.auto_save),
                        checked = settings.autoSave,
                        onCheckedChange = { v -> onSettingChanged { copy(autoSave = v) } },
                    )

                    SettingsDivider()

                    SettingsToggleItem(
                        icon = Icons.Filled.Schedule,
                        title = stringResource(R.string.show_timer),
                        checked = settings.showTimer,
                        onCheckedChange = { v -> onSettingChanged { copy(showTimer = v) } },
                    )

                    SettingsDivider()

                    SettingsToggleItem(
                        icon = Icons.Filled.Favorite,
                        iconTint = AppTheme.colors.error,
                        title = stringResource(R.string.show_errors),
                        checked = settings.showErrors,
                        onCheckedChange = { v -> onSettingChanged { copy(showErrors = v) } },
                    )

                    SettingsDivider()

                    SettingsToggleItem(
                        icon = Icons.Filled.Favorite,
                        iconTint = AppTheme.colors.warning,
                        title = stringResource(R.string.unlimited_errors),
                        checked = settings.unlimitedErrors,
                        onCheckedChange = { v -> onSettingChanged { copy(unlimitedErrors = v) } },
                    )

                    SettingsDivider()

                    SettingsToggleItem(
                        icon = Icons.Filled.Lightbulb,
                        iconTint = AppTheme.colors.warning,
                        title = stringResource(R.string.unlimited_hints),
                        checked = settings.unlimitedHints,
                        onCheckedChange = { v -> onSettingChanged { copy(unlimitedHints = v) } },
                    )

                    SettingsDivider()

                    SettingsToggleItem(
                        icon = Icons.Filled.BarChart,
                        title = stringResource(R.string.track_statistics),
                        checked = settings.effectiveTrackStatistics,
                        enabled = !settings.hasCheats,
                        onCheckedChange = { v -> onSettingChanged { copy(trackStatistics = v) } },
                    )
                }

                SettingsSectionHeader(title = stringResource(R.string.appearance))

                SettingsCard {
                    SettingsToggleItem(
                        icon = Icons.Filled.Palette,
                        title = stringResource(R.string.dark_theme_label),
                        checked = settings.isDarkTheme,
                        onCheckedChange = { v -> onSettingChanged { copy(isDarkTheme = v) } },
                    )
                }

                SettingsSectionHeader(title = stringResource(R.string.sound))

                SettingsCard {
                    SettingsToggleItem(
                        icon = Icons.Filled.VolumeUp,
                        title = stringResource(R.string.sounds),
                        checked = settings.soundsEnabled,
                        onCheckedChange = { v -> onSettingChanged { copy(soundsEnabled = v) } },
                    )
                }

                SettingsSectionHeader(title = stringResource(R.string.other))

                SettingsCard {
                    SettingsNavItem(
                        icon = Icons.Filled.Security,
                        title = stringResource(R.string.privacy_policy),
                        onClick = onNavigateToPrivacyPolicy,
                    )
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = AppTheme.paddings.xxl,
                            bottom = AppTheme.paddings.xxxl,
                        ),
                    onClick = onResetClick,
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
}
