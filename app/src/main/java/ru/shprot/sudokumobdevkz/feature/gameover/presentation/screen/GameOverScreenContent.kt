package ru.shprot.sudokumobdevkz.feature.gameover.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.gameover.presentation.components.ResultHeader
import ru.shprot.sudokumobdevkz.feature.gameover.presentation.components.StatCard

private val difficultyLabels = listOf("Лёгкая", "Средняя", "Экспертная")

@Composable
fun GameOverScreenContent(
    modifier: Modifier = Modifier,
    isWin: Boolean,
    time: String,
    errors: Int,
    difficulty: Int,
    onNavigateToMenu: () -> Unit,
    onNavigateToNewGame: (difficulty: Int) -> Unit,
) {
    Scaffold(containerColor = AppTheme.colors.background) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = AppTheme.paddings.large),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {}

            Column {
                ResultHeader(
                    icon = if (isWin) Icons.Filled.EmojiEvents
                    else Icons.Filled.SentimentDissatisfied,
                    title = if (isWin) "Победа!" else "Игра окончена",
                    subtitle = if (isWin) "Отличная работа! Головоломка решена."
                    else "Не сдавайся — попробуй ещё раз!",
                    isWin = isWin,
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppTheme.paddings.xxxl),
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.default),
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.Timer,
                        iconTint = Color(0xFF039FE0),
                        label = "Время",
                        value = time,
                    )

                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.Favorite,
                        iconTint = AppTheme.colors.error,
                        label = "Ошибки",
                        value = "$errors",
                    )

                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.SignalCellularAlt,
                        iconTint = AppTheme.colors.primary,
                        label = "Сложность",
                        value = difficultyLabels.getOrElse(difficulty) { "?" },
                    )
                }
            }

            Column(modifier = Modifier.padding(bottom = AppTheme.paddings.xxxl)) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(AppTheme.sizes.buttonHeight),
                    onClick = { onNavigateToNewGame(difficulty) },
                    shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.primary),
                ) {
                    Text(
                        text = if (isWin) "Играть снова" else "Попробовать ещё",
                        style = AppTheme.typography.button,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.colors.textOnPrimary,
                    )
                }

                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppTheme.paddings.default)
                        .height(AppTheme.sizes.buttonHeight),
                    onClick = onNavigateToMenu,
                    shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
                ) {
                    Text(
                        text = "На главную",
                        style = AppTheme.typography.button,
                        color = AppTheme.colors.text,
                    )
                }
            }
        }
    }
}
