package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import ru.shprot.sudokumobdevkz.core.base.domain.model.dotColor
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.button.ButtonDefault
import ru.shprot.sudokumobdevkz.core.uicommon.button.ButtonText

@Composable
fun NewGameDialog(
    initialDifficulty: Int = 0,
    onStartGame: (difficulty: Int) -> Unit,
    onDismiss: () -> Unit,
) {
    var selectedDifficulty by rememberSaveable { mutableIntStateOf(initialDifficulty) }

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
                        modifier = Modifier.size(AppTheme.sizes.iconLarge),
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = null,
                        tint = AppTheme.colors.primary,
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
                    Difficulty.entries.forEachIndexed { index, diff ->
                        DifficultyOption(
                            modifier = Modifier.weight(1f),
                            label = stringResource(diff.titleRes),
                            emoji = diff.emoji,
                            dotCount = diff.dotCount,
                            dotColor = diff.dotColor(),
                            isSelected = selectedDifficulty == index,
                            onClick = { selectedDifficulty = index },
                        )
                    }
                }

                ButtonDefault(
                    modifier = Modifier.padding(top = AppTheme.paddings.xxl),
                    text = stringResource(R.string.start),
                    onClick = { onStartGame(selectedDifficulty) },
                )

                ButtonText(
                    modifier = Modifier.padding(top = AppTheme.paddings.medium),
                    text = stringResource(R.string.cancel),
                    onClick = onDismiss,
                )
            }
        }
    }
}
