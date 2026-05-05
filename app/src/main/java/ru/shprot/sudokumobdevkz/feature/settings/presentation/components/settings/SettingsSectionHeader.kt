package ru.shprot.sudokumobdevkz.feature.settings.presentation.components.settings

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun SettingsSectionHeader(
    modifier: Modifier,
    title: String,
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = AppTheme.paddings.xxl,
                bottom = AppTheme.paddings.default,
            ),
        text = title,
        style = AppTheme.typography.h4,
        color = AppTheme.colors.text,
    )
}
