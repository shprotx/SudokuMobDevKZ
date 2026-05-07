package ru.shprot.sudokumobdevkz.feature.achievements.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

enum class AchievementBadgeVariant {
    UNLOCKED,
    SECRET,
}

@Composable
internal fun AchievementBadge(
    modifier: Modifier,
    variant: AchievementBadgeVariant,
) {
    val background = when (variant) {
        AchievementBadgeVariant.UNLOCKED -> AppTheme.colors.primaryLight
        AchievementBadgeVariant.SECRET -> AppTheme.colors.divider
    }
    val contentColor = when (variant) {
        AchievementBadgeVariant.UNLOCKED -> AppTheme.colors.primary
        AchievementBadgeVariant.SECRET -> AppTheme.colors.textSecondary
    }
    val icon = when (variant) {
        AchievementBadgeVariant.UNLOCKED -> Icons.Filled.CheckCircle
        AchievementBadgeVariant.SECRET -> Icons.Filled.Lock
    }
    val textRes = when (variant) {
        AchievementBadgeVariant.UNLOCKED -> R.string.achievements_section_unlocked
        AchievementBadgeVariant.SECRET -> R.string.achievement_secret_title
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(AppTheme.sizes.cornerRadiusFull))
            .background(background)
            .padding(
                horizontal = AppTheme.paddings.large,
                vertical = AppTheme.paddings.small,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.small),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(AppTheme.sizes.iconSmall),
        )

        Text(
            text = stringResource(textRes),
            style = AppTheme.typography.body3,
            color = contentColor,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
