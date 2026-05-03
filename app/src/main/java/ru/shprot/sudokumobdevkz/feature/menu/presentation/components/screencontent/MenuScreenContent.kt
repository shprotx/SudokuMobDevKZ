package ru.shprot.sudokumobdevkz.feature.menu.presentation.components.screencontent
import ru.shprot.sudokumobdevkz.feature.menu.presentation.components.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.menu.presentation.contract.MenuUIEvent
import ru.shprot.sudokumobdevkz.feature.menu.presentation.contract.MenuUIState

@Composable
fun MenuScreenContent(
    uiState: MenuUIState,
    onEvent: (MenuUIEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = AppTheme.paddings.large),
    ) {
        MenuHeader(
            modifier = Modifier
                .statusBarsPadding()
                .padding(top = AppTheme.paddings.large),
            onSettingsClick = { onEvent(MenuUIEvent.NavigateToSettings) },
        )

        DailyChallengeCard(
            modifier = Modifier.padding(top = AppTheme.paddings.extraLarge),
        )

        if (uiState.hasSavedGame) {
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.paddings.large)
                    .height(AppTheme.sizes.buttonHeight),
                onClick = { onEvent(MenuUIEvent.ContinueGameClicked) },
                shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusXL),
            ) {
                Text(
                    text = stringResource(R.string.continue_playing),
                    style = AppTheme.typography.h3,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.colors.primary,
                )
            }
        }

        NewGameButton(
            modifier = Modifier.padding(top = AppTheme.paddings.large),
            onClick = { onEvent(MenuUIEvent.NewGameClicked(uiState.selectedDifficulty)) },
        )

        DifficultySelector(
            modifier = Modifier.padding(top = AppTheme.paddings.xxl),
            selectedDifficulty = uiState.selectedDifficulty,
            onDifficultySelected = { onEvent(MenuUIEvent.DifficultySelected(it)) },
        )

        MenuNavigationCards(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(
                    top = AppTheme.paddings.xxl,
                    bottom = AppTheme.paddings.xxxl,
                ),
            onStatisticClick = { onEvent(MenuUIEvent.NavigateToStatistic) },
            onHowToPlayClick = { onEvent(MenuUIEvent.NavigateToHowToPlay) },
            onSettingsClick = { onEvent(MenuUIEvent.NavigateToSettings) },
        )
    }
}
