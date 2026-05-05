package ru.shprot.sudokumobdevkz.feature.statistic.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
internal fun StatRow(
    label: String,
    value: String,
    valueColor: Color = AppTheme.colors.text,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = AppTheme.paddings.large,
                vertical = AppTheme.paddings.default,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = label,
            style = AppTheme.typography.body3,
            color = AppTheme.colors.text,
        )

        Text(
            text = value,
            style = AppTheme.typography.body2,
            fontWeight = FontWeight.SemiBold,
            color = valueColor,
        )
    }
}
