package ru.shprot.sudokumobdevkz.feature.gameover.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.icon.AppIcons

@Composable
fun StreakBanner(
    modifier: Modifier,
    title: String,
    subtitle: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = AppTheme.colors.backgroundCardAccent,
                shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
            )
            .padding(
                horizontal = AppTheme.paddings.large,
                vertical = AppTheme.paddings.default,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(AppTheme.sizes.iconLarge),
            imageVector = AppIcons.Fire,
            contentDescription = null,
            tint = AppTheme.colors.warning,
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = AppTheme.paddings.default),
        ) {
            Text(
                text = title,
                style = AppTheme.typography.body2,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.colors.text,
            )

            Text(
                text = subtitle,
                style = AppTheme.typography.caption1,
                color = AppTheme.colors.textSecondary,
            )
        }
    }
}