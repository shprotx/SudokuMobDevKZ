package ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
internal fun ColorRow(
    label: String,
    colorValue: Long,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = AppTheme.paddings.default),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = label,
            style = AppTheme.typography.body1,
            color = AppTheme.colors.text,
        )

        Box(
            modifier = Modifier
                .size(AppTheme.sizes.colorSwatch)
                .clip(RoundedCornerShape(AppTheme.sizes.cornerRadiusSmall))
                .background(Color(colorValue))
                .border(
                    width = AppTheme.sizes.dividerThickness,
                    color = AppTheme.colors.divider,
                    shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusSmall),
                ),
            content = {},
        )
    }
}