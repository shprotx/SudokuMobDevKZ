package ru.shprot.sudokumobdevkz.feature.settings.presentation.components.screencontent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.button.ButtonDefault
import ru.shprot.sudokumobdevkz.core.uicommon.toolbar.ToolbarDefault
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.settings.SettingsSectionHeader
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.settings.SettingsVersionFooter
import ru.shprot.sudokumobdevkz.feature.settings.presentation.contract.SettingsUIEvent
import ru.shprot.sudokumobdevkz.feature.settings.presentation.contract.SettingsUIState

@Composable
internal fun SettingsLandscapeContent(
    uiState: SettingsUIState,
    onEvent: (SettingsUIEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .statusBarsPadding(),
    ) {
        ToolbarDefault(
            modifier = Modifier,
            title = stringResource(R.string.settings),
            onLeadIconClick = { onEvent(SettingsUIEvent.BackClicked) },
        )

        Row(
            modifier = Modifier
                .weight(1f)
                .navigationBarsPadding()
                .padding(horizontal = AppTheme.paddings.large),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.large),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
            ) {
                SettingsSectionHeader(modifier = Modifier, title = stringResource(R.string.game_label))

                GameSettingsCard(uiState = uiState, onEvent = onEvent)
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
            ) {
                SettingsSectionHeader(modifier = Modifier, title = stringResource(R.string.appearance))

                AppearanceSettingsCard(uiState = uiState, onEvent = onEvent)

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

                SettingsVersionFooter(modifier = Modifier)
            }
        }
    }
}
