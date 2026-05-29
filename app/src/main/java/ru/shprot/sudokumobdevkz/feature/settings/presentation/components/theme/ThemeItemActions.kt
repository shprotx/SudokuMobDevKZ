package ru.shprot.sudokumobdevkz.feature.settings.presentation.components.theme

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
internal fun ThemeItemActions(
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(
            modifier = Modifier.size(AppTheme.sizes.toolbarButton),
            onClick = onEdit,
        ) {
            Icon(
                modifier = Modifier.size(AppTheme.sizes.iconSmall),
                imageVector = Icons.Filled.Edit,
                contentDescription = stringResource(R.string.theme_list_edit),
                tint = AppTheme.colors.iconTint,
            )
        }

        IconButton(
            modifier = Modifier.size(AppTheme.sizes.toolbarButton),
            onClick = onDelete,
        ) {
            Icon(
                modifier = Modifier.size(AppTheme.sizes.iconSmall),
                imageVector = Icons.Outlined.Delete,
                contentDescription = stringResource(R.string.theme_list_delete),
                tint = AppTheme.colors.error,
            )
        }
    }
}
