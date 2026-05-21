package ru.shprot.sudokumobdevkz.feature.settings.presentation.components.settings

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import ru.shprot.sudokumobdevkz.BuildConfig
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun SettingsVersionFooter(
    modifier: Modifier,
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = AppTheme.paddings.medium,
                bottom = AppTheme.paddings.large,
            ),
        text = stringResource(R.string.version_format, BuildConfig.VERSION_NAME),
        style = AppTheme.typography.caption1,
        color = AppTheme.colors.textSecondary,
        textAlign = TextAlign.Center,
    )
}