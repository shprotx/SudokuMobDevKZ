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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.button.ButtonText
import ru.shprot.sudokumobdevkz.core.uicommon.toolbar.ToolbarDefault
import ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.components.LeaderboardRowItem
import ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.contract.LeaderboardsUIEvent
import ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.contract.LeaderboardsUIState

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

        PullToRefreshBox(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            isRefreshing = uiState.isRefreshing,
            onRefresh = { onEvent(LeaderboardsUIEvent.Refresh) },
        ) {

            when {
                !uiState.isSignedIn ->
                    SignInCtaContent(onSignInCta = { onEvent(LeaderboardsUIEvent.SignInCtaClicked) })

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
}

@Composable
private fun SignInCtaContent(onSignInCta: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(AppTheme.paddings.xxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        Text(
            text = stringResource(R.string.leaderboard_sign_in_cta),
            style = AppTheme.typography.body2,
            color = AppTheme.colors.textSecondary,
            textAlign = TextAlign.Center,
        )

        ButtonText(
            modifier = Modifier.padding(top = AppTheme.paddings.medium),
            text = stringResource(R.string.leaderboard_open_settings),
            textColor = AppTheme.colors.primary,
            onClick = onSignInCta,
        )
    }
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
private fun LeaderboardList(uiState: LeaderboardsUIState) {

    val data = uiState.data ?: return
    val playerScore = data.playerScore
    val playerInTop = data.topRows.any { it.isCurrentPlayer }

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

        if (playerScore != null && !playerInTop && playerScore.rank != null) {
            item(key = "footer-player") {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppTheme.paddings.large),
                    text = stringResource(
                        R.string.leaderboard_your_place,
                        playerScore.rank,
                        playerScore.displayScore,
                    ),
                    style = AppTheme.typography.body2,
                    color = AppTheme.colors.primary,
                    textAlign = TextAlign.Center,
                )
            }
        }

        if (uiState.debugAvatarLog.isNotEmpty()) {
            item(key = "debug-avatar-log") {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppTheme.paddings.xxl),
                    text = "DEBUG avatars:\n" + uiState.debugAvatarLog,
                    style = AppTheme.typography.body3,
                    color = AppTheme.colors.textSecondary,
                )
            }
        }
    }
}
