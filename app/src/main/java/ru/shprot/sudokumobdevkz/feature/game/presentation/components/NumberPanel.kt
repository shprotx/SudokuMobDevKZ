package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun NumberPanel(
    modifier: Modifier,
    availableNumbers: Set<Int>,
    isNotesMode: Boolean,
    onNumberClick: (Int) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.extraSmall),
    ) {
        for (num in 1..9) {
            NumberCell(
                modifier = Modifier.weight(1f),
                number = num,
                isAvailable = num in availableNumbers,
                isNotesMode = isNotesMode,
                height = 52.dp,
                onClick = onNumberClick,
            )
        }
    }
}