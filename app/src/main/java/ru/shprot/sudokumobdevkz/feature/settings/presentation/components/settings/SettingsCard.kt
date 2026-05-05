package ru.shprot.sudokumobdevkz.feature.settings.presentation.components.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun SettingsCard(
    modifier: Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.backgroundCard),
        elevation = CardDefaults.cardElevation(defaultElevation = AppTheme.sizes.elevationSmall),
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = AppTheme.paddings.large,
                vertical = AppTheme.paddings.medium,
            ),
            content = content,
        )
    }
}
