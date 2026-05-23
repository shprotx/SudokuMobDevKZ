package ru.shprot.sudokumobdevkz.feature.settings.presentation.components.cloud

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun ImportFromCloudRow(
    modifier: Modifier,
    isLoading: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = !isLoading, onClick = onClick)
            .padding(vertical = AppTheme.paddings.default),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Filled.CloudDownload,
            contentDescription = null,
            tint = AppTheme.colors.iconTint,
            modifier = Modifier.size(AppTheme.sizes.iconMedium),
        )

        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = AppTheme.paddings.default),
            text = stringResource(R.string.cloud_import_from_other_device),
            style = AppTheme.typography.body1,
            color = AppTheme.colors.text,
        )

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(AppTheme.sizes.iconSmall),
                strokeWidth = AppTheme.sizes.dividerThickness,
                color = AppTheme.colors.iconTint,
            )
        }
    }
}
