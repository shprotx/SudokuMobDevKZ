package ru.shprot.sudokumobdevkz.feature.menu.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
fun DailyChallengeCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.backgroundCardAccent,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.paddings.large),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Filled.LocalFireDepartment,
                contentDescription = null,
                tint = Color(0xFFFF9500),
                modifier = Modifier.size(AppTheme.sizes.iconLarge),
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

            Text(
                text = stringResource(R.string.progress),
                style = AppTheme.typography.caption1,
                color = AppTheme.colors.textSecondary,
            )

            Text(
                modifier = Modifier.padding(start = AppTheme.paddings.small),
                text = "0/3",
                style = AppTheme.typography.body2,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.primary,
            )

            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = AppTheme.colors.textSecondary,
            )
        }
    }
}
