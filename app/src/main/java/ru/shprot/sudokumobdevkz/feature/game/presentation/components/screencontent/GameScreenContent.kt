package ru.shprot.sudokumobdevkz.feature.game.presentation.components.screencontent
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.*

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameUIEvent
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameUIState

@Composable
fun GameScreenContent(
    uiState: GameUIState,
    onEvent: (GameUIEvent) -> Unit,
) {
    if (uiState.isGenerating) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.background),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = AppTheme.colors.primary)

                Text(
                    modifier = Modifier.padding(top = AppTheme.paddings.large),
                    text = stringResource(R.string.generating),
                    style = AppTheme.typography.body1,
                    color = AppTheme.colors.textSecondary,
                )
            }
        }
    } else {
        when (LocalConfiguration.current.orientation) {
            Configuration.ORIENTATION_LANDSCAPE ->
                GameLandscapeContent(uiState = uiState, onEvent = onEvent)

            else ->
                GamePortraitContent(uiState = uiState, onEvent = onEvent)
        }
    }

    if (uiState.showPauseDialog) {
        PauseDialog(
            timer = uiState.timer,
            errors = uiState.errors,
            maxErrors = uiState.maxErrors,
            onResume = {
                onEvent(GameUIEvent.DismissPauseDialog)
                onEvent(GameUIEvent.ResumeClicked)
            },
            onRestart = {
                onEvent(GameUIEvent.ShowNewGameDialog)
            },
            onExit = {
                onEvent(GameUIEvent.ExitGame)
            },
        )
    }

    if (uiState.showNewGameDialog) {
        NewGameDialog(
            initialDifficulty = uiState.difficulty.ordinal,
            onStartGame = { newDifficulty ->
                onEvent(GameUIEvent.StartNewGame(newDifficulty))
            },
            onDismiss = { onEvent(GameUIEvent.DismissNewGameDialog) },
        )
    }
}
