package ru.shprot.sudokumobdevkz.feature.settings.presentation.components.privacy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
internal fun PrivacySection(
    modifier: Modifier,
    title: String,
    body: String
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(AppTheme.paddings.medium)
    ) {

        Text(
            text = title,
            style = AppTheme.typography.h4,
            color = AppTheme.colors.text,
        )

        Text(
            text = body,
            style = AppTheme.typography.body3,
            color = AppTheme.colors.textSecondary,
        )
    }
}