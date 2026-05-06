package ru.shprot.sudokumobdevkz.feature.achievements.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.util.AchievementIconMapper

@Composable
internal fun AchievementIcon(
    modifier: Modifier,
    iconKey: String,
) {
    Box(
        modifier = modifier
            .size(AppTheme.sizes.iconXL)
            .clip(CircleShape)
            .background(AppTheme.colors.primaryLight),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = AchievementIconMapper.resolve(iconKey),
            contentDescription = null,
            tint = AppTheme.colors.primary,
            modifier = Modifier
                .padding(AppTheme.paddings.small)
                .size(AppTheme.sizes.iconLarge),
        )
    }
}