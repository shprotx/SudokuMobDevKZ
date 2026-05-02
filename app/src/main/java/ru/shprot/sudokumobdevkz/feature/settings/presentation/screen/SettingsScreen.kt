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
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.SettingsCard
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.SettingsDivider
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.SettingsNavItem
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.SettingsSectionHeader
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.SettingsToggleItem
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.SettingsToolbar

@Composable
fun SettingsScreen(onNavigateBack: () -> Unit) {
    var checkErrors by rememberSaveable { mutableStateOf(true) }
    var highlightDuplicates by rememberSaveable { mutableStateOf(true) }
    var autoSave by rememberSaveable { mutableStateOf(true) }
    var showTimer by rememberSaveable { mutableStateOf(true) }
    var showErrors by rememberSaveable { mutableStateOf(true) }
    var unlimitedErrors by rememberSaveable { mutableStateOf(false) }
    var unlimitedHints by rememberSaveable { mutableStateOf(false) }
    var trackStatistics by rememberSaveable { mutableStateOf(true) }
    var sounds by rememberSaveable { mutableStateOf(true) }

    val hasCheats = unlimitedErrors || unlimitedHints
    val effectiveTrackStatistics = trackStatistics && !hasCheats

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
                        checked = checkErrors,
                        onCheckedChange = { checkErrors = it },
                    )

                    SettingsDivider()

                    SettingsToggleItem(
                        icon = Icons.Filled.ContentCopy,
                        title = "Подсветка дубликатов",
                        checked = highlightDuplicates,
                        onCheckedChange = { highlightDuplicates = it },
                    )

                    SettingsDivider()

                    SettingsToggleItem(
                        icon = Icons.Filled.Save,
                        title = "Автосохранение",
                        checked = autoSave,
                        onCheckedChange = { autoSave = it },
                    )

                    SettingsDivider()

                    SettingsToggleItem(
                        icon = Icons.Filled.Schedule,
                        title = "Показывать время",
                        checked = showTimer,
                        onCheckedChange = { showTimer = it },
                    )

                    SettingsDivider()

                    SettingsToggleItem(
                        icon = Icons.Filled.Favorite,
                        iconTint = AppTheme.colors.error,
                        title = "Показывать ошибки",
                        checked = showErrors,
                        onCheckedChange = { showErrors = it },
                    )

                    SettingsDivider()

                    SettingsToggleItem(
                        icon = Icons.Filled.Favorite,
                        iconTint = AppTheme.colors.warning,
                        title = "Бесконечные ошибки",
                        checked = unlimitedErrors,
                        onCheckedChange = { unlimitedErrors = it },
                    )

                    SettingsDivider()

                    SettingsToggleItem(
                        icon = Icons.Filled.Lightbulb,
                        iconTint = AppTheme.colors.warning,
                        title = "Безлимитные подсказки",
                        checked = unlimitedHints,
                        onCheckedChange = { unlimitedHints = it },
                    )

                    SettingsDivider()

                    SettingsToggleItem(
                        icon = Icons.Filled.BarChart,
                        title = "Учёт статистики",
                        checked = effectiveTrackStatistics,
                        enabled = !hasCheats,
                        onCheckedChange = { trackStatistics = it },
                    )
                }

                // --- Внешний вид ---
                SettingsSectionHeader(title = "Внешний вид")

                SettingsCard {
                    SettingsNavItem(
                        icon = Icons.Filled.Palette,
                        title = "Тема",
                        value = "Светлая",
                        onClick = { },
                    )

                    SettingsDivider()

                    SettingsNavItem(
                        icon = Icons.Filled.ColorLens,
                        iconTint = AppTheme.colors.primary,
                        title = "Цветовая схема",
                        value = "Зелёная",
                        onClick = { },
                    )
                }

                // --- Звук ---
                SettingsSectionHeader(title = "Звук")

                SettingsCard {
                    SettingsToggleItem(
                        icon = Icons.Filled.VolumeUp,
                        title = "Звуки",
                        checked = sounds,
                        onCheckedChange = { sounds = it },
                    )
                }

                // --- Другое ---
                SettingsSectionHeader(title = "Другое")

                SettingsCard {
                    SettingsNavItem(
                        icon = Icons.Filled.Language,
                        title = "Язык",
                        value = "Русский",
                        onClick = { },
                    )

                    SettingsDivider()

                    SettingsNavItem(
                        icon = Icons.Filled.Star,
                        title = "Оценить приложение",
                        onClick = { },
                    )

                    SettingsDivider()

                    SettingsNavItem(
                        icon = Icons.Filled.Share,
                        title = "Поделиться",
                        onClick = { },
                    )

                    SettingsDivider()

                    SettingsNavItem(
                        icon = Icons.Filled.Security,
                        title = "Политика конфиденциальности",
                        onClick = { },
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
                    onClick = { },
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
