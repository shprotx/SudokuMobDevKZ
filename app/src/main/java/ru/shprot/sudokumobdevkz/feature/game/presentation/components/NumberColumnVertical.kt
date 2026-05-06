package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun NumberColumnVertical(
    modifier: Modifier,
    numbers: List<Int>,
    availableNumbers: Set<Int>,
    isNotesMode: Boolean,
    onNumberClick: (Int) -> Unit,
) {
    val shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusMedium)
    val borderColor = when (isNotesMode) {
        true -> AppTheme.colors.divider.copy(alpha = 0.5f)
        false -> AppTheme.colors.divider
    }
    val textColor = when (isNotesMode) {
        true -> AppTheme.colors.draftText
        false -> AppTheme.colors.text
    }
    val fontWeight = when (isNotesMode) {
        true -> FontWeight.Light
        false -> FontWeight.Medium
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        numbers.forEach { num ->
            val isAvailable = num in availableNumbers

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .alpha(when (isAvailable) { true -> 1f; false -> 0f })
                    .clip(shape)
                    .then(
                        when (isAvailable) {
                            true -> Modifier
                                .border(1.dp, borderColor, shape)
                                .clickable { onNumberClick(num) }
                            false -> Modifier
                        }
                    ),
                contentAlignment = Alignment.Center,
            ) {
                if (isAvailable) {
                    Text(
                        text = num.toString(),
                        style = AppTheme.typography.h3,
                        fontWeight = fontWeight,
                        color = textColor,
                    )
                }
            }
        }
    }
}
