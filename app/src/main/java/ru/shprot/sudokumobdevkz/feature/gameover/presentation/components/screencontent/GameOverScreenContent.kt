package ru.shprot.sudokumobdevkz.feature.gameover.presentation.components.screencontent
import ru.shprot.sudokumobdevkz.feature.gameover.presentation.components.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.gameover.presentation.contract.GameOverUIEvent
import ru.shprot.sudokumobdevkz.feature.gameover.presentation.contract.GameOverUIState

@Composable
fun GameOverScreenContent(
    uiState: GameOverUIState,
    onEvent: (GameOverUIEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .padding(horizontal = AppTheme.paddings.large),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {}

        Column {
            ResultHeader(
                icon = if (uiState.isWin) Icons.Filled.EmojiEvents
                else Icons.Filled.SentimentDissatisfied,
                title = if (uiState.isWin) stringResource(R.string.you_won) else stringResource(R.string.game_over),
                subtitle = if (uiState.isWin) stringResource(R.string.win_subtitle)
                else stringResource(R.string.lose_subtitle),
                isWin = uiState.isWin,
            )

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
                    value = listOf(
                        stringResource(R.string.difficulty_easy),
                        stringResource(R.string.difficulty_middle),
                        stringResource(R.string.difficulty_expert),
                    ).getOrElse(uiState.difficulty) { "?" },
                )
            }
        }

        Column(modifier = Modifier.padding(bottom = AppTheme.paddings.xxxl)) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AppTheme.sizes.buttonHeight),
                onClick = { onEvent(GameOverUIEvent.PlayAgainClicked) },
                shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.primary),
            ) {
                Text(
                    text = if (uiState.isWin) stringResource(R.string.play_again) else stringResource(R.string.try_again),
                    style = AppTheme.typography.button,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.colors.textOnPrimary,
                )
            }

            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.paddings.default)
                    .height(AppTheme.sizes.buttonHeight),
                onClick = { onEvent(GameOverUIEvent.BackToMenuClicked) },
                shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
            ) {
                Text(
                    text = stringResource(R.string.go_to_main_page),
                    style = AppTheme.typography.button,
                    color = AppTheme.colors.text,
                )
            }
        }
    }
}
