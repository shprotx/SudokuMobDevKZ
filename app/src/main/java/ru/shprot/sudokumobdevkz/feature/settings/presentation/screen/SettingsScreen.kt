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
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.SettingsCard
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.SettingsDivider
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.SettingsNavItem
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.SettingsSectionHeader
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.SettingsToggleItem
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.SettingsToolbar
import ru.shprot.sudokumobdevkz.feature.settings.presentation.viewmodel.SettingsViewModel

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

    Scaffold(containerColor = AppTheme.colors.background) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        ) {
            SettingsToolbar(onBackClick = onNavigateBack)

            Column(modifier = Modifier.padding(horizontal = AppTheme.paddings.large)) {

                // --- Игра ---
                SettingsSectionHeader(title = "Игра")

                SettingsCard {
                    SettingsToggleItem(
                        icon = Icons.Filled.CheckCircle,
                        title = "Проверять ошибки",
                        checked = settings.checkErrors,
                        onCheckedChange = { v -> viewModel.updateSetting { copy(checkErrors = v) } },
                    )

                    SettingsDivider()

                    SettingsToggleItem(
                        icon = Icons.Filled.ContentCopy,
                        title = "Подсветка дубликатов",
                        checked = settings.highlightDuplicates,
                        onCheckedChange = { v -> viewModel.updateSetting { copy(highlightDuplicates = v) } },
                    )

                    SettingsDivider()

                    SettingsToggleItem(
                        icon = Icons.Filled.Save,
                        title = "Автосохранение",
                        checked = settings.autoSave,
                        onCheckedChange = { v -> viewModel.updateSetting { copy(autoSave = v) } },
                    )

                    SettingsDivider()

                    SettingsToggleItem(
                        icon = Icons.Filled.Schedule,
                        title = "Показывать время",
                        checked = settings.showTimer,
                        onCheckedChange = { v -> viewModel.updateSetting { copy(showTimer = v) } },
                    )

                    SettingsDivider()

                    SettingsToggleItem(
                        icon = Icons.Filled.Favorite,
                        iconTint = AppTheme.colors.error,
                        title = "Показывать ошибки",
                        checked = settings.showErrors,
                        onCheckedChange = { v -> viewModel.updateSetting { copy(showErrors = v) } },
                    )

                    SettingsDivider()

                    SettingsToggleItem(
                        icon = Icons.Filled.Favorite,
                        iconTint = AppTheme.colors.warning,
                        title = "Бесконечные ошибки",
                        checked = settings.unlimitedErrors,
                        onCheckedChange = { v -> viewModel.updateSetting { copy(unlimitedErrors = v) } },
                    )

                    SettingsDivider()

                    SettingsToggleItem(
                        icon = Icons.Filled.Lightbulb,
                        iconTint = AppTheme.colors.warning,
                        title = "Безлимитные подсказки",
                        checked = settings.unlimitedHints,
                        onCheckedChange = { v -> viewModel.updateSetting { copy(unlimitedHints = v) } },
                    )

                    SettingsDivider()

                    SettingsToggleItem(
                        icon = Icons.Filled.BarChart,
                        title = "Учёт статистики",
                        checked = settings.effectiveTrackStatistics,
                        enabled = !settings.hasCheats,
                        onCheckedChange = { v -> viewModel.updateSetting { copy(trackStatistics = v) } },
                    )
                }

                // --- Внешний вид ---
                SettingsSectionHeader(title = "Внешний вид")

                SettingsCard {
                    SettingsToggleItem(
                        icon = Icons.Filled.Palette,
                        title = "Тёмная тема",
                        checked = settings.isDarkTheme,
                        onCheckedChange = { v -> viewModel.updateSetting { copy(isDarkTheme = v) } },
                    )
                }

                // --- Звук ---
                SettingsSectionHeader(title = "Звук")

                SettingsCard {
                    SettingsToggleItem(
                        icon = Icons.Filled.VolumeUp,
                        title = "Звуки",
                        checked = settings.soundsEnabled,
                        onCheckedChange = { v -> viewModel.updateSetting { copy(soundsEnabled = v) } },
                    )
                }

                // --- Другое ---
                SettingsSectionHeader(title = "Другое")

                SettingsCard {
                    SettingsNavItem(
                        icon = Icons.Filled.Security,
                        title = "Политика конфиденциальности",
                        onClick = onNavigateToPrivacyPolicy,
                    )
                }

                // --- Сброс ---
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = AppTheme.paddings.xxl,
                            bottom = AppTheme.paddings.xxxl,
                        ),
                    onClick = { showResetDialog = true },
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
