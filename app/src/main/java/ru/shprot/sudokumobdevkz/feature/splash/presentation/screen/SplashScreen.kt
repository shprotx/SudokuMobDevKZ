package ru.shprot.sudokumobdevkz.feature.splash.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun SplashScreen(
    onNavigateToMenu: () -> Unit,
) {
    LaunchedEffect(Unit) {
        delay(1500)
        onNavigateToMenu()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.primary),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Sudoku",
                style = AppTheme.typography.h1,
                color = AppTheme.colors.textOnPrimary,
            )
            Text(
                text = "v${ru.shprot.sudokumobdevkz.BuildConfig.VERSION_NAME}",
                style = AppTheme.typography.body3,
                color = AppTheme.colors.textOnPrimary.copy(alpha = 0.7f),
            )
        }
    }
}
