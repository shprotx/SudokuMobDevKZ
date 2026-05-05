package ru.shprot.sudokumobdevkz.feature.menu.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import ru.shprot.sudokumobdevkz.core.base.domain.model.dotColor
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun DifficultySelector(
    modifier: Modifier,
    selectedDifficulty: Int,
    onDifficultySelected: (Int) -> Unit,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.select_difficulty),
            style = AppTheme.typography.h4,
            color = AppTheme.colors.text,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = AppTheme.paddings.large),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Difficulty.entries.forEachIndexed { index, diff ->
                DifficultyCard(
                    title = stringResource(diff.titleRes),
                    subtitle = stringResource(diff.subtitleRes),
                    icon = diff.emoji,
                    dotCount = diff.dotCount,
                    dotColor = diff.dotColor(),
                    isSelected = selectedDifficulty == index,
                    onClick = { onDifficultySelected(index) },
                )
            }
        }
    }
}
