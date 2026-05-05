package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun DifficultyOption(
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

        Text(
            text = emoji,
            style = AppTheme.typography.h3,
        )

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
