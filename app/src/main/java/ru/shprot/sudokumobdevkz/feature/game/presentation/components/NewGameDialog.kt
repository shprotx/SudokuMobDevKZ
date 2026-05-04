package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun NewGameDialog(
    initialDifficulty: Int = 0,
    onStartGame: (difficulty: Int) -> Unit,
    onDismiss: () -> Unit,
) {
    var selectedDifficulty by rememberSaveable { mutableIntStateOf(initialDifficulty) }

    val dotColors = listOf(
        AppTheme.colors.primary,
        Color(0xFFFF9500),
        Color(0xFFFF3B30),
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusXL),
            colors = CardDefaults.cardColors(containerColor = AppTheme.colors.backgroundCard),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.paddings.xxl),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(AppTheme.colors.primaryLight),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = null,
                        tint = AppTheme.colors.primary,
                        modifier = Modifier.size(AppTheme.sizes.iconLarge),
                    )
                }

                Text(
                    modifier = Modifier.padding(top = AppTheme.paddings.large),
                    text = stringResource(R.string.new_game),
                    style = AppTheme.typography.h2,
                    color = AppTheme.colors.text,
                )

                Text(
                    modifier = Modifier.padding(top = AppTheme.paddings.small),
                    text = stringResource(R.string.select_difficulty),
                    style = AppTheme.typography.body3,
                    color = AppTheme.colors.textSecondary,
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppTheme.paddings.extraLarge),
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.default),
                ) {
                    val labels = listOf(
                        stringResource(R.string.difficulty_easy),
                        stringResource(R.string.difficulty_middle),
                        stringResource(R.string.difficulty_expert),
                    )

                    Difficulty.entries.forEachIndexed { index, diff ->
                        DifficultyOption(
                            modifier = Modifier.weight(1f),
                            label = labels[index],
                            emoji = diff.emoji,
                            dotCount = index + 1,
                            dotColor = dotColors[index],
                            isSelected = selectedDifficulty == index,
                            onClick = { selectedDifficulty = index },
                        )
                    }
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppTheme.paddings.xxl)
                        .height(AppTheme.sizes.buttonHeight),
                    onClick = { onStartGame(selectedDifficulty) },
                    shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.primary),
                ) {
                    Text(
                        text = stringResource(R.string.start),
                        style = AppTheme.typography.button,
                        color = AppTheme.colors.textOnPrimary,
                    )
                }

                TextButton(
                    modifier = Modifier.padding(top = AppTheme.paddings.medium),
                    onClick = onDismiss,
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        style = AppTheme.typography.body2,
                        color = AppTheme.colors.textSecondary,
                    )
                }
            }
        }
    }
}

@Composable
internal fun DifficultyOption(
    modifier: Modifier = Modifier,
    label: String,
    emoji: String,
    dotCount: Int,
    dotColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val bgColor = if (isSelected) AppTheme.colors.primaryLight else Color.Transparent
    val borderColor = if (isSelected) AppTheme.colors.primary else AppTheme.colors.divider

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(AppTheme.sizes.cornerRadiusMedium))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(AppTheme.sizes.cornerRadiusMedium))
            .clickable(onClick = onClick)
            .padding(vertical = AppTheme.paddings.default),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = emoji, style = AppTheme.typography.h3)

        Text(
            modifier = Modifier.padding(top = AppTheme.paddings.small),
            text = label,
            style = AppTheme.typography.caption1,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.colors.text,
            textAlign = TextAlign.Center,
        )

        Row(
            modifier = Modifier.padding(top = AppTheme.paddings.small),
            horizontalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .size(AppTheme.sizes.difficultyDot)
                        .clip(CircleShape)
                        .background(
                            if (index < dotCount) dotColor
                            else AppTheme.colors.divider
                        ),
                )
            }
        }
    }
}
