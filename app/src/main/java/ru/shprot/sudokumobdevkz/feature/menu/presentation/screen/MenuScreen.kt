package ru.shprot.sudokumobdevkz.feature.menu.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

    MenuScreenContent(
        hasSavedGame = hasSavedGame,
        selectedDifficulty = selectedDifficulty,
        onDifficultySelected = { selectedDifficulty = it },
        onNavigateToGame = onNavigateToGame,
        onContinueGame = onContinueGame,
        onNavigateToStatistic = onNavigateToStatistic,
        onNavigateToSettings = onNavigateToSettings,
        onNavigateToHowToPlay = onNavigateToHowToPlay,
    )
}
