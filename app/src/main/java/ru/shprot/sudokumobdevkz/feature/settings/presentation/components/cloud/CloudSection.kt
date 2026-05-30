package ru.shprot.sudokumobdevkz.feature.settings.presentation.components.cloud

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInState
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.settings.SettingsCard
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.settings.SettingsDivider
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.settings.SettingsSectionHeader
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.settings.SettingsToggleItem
import ru.shprot.sudokumobdevkz.feature.settings.presentation.contract.CloudImportState
import ru.shprot.sudokumobdevkz.feature.settings.presentation.contract.SettingsUIEvent
import ru.shprot.sudokumobdevkz.feature.settings.presentation.contract.SettingsUIState

@Composable
fun CloudSection(
    uiState: SettingsUIState,
    onEvent: (SettingsUIEvent) -> Unit,
) {
    if (!uiState.isCloudAvailable) return

    SettingsSectionHeader(
        modifier = Modifier,
        title = stringResource(R.string.cloud_section_title),
    )

    SettingsCard(modifier = Modifier) {
        when (val state = uiState.signInState) {
            SignInState.NotAvailable,
            SignInState.SignedOut ->
                SignInRow(
                    modifier = Modifier,
                    isLoading = uiState.isSigningIn,
                    onClick = { onEvent(SettingsUIEvent.SignInClicked) },
                )

            is SignInState.SignedIn -> {
                SignedInRow(
                    modifier = Modifier,
                    displayName = state.displayName,
                    avatarUrl = state.avatarUrl,
                    onSignOutClick = { onEvent(SettingsUIEvent.SignOutClicked) },
                )

                SettingsDivider(modifier = Modifier)

                ImportFromCloudRow(
                    modifier = Modifier,
                    isLoading = uiState.cloudImport == CloudImportState.Loading ||
                        uiState.cloudImport == CloudImportState.Applying,
                    onClick = { onEvent(SettingsUIEvent.ImportFromCloudClicked) },
                )

                SettingsDivider(modifier = Modifier)

                SettingsToggleItem(
                    modifier = Modifier,
                    icon = Icons.Filled.EmojiEvents,
                    title = stringResource(R.string.settings_show_name_on_leaderboard),
                    checked = uiState.settings.showNameOnLeaderboard,
                    onCheckedChange = { onEvent(SettingsUIEvent.ToggleShowNameOnLeaderboard) },
                )
            }
        }
    }
}
