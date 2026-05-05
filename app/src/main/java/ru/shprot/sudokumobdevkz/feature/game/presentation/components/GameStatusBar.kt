package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Schedule
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

@Composable
fun GameStatusBar(
    modifier: Modifier,
    difficultyLabel: String,
    errors: Int,
    maxErrors: Int,
    lives: Int,
    timer: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.paddings.large),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(AppTheme.sizes.cornerRadiusFull))
                .background(AppTheme.colors.primaryLight)
                .padding(
                    horizontal = AppTheme.paddings.default,
                    vertical = AppTheme.paddings.small,
                ),
        ) {
            Text(
                text = difficultyLabel,
                style = AppTheme.typography.caption1,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.colors.primary,
            )
        }

        Text(
            text = stringResource(R.string.errors_format, errors, maxErrors),
            style = AppTheme.typography.body5,
            color = AppTheme.colors.textSecondary,
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = AppTheme.colors.error,
                modifier = Modifier.size(AppTheme.sizes.iconSmall),
            )

            Text(
                modifier = Modifier.padding(start = AppTheme.paddings.small),
                text = "$lives",
                style = AppTheme.typography.body2,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.colors.text,
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Schedule,
                contentDescription = null,
                tint = AppTheme.colors.textSecondary,
                modifier = Modifier.size(AppTheme.sizes.iconSmall),
            )

            Text(
                modifier = Modifier.padding(start = AppTheme.paddings.small),
                text = timer,
                style = AppTheme.typography.body2,
                color = AppTheme.colors.text,
            )
        }
    }
}
