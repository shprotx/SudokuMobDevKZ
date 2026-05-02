package ru.shprot.sudokumobdevkz.feature.gameover.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun GameOverScreen(
    isWin: Boolean,
    time: String,
    errors: Int,
    difficulty: Int,
    onNavigateToMenu: () -> Unit,
    onNavigateToNewGame: (difficulty: Int) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = if (isWin) "You Win! ($time)" else "Game Over (errors=$errors)",
            style = AppTheme.typography.h2,
            color = AppTheme.colors.text,
        )
    }
}
