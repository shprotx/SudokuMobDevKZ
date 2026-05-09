package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun NumberPadTwoRow(
    modifier: Modifier,
    availableNumbers: Set<Int>,
    isNotesMode: Boolean,
    onNumberClick: (Int) -> Unit,
) {
    val cellHeight = 64.dp
    val spacing = AppTheme.paddings.small
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val sidePadding = (maxWidth + spacing) / 10
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(spacing),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing),
            ) {
                for (num in 1..5) {
                    NumberCell(
                        modifier = Modifier.weight(1f),
                        number = num,
                        isAvailable = num in availableNumbers,
                        isNotesMode = isNotesMode,
                        height = cellHeight,
                        onClick = onNumberClick,
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = sidePadding),
                horizontalArrangement = Arrangement.spacedBy(spacing),
            ) {
                for (num in 6..9) {
                    NumberCell(
                        modifier = Modifier.weight(1f),
                        number = num,
                        isAvailable = num in availableNumbers,
                        isNotesMode = isNotesMode,
                        height = cellHeight,
                        onClick = onNumberClick,
                    )
                }
            }
        }
    }
}
