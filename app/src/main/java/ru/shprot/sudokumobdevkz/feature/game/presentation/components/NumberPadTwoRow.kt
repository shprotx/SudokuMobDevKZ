package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun NumberPadTwoRow(
    modifier: Modifier,
    availableNumbers: Set<Int>,
    isNotesMode: Boolean,
    onNumberClick: (Int) -> Unit,
    onUndoClick: () -> Unit,
) {
    val cellHeight = 64.dp
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppTheme.paddings.small),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.small),
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
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.small),
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
            UndoCell(
                modifier = Modifier.weight(1f),
                height = cellHeight,
                onClick = onUndoClick,
            )
        }
    }
}

@Composable
internal fun UndoCell(
    modifier: Modifier,
    height: Dp,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusMedium)
    Box(
        modifier = modifier
            .height(height)
            .clip(shape)
            .border(1.dp, AppTheme.colors.divider, shape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Undo,
            contentDescription = null,
            tint = AppTheme.colors.iconTint,
            modifier = Modifier.size(AppTheme.sizes.iconMedium),
        )
    }
}