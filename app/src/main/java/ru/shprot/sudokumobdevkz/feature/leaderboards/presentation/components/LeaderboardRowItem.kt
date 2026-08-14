package ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.LeaderboardRow
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.avatar.PgsAvatar

@Composable
fun LeaderboardRowItem(
    modifier: Modifier,
    row: LeaderboardRow,
) {

    val bgColor =
        if (row.isCurrentPlayer) AppTheme.colors.primaryLight
        else AppTheme.colors.backgroundCard

    val nameColor =
        if (row.isCurrentPlayer) AppTheme.colors.primary
        else AppTheme.colors.text

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppTheme.sizes.cornerRadiusMedium))
            .background(bgColor)
            .padding(
                horizontal = AppTheme.paddings.large,
                vertical = AppTheme.paddings.default,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Text(
            modifier = Modifier.padding(end = AppTheme.paddings.default),
            text = "#${row.rank}",
            style = AppTheme.typography.body2,
            color = AppTheme.colors.textSecondary,
            fontWeight = FontWeight.SemiBold,
        )

        PgsAvatar(
            modifier = Modifier,
            size = AppTheme.sizes.iconLarge,
            avatarUrl = row.avatarUrl,
        )

        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = AppTheme.paddings.default),
            text = row.displayName,
            style = AppTheme.typography.body2,
            color = nameColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = if (row.isCurrentPlayer) FontWeight.SemiBold else FontWeight.Normal,
        )

        if (row.achievementsCount != null) {
            Icon(
                modifier = Modifier
                    .padding(end = AppTheme.paddings.small)
                    .size(AppTheme.sizes.iconSmall),
                imageVector = Icons.Filled.EmojiEvents,
                contentDescription = null,
                tint = AppTheme.colors.textSecondary,
            )

            Text(
                modifier = Modifier.padding(end = AppTheme.paddings.default),
                text = row.achievementsCount.toString(),
                style = AppTheme.typography.body3,
                color = AppTheme.colors.textSecondary,
            )
        }

        Text(
            text = row.displayScore,
            style = AppTheme.typography.body2,
            color = AppTheme.colors.text,
            fontWeight = FontWeight.Medium,
        )
    }
}
