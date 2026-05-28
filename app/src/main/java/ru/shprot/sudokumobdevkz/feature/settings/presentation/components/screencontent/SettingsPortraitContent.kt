package ru.shprot.sudokumobdevkz.feature.settings.presentation.components.screencontent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kotlinx.collections.immutable.toImmutableList
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.domain.model.ThemeMode
import ru.shprot.sudokumobdevkz.core.base.presentation.util.deviceFitsTwoRowInPortrait
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.button.ButtonDefault
import ru.shprot.sudokumobdevkz.core.uicommon.dropdown.AppDropdown
import ru.shprot.sudokumobdevkz.core.uicommon.toolbar.ToolbarDefault
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.HintModeSection
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.cloud.CloudSection
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.settings.SettingsCard
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.settings.SettingsDivider
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.settings.SettingsNavItem
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.settings.SettingsSectionHeader
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.settings.SettingsToggleItem
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.settings.SettingsVersionFooter
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.theme.ThemeModeDropdownItem
import ru.shprot.sudokumobdevkz.feature.settings.presentation.contract.SettingsUIEvent
import ru.shprot.sudokumobdevkz.feature.settings.presentation.contract.SettingsUIState

@Composable
internal fun SettingsPortraitContent(
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
            SettingsSectionHeader(modifier = Modifier, title = stringResource(R.string.game_label))

            GameSettingsCard(uiState = uiState, onEvent = onEvent)

            SettingsSectionHeader(modifier = Modifier, title = stringResource(R.string.hint_mode_section_title))

            HintModeSection(
                modifier = Modifier,
                selectedMode = uiState.settings.hintMode,
                onModeSelected = { onEvent(SettingsUIEvent.SelectHintMode(it)) },
            )

            SettingsSectionHeader(modifier = Modifier, title = stringResource(R.string.appearance))

            AppearanceSettingsCard(uiState = uiState, onEvent = onEvent)

            CloudSection(uiState = uiState, onEvent = onEvent)

            SettingsSectionHeader(modifier = Modifier, title = stringResource(R.string.other))

            OtherSettingsCard(onEvent = onEvent)

            ButtonDefault(
                modifier = Modifier.padding(top = AppTheme.paddings.xxl),
                text = stringResource(R.string.reset_statistics),
                containerColor = AppTheme.colors.error,
                onClick = { onEvent(SettingsUIEvent.ShowResetDialog) },
            )

            ButtonDefault(
                modifier = Modifier.padding(top = AppTheme.paddings.medium),
                text = stringResource(R.string.go_back),
                onClick = { onEvent(SettingsUIEvent.BackClicked) },
            )

            SettingsVersionFooter(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Composable
internal fun GameSettingsCard(
    uiState: SettingsUIState,
    onEvent: (SettingsUIEvent) -> Unit,
) {
    SettingsCard(modifier = Modifier) {
        SettingsToggleItem(
            modifier = Modifier,
            icon = Icons.Filled.CheckCircle,
            title = stringResource(R.string.check_errors),
            checked = uiState.settings.checkErrors,
            onCheckedChange = { onEvent(SettingsUIEvent.ToggleCheckErrors) },
        )

        SettingsDivider(modifier = Modifier)

        SettingsToggleItem(
            modifier = Modifier,
            icon = Icons.Filled.ContentCopy,
            title = stringResource(R.string.highlight_duplicates),
            checked = uiState.settings.highlightDuplicates,
            onCheckedChange = { onEvent(SettingsUIEvent.ToggleHighlightDuplicates) },
        )

        SettingsDivider(modifier = Modifier)

        SettingsToggleItem(
            modifier = Modifier,
            icon = Icons.Filled.Save,
            title = stringResource(R.string.auto_save),
            checked = uiState.settings.autoSave,
            onCheckedChange = { onEvent(SettingsUIEvent.ToggleAutoSave) },
        )

        SettingsDivider(modifier = Modifier)

        SettingsToggleItem(
            modifier = Modifier,
            icon = Icons.Filled.Schedule,
            title = stringResource(R.string.show_timer),
            checked = uiState.settings.showTimer,
            onCheckedChange = { onEvent(SettingsUIEvent.ToggleShowTimer) },
        )

        SettingsDivider(modifier = Modifier)

        SettingsToggleItem(
            modifier = Modifier,
            icon = Icons.Filled.Favorite,
            iconTint = AppTheme.colors.error,
            title = stringResource(R.string.show_errors),
            checked = uiState.settings.showErrors,
            onCheckedChange = { onEvent(SettingsUIEvent.ToggleShowErrors) },
        )

        SettingsDivider(modifier = Modifier)

        SettingsToggleItem(
            modifier = Modifier,
            icon = Icons.Filled.Favorite,
            iconTint = AppTheme.colors.warning,
            title = stringResource(R.string.unlimited_errors),
            checked = uiState.settings.unlimitedErrors,
            onCheckedChange = { onEvent(SettingsUIEvent.ToggleUnlimitedErrors) },
        )

        SettingsDivider(modifier = Modifier)

        SettingsToggleItem(
            modifier = Modifier,
            icon = Icons.Filled.Lightbulb,
            iconTint = AppTheme.colors.warning,
            title = stringResource(R.string.unlimited_hints),
            checked = uiState.settings.unlimitedHints,
            onCheckedChange = { onEvent(SettingsUIEvent.ToggleUnlimitedHints) },
        )

        SettingsDivider(modifier = Modifier)

        SettingsToggleItem(
            modifier = Modifier,
            icon = Icons.Filled.BarChart,
            title = stringResource(R.string.track_statistics),
            checked = uiState.settings.trackStatistics && uiState.settings.isStandardMode,
            enabled = uiState.settings.isStandardMode,
            onCheckedChange = { onEvent(SettingsUIEvent.ToggleTrackStatistics) },
        )

        val compactPadAvailable = deviceFitsTwoRowInPortrait()

        SettingsDivider(modifier = Modifier)

        SettingsToggleItem(
            modifier = Modifier,
            icon = Icons.Filled.GridView,
            title = stringResource(R.string.compact_number_pad),
            checked = uiState.settings.compactNumberPad && compactPadAvailable,
            enabled = compactPadAvailable,
            onCheckedChange = { onEvent(SettingsUIEvent.ToggleCompactNumberPad) },
        )
    }
}

@Composable
internal fun AppearanceSettingsCard(
    uiState: SettingsUIState,
    onEvent: (SettingsUIEvent) -> Unit,
) {
    val themeItems = remember {
        ThemeMode.builtIn().map(::ThemeModeDropdownItem).toImmutableList()
    }
    val selectedItem = remember(uiState.settings.themeModeId) {
        ThemeModeDropdownItem(ThemeMode.fromId(uiState.settings.themeModeId))
    }

    SettingsCard(modifier = Modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = AppTheme.paddings.default),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(AppTheme.sizes.iconMedium),
                imageVector = Icons.Filled.Palette,
                contentDescription = null,
                tint = AppTheme.colors.primary,
            )

            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = AppTheme.paddings.default),
                text = stringResource(R.string.theme_label),
                style = AppTheme.typography.body1,
                color = AppTheme.colors.text,
            )
        }

        AppDropdown(
            modifier = Modifier.padding(bottom = AppTheme.paddings.medium),
            items = themeItems,
            selected = selectedItem,
            onSelect = { item -> onEvent(SettingsUIEvent.SelectThemeMode(item.mode)) },
        )
    }
}

@Composable
internal fun OtherSettingsCard(
    onEvent: (SettingsUIEvent) -> Unit,
) {
    SettingsCard(modifier = Modifier) {
        SettingsNavItem(
            modifier = Modifier,
            icon = Icons.Filled.StarRate,
            title = stringResource(R.string.settings_rate_app),
            onClick = { onEvent(SettingsUIEvent.RateAppClicked) },
        )

        SettingsDivider(modifier = Modifier)

        SettingsNavItem(
            modifier = Modifier,
            icon = Icons.Filled.Share,
            title = stringResource(R.string.share_app),
            onClick = { onEvent(SettingsUIEvent.ShareAppClicked) },
        )

        SettingsDivider(modifier = Modifier)

        SettingsNavItem(
            modifier = Modifier,
            icon = Icons.Filled.Security,
            title = stringResource(R.string.privacy_policy),
            onClick = { onEvent(SettingsUIEvent.NavigateToPrivacyPolicy) },
        )

        SettingsDivider(modifier = Modifier)

        SettingsNavItem(
            modifier = Modifier,
            icon = Icons.Filled.Feedback,
            title = stringResource(R.string.feedback),
            onClick = { onEvent(SettingsUIEvent.NavigateToFeedback) },
        )
    }
}