package ru.shprot.sudokumobdevkz.feature.menu.presentation.screen

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.menu.presentation.components.DailyChallengeCard
import ru.shprot.sudokumobdevkz.feature.menu.presentation.components.DifficultySelector
import ru.shprot.sudokumobdevkz.feature.menu.presentation.components.MenuHeader
import ru.shprot.sudokumobdevkz.feature.menu.presentation.components.MenuNavigationCards
import ru.shprot.sudokumobdevkz.feature.menu.presentation.components.NewGameButton
import ru.shprot.sudokumobdevkz.feature.menu.presentation.viewmodel.MenuViewModel

@Composable
fun MenuScreen(
    onNavigateToGame: (difficulty: Int) -> Unit,
    onContinueGame: () -> Unit,
    onNavigateToStatistic: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToHowToPlay: () -> Unit,
    viewModel: MenuViewModel = hiltViewModel(),
) {
    var selectedDifficulty by rememberSaveable { mutableIntStateOf(0) }
    val hasSavedGame by viewModel.hasSavedGame.collectAsStateWithLifecycle()

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
                        text = "Продолжить игру",
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
