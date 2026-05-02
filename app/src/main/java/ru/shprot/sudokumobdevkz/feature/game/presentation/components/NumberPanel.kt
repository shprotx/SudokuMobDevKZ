package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun NumberPanel(
    modifier: Modifier = Modifier,
    onNumberClick: (Int) -> Unit,
    onEraseClick: () -> Unit,
) {
    val buttonSize = AppTheme.sizes.numberPanelButton
    val shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusMedium)
    val borderColor = AppTheme.colors.divider

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            for (num in 1..5) {
                NumberButton(
                    number = num,
                    size = buttonSize,
                    shape = shape,
                    borderColor = borderColor,
                    onClick = { onNumberClick(num) },
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = AppTheme.paddings.default),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            for (num in 6..9) {
                NumberButton(
                    number = num,
                    size = buttonSize,
                    shape = shape,
                    borderColor = borderColor,
                    onClick = { onNumberClick(num) },
                )
            }

            Box(
                modifier = Modifier
                    .size(buttonSize)
                    .border(1.dp, borderColor, shape)
                    .clickable(onClick = onEraseClick),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                    contentDescription = "Стереть",
                    tint = AppTheme.colors.iconTint,
                    modifier = Modifier.size(AppTheme.sizes.iconMedium),
                )
            }
        }
    }
}

@Composable
private fun NumberButton(
    number: Int,
    size: Dp,
    shape: RoundedCornerShape,
    borderColor: Color,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(size)
            .border(1.dp, borderColor, shape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = number.toString(),
            style = AppTheme.typography.h2,
            fontWeight = FontWeight.Medium,
            color = AppTheme.colors.text,
        )
    }
}
