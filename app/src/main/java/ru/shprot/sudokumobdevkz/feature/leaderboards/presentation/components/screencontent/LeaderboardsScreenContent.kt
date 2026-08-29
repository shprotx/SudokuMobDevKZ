package ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.components.screencontent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.LeaderboardMappers.ownRowOutsideTop
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.icon.AppIcons
import ru.shprot.sudokumobdevkz.core.uicommon.toolbar.ToolbarDefault
import ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.components.LeaderboardRowItem
import ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.contract.LeaderboardsUIEvent
import ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.contract.LeaderboardsUIState
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.settings.SettingsToggleItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardsScreenContent(
    uiState: LeaderboardsUIState,
    onEvent: (LeaderboardsUIEvent) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background),
    ) {

        ToolbarDefault(
            modifier = Modifier,
            title = stringResource(R.string.leaderboards_title),
            onLeadIconClick = { onEvent(LeaderboardsUIEvent.BackClicked) },
        )

        SettingsToggleItem(
            modifier = Modifier.padding(horizontal = AppTheme.paddings.large),
            icon = AppIcons.Trophy,
            title = stringResource(R.string.settings_show_name_on_leaderboard),
            checked = uiState.showNameOnLeaderboard,
            onCheckedChange = { onEvent(LeaderboardsUIEvent.ToggleShowName) },
        )

        PullToRefreshBox(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            isRefreshing = uiState.isRefreshing,
            onRefresh = { onEvent(LeaderboardsUIEvent.Refresh) },
        ) {

            when {
                uiState.isLoading ->
                    LoadingContent()

                uiState.errorMessageRes != null ->
                    ErrorContent(messageRes = uiState.errorMessageRes)

                uiState.data == null || uiState.data.topRows.isEmpty() ->
                    EmptyContent()

                else ->
                    LeaderboardList(uiState = uiState)
            }
        }
    }

    if (uiState.showNameConsentPrompt) {
        NameConsentDialog(
            onAccept = { onEvent(LeaderboardsUIEvent.AcceptNameConsent) },
            onDismiss = { onEvent(LeaderboardsUIEvent.DismissNameConsentPrompt) },
        )
    }
}

@Composable
private fun NameConsentDialog(
    onAccept: () -> Unit,
    onDismiss: () -> Unit,
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.leaderboard_consent_title),
                style = AppTheme.typography.h3,
                color = AppTheme.colors.text,
            )
        },
        text = {
            Text(
                text = stringResource(R.string.leaderboard_consent_desc),
                style = AppTheme.typography.body3,
                color = AppTheme.colors.textSecondary,
            )
        },
        confirmButton = {
            TextButton(onClick = onAccept) {
                Text(
                    text = stringResource(R.string.leaderboard_consent_confirm),
                    color = AppTheme.colors.primary,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.leaderboard_consent_dismiss),
                    color = AppTheme.colors.textSecondary,
                )
            }
        },
        containerColor = AppTheme.colors.backgroundCard,
        titleContentColor = AppTheme.colors.text,
    )
}

@Composable
private fun LoadingContent() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {

        CircularProgressIndicator(
            modifier = Modifier.size(AppTheme.sizes.iconLarge),
            color = AppTheme.colors.primary,
        )
    }
}

@Composable
private fun ErrorContent(messageRes: Int) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(AppTheme.paddings.xxl),
        contentAlignment = Alignment.Center,
    ) {

        Text(
            text = stringResource(messageRes),
            style = AppTheme.typography.body2,
            color = AppTheme.colors.textSecondary,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun EmptyContent() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(AppTheme.paddings.xxl),
        contentAlignment = Alignment.Center,
    ) {

        Text(
            text = stringResource(R.string.leaderboard_empty),
            style = AppTheme.typography.body2,
            color = AppTheme.colors.textSecondary,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
internal fun LeaderboardList(uiState: LeaderboardsUIState) {

    val data = uiState.data ?: return
    val ownRow = data.ownRowOutsideTop()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        contentPadding = PaddingValues(
            horizontal = AppTheme.paddings.large,
            vertical = AppTheme.paddings.default,
        ),
        verticalArrangement = Arrangement.spacedBy(AppTheme.paddings.small),
    ) {

        items(
            items = data.topRows,
            key = { row -> "${row.rank}-${row.displayName}" },
        ) { row ->
            LeaderboardRowItem(
                modifier = Modifier,
                row = row,
            )
        }

        if (ownRow != null) {
            item(key = "own-row-divider") {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppTheme.paddings.small),
                    text = stringResource(R.string.leaderboard_rank_divider),
                    style = AppTheme.typography.body2,
                    color = AppTheme.colors.textSecondary,
                    textAlign = TextAlign.Center,
                )
            }

            item(key = "own-row-card") {
                LeaderboardRowItem(
                    modifier = Modifier,
                    row = ownRow,
                )
            }
        }
    }
}