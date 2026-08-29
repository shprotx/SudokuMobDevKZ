package ru.shprot.sudokumobdevkz.feature.gameover.presentation.components.screencontent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.button.ButtonDefault
import ru.shprot.sudokumobdevkz.core.uicommon.button.ButtonOutlined
import ru.shprot.sudokumobdevkz.core.uicommon.icon.AppIcons
import ru.shprot.sudokumobdevkz.feature.gameover.presentation.components.ResultHeader
import ru.shprot.sudokumobdevkz.feature.gameover.presentation.components.StatCard
import ru.shprot.sudokumobdevkz.feature.gameover.presentation.components.StreakBanner
import ru.shprot.sudokumobdevkz.feature.gameover.presentation.contract.GameOverUIEvent
import ru.shprot.sudokumobdevkz.feature.gameover.presentation.contract.GameOverUIState

@Composable
internal fun GameOverLandscapeContent(
    uiState: GameOverUIState,
    onEvent: (GameOverUIEvent) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(
                horizontal = AppTheme.paddings.xxl,
                vertical = AppTheme.paddings.large,
            ),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.xxl),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
        ) {
            ResultHeader(
                modifier = Modifier,
                icon = when (uiState.isWin) {
                    true -> AppIcons.Trophy
                    false -> AppIcons.Sad
                },
                title = when (uiState.isWin) {
                    true -> stringResource(R.string.you_won)
                    false -> stringResource(R.string.game_over)
                },
                subtitle = when (uiState.isWin) {
                    true -> stringResource(R.string.win_subtitle)
                    false -> stringResource(R.string.lose_subtitle)
                },
                isWin = uiState.isWin,
            )

            if (uiState.isDailyChallenge && uiState.isWin) {
                StreakBanner(
                    modifier = Modifier.padding(top = AppTheme.paddings.large),
                    title = stringResource(R.string.daily_streak_label),
                    subtitle = stringResource(R.string.streak_days, uiState.newStreak),
                )
            }

            if (uiState.isDailyChallenge && !uiState.isWin) {
                StreakBanner(
                    modifier = Modifier.padding(top = AppTheme.paddings.large),
                    title = stringResource(R.string.daily_try_again),
                    subtitle = stringResource(R.string.daily_try_tomorrow),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.paddings.large),
                horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.default),
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = AppIcons.Stopwatch,
                    iconTint = Color(0xFF039FE0),
                    label = stringResource(R.string.time_label),
                    value = uiState.time,
                )

                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = AppIcons.Heart,
                    iconTint = AppTheme.colors.error,
                    label = stringResource(R.string.errors_label),
                    value = "${uiState.errors}",
                )

                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = AppIcons.Signal,
                    iconTint = AppTheme.colors.primary,
                    label = stringResource(R.string.difficulty_label),
                    value = uiState.difficulty.emoji,
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
        ) {
            ButtonDefault(
                modifier = Modifier,
                text = when {
                    uiState.isDailyChallenge -> stringResource(R.string.go_to_main_page)
                    uiState.isWin -> stringResource(R.string.play_again)
                    else -> stringResource(R.string.try_again)
                },
                onClick = { onEvent(GameOverUIEvent.PlayAgainClicked) },
            )

            if (!uiState.isDailyChallenge) {
                ButtonOutlined(
                    modifier = Modifier
                        .padding(top = AppTheme.paddings.default),
                    text = stringResource(R.string.go_to_main_page),
                    onClick = { onEvent(GameOverUIEvent.BackToMenuClicked) },
                )
            }
        }
    }
}
