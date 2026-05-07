package ru.shprot.sudokumobdevkz.feature.achievements.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
internal fun SecretAchievementIcon(
    modifier: Modifier,
    size: Dp = AppTheme.sizes.iconXL,
) {
    val fontSize = size.value.times(0.5f).sp
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(AppTheme.colors.divider),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "?",
            style = TextStyle(fontSize = fontSize, fontWeight = FontWeight.Bold),
            color = AppTheme.colors.textSecondary,
        )
    }
}
