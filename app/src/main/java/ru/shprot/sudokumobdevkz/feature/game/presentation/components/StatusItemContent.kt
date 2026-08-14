package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import ru.shprot.sudokumobdevkz.core.base.domain.model.StatusItemId
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
internal fun StatusItemContent(
    modifier: Modifier,
    itemId: StatusItemId,
    difficultyLabel: String,
    errors: Int,
    maxErrors: Int,
    lives: Int,
    timer: String,
) {
    when (itemId) {
        StatusItemId.DIFFICULTY -> Box(
            modifier = modifier
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

        StatusItemId.ERRORS -> Text(
            modifier = modifier,
            text = stringResource(R.string.errors_format, errors, maxErrors),
            style = AppTheme.typography.body5,
            color = AppTheme.colors.textSecondary,
        )

        StatusItemId.LIVES -> Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = AppTheme.colors.error,
                modifier = Modifier.size(AppTheme.sizes.iconSmall),
            )

            Text(
                modifier = Modifier.padding(start = AppTheme.paddings.small),
                text = lives.toString(),
                style = AppTheme.typography.body2,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.colors.text,
            )
        }

        StatusItemId.TIMER -> Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
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
