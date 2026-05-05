package ru.shprot.sudokumobdevkz.feature.menu.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun DailyChallengeCard(
    modifier: Modifier,
    streak: Int,
    isCompleted: Boolean,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.backgroundCardAccent,
        ),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.paddings.large),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(AppTheme.sizes.iconLarge),
                imageVector = Icons.Filled.LocalFireDepartment,
                contentDescription = null,
                tint = Color(0xFFFF9500),
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = AppTheme.paddings.default),
            ) {
                Text(
                    text = stringResource(R.string.daily_challenge),
                    style = AppTheme.typography.body2,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.colors.text,
                )

                Text(
                    text = stringResource(R.string.daily_challenge_desc),
                    style = AppTheme.typography.caption1,
                    color = AppTheme.colors.textSecondary,
                )
            }

            if (isCompleted) {
                Icon(
                    modifier = Modifier.size(AppTheme.sizes.iconMedium),
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = AppTheme.colors.primary,
                )
            } else if (streak > 0) {
                Text(
                    modifier = Modifier.padding(end = AppTheme.paddings.small),
                    text = streak.toString(),
                    style = AppTheme.typography.h4,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colors.primary,
                )
            }

            Icon(
                modifier = Modifier,
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = AppTheme.colors.textSecondary,
            )
        }
    }
}