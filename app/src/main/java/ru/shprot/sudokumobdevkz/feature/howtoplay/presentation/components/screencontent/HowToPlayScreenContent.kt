package ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.components.screencontent
import ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.components.*

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.contract.HowToPlayUIEvent
import ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.contract.HowToPlayUIState

@Composable
fun HowToPlayScreenContent(
    uiState: HowToPlayUIState,
    onEvent: (HowToPlayUIEvent) -> Unit,
) {
    when (LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_LANDSCAPE ->
            HowToPlayLandscapeContent(uiState = uiState, onEvent = onEvent)

        else ->
            HowToPlayPortraitContent(uiState = uiState, onEvent = onEvent)
    }
}
