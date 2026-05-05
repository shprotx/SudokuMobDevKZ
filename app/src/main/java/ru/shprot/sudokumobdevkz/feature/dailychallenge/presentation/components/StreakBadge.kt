package ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun StreakBadge(
    modifier: Modifier,
    streak: Int,
    label: String,
) {
    Row(
        modifier = modifier
            .background(
                color = AppTheme.colors.backgroundCardAccent,
                shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusFull),
            )
            .padding(
                horizontal = AppTheme.paddings.large,
                vertical = AppTheme.paddings.medium,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(AppTheme.sizes.iconMedium),
            imageVector = Icons.Filled.LocalFireDepartment,
            contentDescription = null,
            tint = Color(0xFFFF9500),
        )

        Text(
            modifier = Modifier.padding(start = AppTheme.paddings.small),
            text = streak.toString(),
            style = AppTheme.typography.h3,
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.text,
        )

        Text(
            modifier = Modifier.padding(start = AppTheme.paddings.small),
            text = label,
            style = AppTheme.typography.body3,
            color = AppTheme.colors.textSecondary,
        )
    }
}