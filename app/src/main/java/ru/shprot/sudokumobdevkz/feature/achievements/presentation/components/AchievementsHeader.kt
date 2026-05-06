package ru.shprot.sudokumobdevkz.feature.achievements.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
internal fun AchievementsHeader(
    modifier: Modifier,
    unlocked: Int,
    total: Int,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface),
    ) {
        Row(
            modifier = Modifier.padding(AppTheme.paddings.large),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.large),
        ) {
            Icon(
                imageVector = Icons.Filled.EmojiEvents,
                contentDescription = null,
                tint = AppTheme.colors.primary,
                modifier = Modifier.size(AppTheme.sizes.iconLarge),
            )

            Column {
                Text(
                    text = stringResource(R.string.achievements),
                    style = AppTheme.typography.h2,
                    color = AppTheme.colors.text,
                )

                Text(
                    modifier = Modifier.padding(top = AppTheme.paddings.extraSmall),
                    text = "$unlocked / $total",
                    style = AppTheme.typography.body2,
                    color = AppTheme.colors.textSecondary,
                )
            }
        }
    }
}