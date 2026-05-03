package ru.shprot.sudokumobdevkz.feature.settings.presentation.components.screencontent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.toolbar.ToolbarDefault
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.privacy.PrivacySection
import ru.shprot.sudokumobdevkz.feature.settings.presentation.contract.PrivacyPolicyUIEvent
import ru.shprot.sudokumobdevkz.feature.settings.presentation.contract.PrivacyPolicyUIState

@Composable
fun PrivacyPolicyScreenContent(
    uiState: PrivacyPolicyUIState,
    onEvent: (PrivacyPolicyUIEvent) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(AppTheme.paddings.xxl),
    ) {

        ToolbarDefault(
            modifier = Modifier,
            title = stringResource(R.string.privacy_policy),
            onLeadIconClick = { onEvent(PrivacyPolicyUIEvent.BackClicked) },
        )

        PrivacySection(
            modifier = Modifier.padding(horizontal = AppTheme.paddings.large),
            title = stringResource(R.string.data_collection),
            body = stringResource(R.string.privacy_data_collection),
        )

        PrivacySection(
            modifier = Modifier.padding(horizontal = AppTheme.paddings.large),
            title = stringResource(R.string.data_usage),
            body = stringResource(R.string.privacy_data_usage),
        )

        PrivacySection(
            modifier = Modifier.padding(horizontal = AppTheme.paddings.large),
            title = stringResource(R.string.data_storage),
            body = stringResource(R.string.privacy_data_storage),
        )

        PrivacySection(
            modifier = Modifier.padding(horizontal = AppTheme.paddings.large),
            title = stringResource(R.string.user_rights),
            body = stringResource(R.string.privacy_user_rights),
        )

        PrivacySection(
            modifier = Modifier.padding(horizontal = AppTheme.paddings.large),
            title = stringResource(R.string.policy_changes),
            body = stringResource(R.string.privacy_changes),
        )

        Text(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(
                    start = AppTheme.paddings.large,
                    bottom = AppTheme.paddings.xxxl,
                ),
            text = stringResource(R.string.privacy_last_updated),
            style = AppTheme.typography.caption1,
            color = AppTheme.colors.textSecondary,
        )
    }
}
