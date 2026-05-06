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
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
internal fun SecretAchievementIcon(modifier: Modifier) {
    Box(
        modifier = modifier
            .size(AppTheme.sizes.iconXL)
            .clip(CircleShape)
            .background(AppTheme.colors.divider),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "?",
            style = AppTheme.typography.h1,
            color = AppTheme.colors.textSecondary,
        )
    }
}