package ru.shprot.sudokumobdevkz.feature.menu.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.menu.presentation.components.DailyChallengeCard
import ru.shprot.sudokumobdevkz.feature.menu.presentation.components.DifficultySelector
import ru.shprot.sudokumobdevkz.feature.menu.presentation.components.MenuHeader
import ru.shprot.sudokumobdevkz.feature.menu.presentation.components.MenuNavigationCards
import ru.shprot.sudokumobdevkz.feature.menu.presentation.components.NewGameButton

@Composable
fun MenuScreen(
    onNavigateToGame: (difficulty: Int) -> Unit,
    onNavigateToStatistic: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToHowToPlay: () -> Unit,
) {
    var selectedDifficulty by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        containerColor = AppTheme.colors.background,
    ) { paddingValues ->
        Column(
            modifier = Modifier
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

            NewGameButton(
                modifier = Modifier.padding(top = AppTheme.paddings.large),
                onClick = { onNavigateToGame(selectedDifficulty) },
            )

            DifficultySelector(
                modifier = Modifier.padding(top = AppTheme.paddings.xxl),
                selectedDifficulty = selectedDifficulty,
                onDifficultySelected = { selectedDifficulty = it },
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
