package ru.shprot.sudokumobdevkz.feature.gameover.presentation.screen

import androidx.compose.runtime.Composable

@Composable
fun GameOverScreen(
    isWin: Boolean,
    time: String,
    errors: Int,
    difficulty: Int,
    onNavigateToMenu: () -> Unit,
    onNavigateToNewGame: (difficulty: Int) -> Unit,
) {
    GameOverScreenContent(
        isWin = isWin,
        time = time,
        errors = errors,
        difficulty = difficulty,
        onNavigateToMenu = onNavigateToMenu,
        onNavigateToNewGame = onNavigateToNewGame,
    )
}
