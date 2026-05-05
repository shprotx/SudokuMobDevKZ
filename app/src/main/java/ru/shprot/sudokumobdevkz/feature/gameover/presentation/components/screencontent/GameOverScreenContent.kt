package ru.shprot.sudokumobdevkz.feature.gameover.presentation.components.screencontent
import ru.shprot.sudokumobdevkz.feature.gameover.presentation.components.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.button.ButtonDefault
import ru.shprot.sudokumobdevkz.core.uicommon.button.ButtonOutlined
import ru.shprot.sudokumobdevkz.feature.gameover.presentation.contract.GameOverUIEvent
import ru.shprot.sudokumobdevkz.feature.gameover.presentation.contract.GameOverUIState

@Composable
fun GameOverScreenContent(
    uiState: GameOverUIState,
    onEvent: (GameOverUIEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .statusBarsPadding()
            .padding(horizontal = AppTheme.paddings.large),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {}

        Column {
            ResultHeader(
                modifier = Modifier,
                icon = when (uiState.isWin) {
                    true -> Icons.Filled.EmojiEvents
                    false -> Icons.Filled.SentimentDissatisfied
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
                    .padding(top = AppTheme.paddings.xxxl),
                horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.default),
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.Timer,
                    iconTint = Color(0xFF039FE0),
                    label = stringResource(R.string.time_label),
                    value = uiState.time,
                )

                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.Favorite,
                    iconTint = AppTheme.colors.error,
                    label = stringResource(R.string.errors_label),
                    value = "${uiState.errors}",
                )

                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.SignalCellularAlt,
                    iconTint = AppTheme.colors.primary,
                    label = stringResource(R.string.difficulty_label),
                    value = uiState.difficulty.emoji,
                )
            }
        }

        Column(
            modifier = Modifier
            .navigationBarsPadding()
            .padding(bottom = AppTheme.paddings.xxxl)
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