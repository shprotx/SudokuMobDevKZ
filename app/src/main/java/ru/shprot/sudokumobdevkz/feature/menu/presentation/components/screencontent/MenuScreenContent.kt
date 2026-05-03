package ru.shprot.sudokumobdevkz.feature.menu.presentation.components.screencontent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.menu.presentation.components.DailyChallengeCard
import ru.shprot.sudokumobdevkz.feature.menu.presentation.components.DifficultySelector
import ru.shprot.sudokumobdevkz.feature.menu.presentation.components.MenuHeader
import ru.shprot.sudokumobdevkz.feature.menu.presentation.components.MenuNavigationCards
import ru.shprot.sudokumobdevkz.feature.menu.presentation.components.NewGameButton

@Composable
fun MenuScreenContent(
    modifier: Modifier = Modifier,
    hasSavedGame: Boolean,
    selectedDifficulty: Int,
    onDifficultySelected: (Int) -> Unit,
    onNavigateToGame: (difficulty: Int) -> Unit,
    onContinueGame: () -> Unit,
    onNavigateToStatistic: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToHowToPlay: () -> Unit,
) {
    Scaffold(
        containerColor = AppTheme.colors.background,
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = AppTheme.paddings.large),
        ) {
            MenuHeader(
                modifier = Modifier.padding(top = AppTheme.paddings.large),
                onSettingsClick = onNavigateToSettings,
            )

            DailyChallengeCard(
                modifier = Modifier.padding(top = AppTheme.paddings.extraLarge),
            )

            if (hasSavedGame) {
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppTheme.paddings.large)
                        .height(AppTheme.sizes.buttonHeight),
                    onClick = onContinueGame,
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
                onClick = { onNavigateToGame(selectedDifficulty) },
            )

            DifficultySelector(
                modifier = Modifier.padding(top = AppTheme.paddings.xxl),
                selectedDifficulty = selectedDifficulty,
                onDifficultySelected = onDifficultySelected,
            )

            MenuNavigationCards(
                modifier = Modifier.padding(
                    top = AppTheme.paddings.xxl,
                    bottom = AppTheme.paddings.xxxl,
                ),
                onStatisticClick = onNavigateToStatistic,
                onHowToPlayClick = onNavigateToHowToPlay,
                onSettingsClick = onNavigateToSettings,
            )
        }
    }
}
