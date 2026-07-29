package ru.shprot.sudokumobdevkz.feature.settings.presentation.components.theme

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.badge.SquareIconBadge

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
            SquareIconBadge(
                modifier = Modifier,
                icon = Icons.Filled.Edit,
                backgroundColor = AppTheme.colors.warning,
                iconTint = AppTheme.colors.textOnPrimary,
                contentDescription = stringResource(R.string.theme_list_edit),
            )
        }

        IconButton(
            modifier = Modifier.size(AppTheme.sizes.toolbarButton),
            onClick = onDelete,
        ) {
            SquareIconBadge(
                modifier = Modifier,
                icon = Icons.Filled.Delete,
                backgroundColor = AppTheme.colors.error,
                iconTint = AppTheme.colors.textOnPrimary,
                contentDescription = stringResource(R.string.theme_list_delete),
            )
        }
    }
}
