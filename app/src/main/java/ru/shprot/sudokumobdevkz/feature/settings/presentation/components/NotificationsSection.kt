package ru.shprot.sudokumobdevkz.feature.settings.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.uicommon.icon.AppIcons
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.settings.SettingsCard
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.settings.SettingsSectionHeader
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.settings.SettingsToggleItem
import ru.shprot.sudokumobdevkz.feature.settings.presentation.contract.SettingsUIEvent
import ru.shprot.sudokumobdevkz.feature.settings.presentation.contract.SettingsUIState

@Composable
fun NotificationsSection(
    uiState: SettingsUIState,
    onEvent: (SettingsUIEvent) -> Unit,
) {
    SettingsSectionHeader(
        modifier = Modifier,
        title = stringResource(R.string.notifications_section_title),
    )

    SettingsCard(modifier = Modifier) {
        SettingsToggleItem(
            modifier = Modifier,
            icon = AppIcons.Bell,
            title = stringResource(R.string.push_notifications),
            checked = uiState.settings.notificationsEnabled,
            onCheckedChange = { onEvent(SettingsUIEvent.NotificationsToggleClicked) },
        )
    }
}
