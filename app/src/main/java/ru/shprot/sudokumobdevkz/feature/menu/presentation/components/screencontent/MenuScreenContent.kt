package ru.shprot.sudokumobdevkz.feature.menu.presentation.components.screencontent

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import ru.shprot.sudokumobdevkz.feature.menu.presentation.contract.MenuUIEvent
import ru.shprot.sudokumobdevkz.feature.menu.presentation.contract.MenuUIState

@Composable
fun MenuScreenContent(
    uiState: MenuUIState,
    onEvent: (MenuUIEvent) -> Unit,
) {
    when (LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_LANDSCAPE ->
            MenuLandscapeContent(uiState = uiState, onEvent = onEvent)

        else ->
            MenuPortraitContent(uiState = uiState, onEvent = onEvent)
    }
}
