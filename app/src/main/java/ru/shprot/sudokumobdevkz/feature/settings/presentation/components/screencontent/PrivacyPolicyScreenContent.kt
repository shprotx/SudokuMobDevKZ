package ru.shprot.sudokumobdevkz.feature.settings.presentation.components.screencontent
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.settings.presentation.contract.PrivacyPolicyUIEvent
import ru.shprot.sudokumobdevkz.feature.settings.presentation.contract.PrivacyPolicyUIState

@Composable
fun PrivacyPolicyScreenContent(
    uiState: PrivacyPolicyUIState,
    onEvent: (PrivacyPolicyUIEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .verticalScroll(rememberScrollState()),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(
                    horizontal = AppTheme.paddings.small,
                    vertical = AppTheme.paddings.medium,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { onEvent(PrivacyPolicyUIEvent.BackClicked) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.go_back),
                    tint = AppTheme.colors.text,
                )
            }

            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.privacy_policy),
                style = AppTheme.typography.h4,
                color = AppTheme.colors.text,
                textAlign = TextAlign.Center,
            )

            IconButton(onClick = { }, enabled = false) {}
        }

        Column(modifier = Modifier.padding(horizontal = AppTheme.paddings.large)) {
            Section(stringResource(R.string.data_collection), stringResource(R.string.privacy_data_collection))

            Section(stringResource(R.string.data_usage), stringResource(R.string.privacy_data_usage))

            Section(stringResource(R.string.data_storage), stringResource(R.string.privacy_data_storage))

            Section(stringResource(R.string.user_rights), stringResource(R.string.privacy_user_rights))

            Section(stringResource(R.string.policy_changes), stringResource(R.string.privacy_changes))

            Text(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(
                        top = AppTheme.paddings.xxl,
                        bottom = AppTheme.paddings.xxxl,
                    ),
                text = stringResource(R.string.privacy_last_updated),
                style = AppTheme.typography.caption1,
                color = AppTheme.colors.textSecondary,
            )
        }
    }
}

@Composable
internal fun Section(title: String, body: String) {
    Text(
        modifier = Modifier.padding(top = AppTheme.paddings.xxl),
        text = title,
        style = AppTheme.typography.h4,
        color = AppTheme.colors.text,
    )

    Text(
        modifier = Modifier.padding(top = AppTheme.paddings.medium),
        text = body,
        style = AppTheme.typography.body3,
        color = AppTheme.colors.textSecondary,
    )
}
