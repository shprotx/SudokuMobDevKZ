package ru.shprot.sudokumobdevkz.feature.menu.presentation.components.screencontent
import ru.shprot.sudokumobdevkz.feature.menu.presentation.components.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.button.ButtonOutlined
import ru.shprot.sudokumobdevkz.feature.menu.presentation.contract.MenuUIEvent
import ru.shprot.sudokumobdevkz.feature.menu.presentation.contract.MenuUIState

@Composable
internal fun MenuPortraitContent(
    uiState: MenuUIState,
    onEvent: (MenuUIEvent) -> Unit,
) {
    Column(
        modifier = Modifier
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

        NewGameButton(
            modifier = Modifier.padding(top = AppTheme.paddings.large),
            onClick = { onEvent(MenuUIEvent.NewGameClicked(uiState.selectedDifficulty)) },
        )

        if (uiState.hasSavedGame) {
            ButtonOutlined(
                modifier = Modifier.padding(top = AppTheme.paddings.large),
                text = stringResource(R.string.continue_playing),
                borderColor = AppTheme.colors.primary,
                textColor = AppTheme.colors.primary,
                onClick = { onEvent(MenuUIEvent.ContinueGameClicked) },
            )
        }

        DailyChallengeCard(
            modifier = Modifier.padding(top = AppTheme.paddings.large),
            streak = uiState.dailyChallengeStreak,
            isCompleted = uiState.isDailyChallengeCompleted,
            onClick = { onEvent(MenuUIEvent.DailyChallengeClicked) },
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
            onAchievementsClick = { onEvent(MenuUIEvent.NavigateToAchievements) },
            onHowToPlayClick = { onEvent(MenuUIEvent.NavigateToHowToPlay) },
            onSettingsClick = { onEvent(MenuUIEvent.NavigateToSettings) },
        )
    }
}