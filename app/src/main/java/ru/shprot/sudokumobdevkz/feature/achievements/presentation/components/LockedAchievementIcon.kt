package ru.shprot.sudokumobdevkz.feature.achievements.presentation.components

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import ru.shprot.sudokumobdevkz.core.base.domain.achievement.AchievementIconKey
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.util.AchievementIconMapper

@Composable
internal fun LockedAchievementIcon(
    modifier: Modifier,
    iconKey: AchievementIconKey,
) {
    val blurSupported = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    Box(
        modifier = modifier
            .size(AppTheme.sizes.iconXL)
            .clip(CircleShape)
            .background(AppTheme.colors.divider),
        contentAlignment = Alignment.Center,
    ) {
        if (blurSupported) {
            Icon(
                imageVector = AchievementIconMapper.resolve(iconKey),
                contentDescription = null,
                tint = AppTheme.colors.textSecondary,
                modifier = Modifier
                    .padding(AppTheme.paddings.small)
                    .size(AppTheme.sizes.iconLarge)
                    .blur(8.dp)
                    .alpha(0.4f),
            )
        } else {
            Icon(
                imageVector = AchievementIconMapper.resolve(iconKey),
                contentDescription = null,
                tint = AppTheme.colors.textSecondary,
                modifier = Modifier
                    .padding(AppTheme.paddings.small)
                    .size(AppTheme.sizes.iconLarge)
                    .alpha(0.35f),
            )
        }

        Icon(
            imageVector = Icons.Filled.Lock,
            contentDescription = null,
            tint = AppTheme.colors.textSecondary,
            modifier = Modifier.size(AppTheme.sizes.iconSmall),
        )
    }
}