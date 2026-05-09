package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
internal fun NumberCell(
    modifier: Modifier,
    number: Int,
    isAvailable: Boolean,
    isNotesMode: Boolean,
    height: Dp,
    onClick: (Int) -> Unit,
) {
    val shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusMedium)
    val borderColor = if (isNotesMode) AppTheme.colors.divider.copy(alpha = 0.5f) else AppTheme.colors.divider
    val textColor = if (isNotesMode) AppTheme.colors.draftText else AppTheme.colors.text
    val fontWeight = if (isNotesMode) FontWeight.Light else FontWeight.Medium

    Box(
        modifier = modifier
            .height(height)
            .alpha(if (isAvailable) 1f else 0f)
            .clip(shape)
            .then(
                if (isAvailable) Modifier
                    .border(1.dp, borderColor, shape)
                    .clickable { onClick(number) }
                else Modifier,
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (isAvailable) {
            Text(
                text = number.toString(),
                style = AppTheme.typography.h3,
                fontWeight = fontWeight,
                color = textColor,
            )
        }
    }
}