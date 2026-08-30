package ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import ru.shprot.sudokumobdevkz.core.uicommon.icon.AppIcons

@Composable
fun DailyHeroCard(
    modifier: Modifier,
    dateLabel: String,
    difficultyTitle: String,
    difficultyEmoji: String,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusXL),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.backgroundCardAccent,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = AppTheme.sizes.elevationSmall),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.paddings.large),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(AppTheme.sizes.iconXL)
                    .clip(CircleShape)
                    .background(AppTheme.colors.warning.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    modifier = Modifier.size(AppTheme.sizes.iconLarge),
                    imageVector = AppIcons.Fire,
                    contentDescription = null,
                    tint = AppTheme.colors.warning,
                )
            }

            Column(
                modifier = Modifier.padding(start = AppTheme.paddings.large),
            ) {
                Text(
                    text = dateLabel,
                    style = AppTheme.typography.h3,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colors.text,
                )

                Text(
                    modifier = Modifier.padding(top = AppTheme.paddings.extraSmall),
                    text = stringResource(R.string.daily_difficulty_format, difficultyEmoji, difficultyTitle),
                    style = AppTheme.typography.body1,
                    color = AppTheme.colors.textSecondary,
                )
            }
        }
    }
}