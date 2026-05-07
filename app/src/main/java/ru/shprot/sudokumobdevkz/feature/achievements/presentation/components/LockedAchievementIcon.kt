package ru.shprot.sudokumobdevkz.feature.achievements.presentation.components

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.shprot.sudokumobdevkz.core.base.domain.achievement.AchievementIconKey
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
internal fun LockedAchievementIcon(
    modifier: Modifier,
    iconKey: AchievementIconKey,
    size: Dp = AppTheme.sizes.iconXL,
) {
    val blurSupported = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val lockSize = size.times(0.32f)
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(AppTheme.colors.divider),
        contentAlignment = Alignment.Center,
    ) {
        if (blurSupported) {
            AchievementIcon(
                modifier = Modifier
                    .blur(size.times(0.18f))
                    .alpha(0.55f),
                iconKey = iconKey,
                size = size,
            )
        } else {
            AchievementIcon(
                modifier = Modifier.alpha(0.4f),
                iconKey = iconKey,
                size = size,
            )
        }

        Box(
            modifier = Modifier
                .size(lockSize.times(1.6f))
                .clip(CircleShape)
                .background(AppTheme.colors.background.copy(alpha = 0.65f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = null,
                tint = AppTheme.colors.textSecondary,
                modifier = Modifier.size(lockSize),
            )
        }
    }
}
