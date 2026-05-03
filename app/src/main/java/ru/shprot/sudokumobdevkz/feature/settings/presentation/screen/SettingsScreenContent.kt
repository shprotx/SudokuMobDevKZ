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
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.SettingsCard
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.SettingsDivider
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.SettingsNavItem
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.SettingsSectionHeader
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.SettingsToggleItem
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.SettingsToolbar
import ru.shprot.sudokumobdevkz.model.repository.AppSettings

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

                SettingsSectionHeader(title = "Игра")

                SettingsCard {
                    SettingsToggleItem(
                        icon = Icons.Filled.CheckCircle,
                        title = "Проверять ошибки",
                        checked = settings.checkErrors,
                        onCheckedChange = { v -> onSettingChanged { copy(checkErrors = v) } },
                    )

                    SettingsDivider()

                    SettingsToggleItem(
                        icon = Icons.Filled.ContentCopy,
                        title = "Подсветка дубликатов",
                        checked = settings.highlightDuplicates,
                        onCheckedChange = { v -> onSettingChanged { copy(highlightDuplicates = v) } },
                    )

                    SettingsDivider()

                    SettingsToggleItem(
                        icon = Icons.Filled.Save,
                        title = "Автосохранение",
                        checked = settings.autoSave,
                        onCheckedChange = { v -> onSettingChanged { copy(autoSave = v) } },
                    )

                    SettingsDivider()

                    SettingsToggleItem(
                        icon = Icons.Filled.Schedule,
                        title = "Показывать время",
                        checked = settings.showTimer,
                        onCheckedChange = { v -> onSettingChanged { copy(showTimer = v) } },
                    )

                    SettingsDivider()

                    SettingsToggleItem(
                        icon = Icons.Filled.Favorite,
                        iconTint = AppTheme.colors.error,
                        title = "Показывать ошибки",
                        checked = settings.showErrors,
                        onCheckedChange = { v -> onSettingChanged { copy(showErrors = v) } },
                    )

                    SettingsDivider()

                    SettingsToggleItem(
                        icon = Icons.Filled.Favorite,
                        iconTint = AppTheme.colors.warning,
                        title = "Бесконечные ошибки",
                        checked = settings.unlimitedErrors,
                        onCheckedChange = { v -> onSettingChanged { copy(unlimitedErrors = v) } },
                    )

                    SettingsDivider()

                    SettingsToggleItem(
                        icon = Icons.Filled.Lightbulb,
                        iconTint = AppTheme.colors.warning,
                        title = "Безлимитные подсказки",
                        checked = settings.unlimitedHints,
                        onCheckedChange = { v -> onSettingChanged { copy(unlimitedHints = v) } },
                    )

                    SettingsDivider()

                    SettingsToggleItem(
                        icon = Icons.Filled.BarChart,
                        title = "Учёт статистики",
                        checked = settings.effectiveTrackStatistics,
                        enabled = !settings.hasCheats,
                        onCheckedChange = { v -> onSettingChanged { copy(trackStatistics = v) } },
                    )
                }

                SettingsSectionHeader(title = "Внешний вид")

                SettingsCard {
                    SettingsToggleItem(
                        icon = Icons.Filled.Palette,
                        title = "Тёмная тема",
                        checked = settings.isDarkTheme,
                        onCheckedChange = { v -> onSettingChanged { copy(isDarkTheme = v) } },
                    )
                }

                SettingsSectionHeader(title = "Звук")

                SettingsCard {
                    SettingsToggleItem(
                        icon = Icons.Filled.VolumeUp,
                        title = "Звуки",
                        checked = settings.soundsEnabled,
                        onCheckedChange = { v -> onSettingChanged { copy(soundsEnabled = v) } },
                    )
                }

                SettingsSectionHeader(title = "Другое")

                SettingsCard {
                    SettingsNavItem(
                        icon = Icons.Filled.Security,
                        title = "Политика конфиденциальности",
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
                        text = "Сбросить статистику",
                        style = AppTheme.typography.button,
                        color = AppTheme.colors.textOnPrimary,
                    )
                }
            }
        }
    }
}
