package ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.LeaderboardRow
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

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

        Box(
            modifier = Modifier
                .size(AppTheme.sizes.iconLarge)
                .clip(CircleShape)
                .background(AppTheme.colors.backgroundCardAccent),
            contentAlignment = Alignment.Center,
        ) {

            if (row.avatarUri != null) {
                AsyncImage(
                    modifier = Modifier
                        .size(AppTheme.sizes.iconLarge)
                        .clip(CircleShape),
                    model = row.avatarUri,
                    contentDescription = null,
                )
            } else {
                Icon(
                    modifier = Modifier.size(AppTheme.sizes.iconMedium),
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    tint = AppTheme.colors.textSecondary,
                )
            }
        }

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

        Text(
            text = row.displayScore,
            style = AppTheme.typography.body2,
            color = AppTheme.colors.text,
            fontWeight = FontWeight.Medium,
        )
    }
}
