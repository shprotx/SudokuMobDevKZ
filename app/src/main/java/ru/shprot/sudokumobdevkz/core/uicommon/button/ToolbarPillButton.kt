package ru.shprot.sudokumobdevkz.core.uicommon.button

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
internal fun ToolbarPillButton(
    modifier: Modifier,
    icon: ImageVector,
    contentDescription: String,
    isHighlighted: Boolean = false,
    onClick: () -> Unit,
) {

    IconButton(
        modifier = modifier.size(AppTheme.sizes.toolbarButton),
        onClick = onClick,
    ) {

        Icon(
            modifier = Modifier.size(AppTheme.sizes.iconMedium),
            imageVector = icon,
            contentDescription = contentDescription,
            tint = when (isHighlighted) {
                true -> AppTheme.colors.primary
                false -> AppTheme.colors.iconTint
            },
        )
    }
}