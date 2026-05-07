package ru.shprot.sudokumobdevkz.feature.gameover.presentation.components.screencontent

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import ru.shprot.sudokumobdevkz.feature.gameover.presentation.contract.GameOverUIEvent
import ru.shprot.sudokumobdevkz.feature.gameover.presentation.contract.GameOverUIState

@Composable
fun GameOverScreenContent(
    uiState: GameOverUIState,
    onEvent: (GameOverUIEvent) -> Unit,
) {
    when (LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_LANDSCAPE ->
            GameOverLandscapeContent(uiState = uiState, onEvent = onEvent)

        else ->
            GameOverPortraitContent(uiState = uiState, onEvent = onEvent)
    }
}
