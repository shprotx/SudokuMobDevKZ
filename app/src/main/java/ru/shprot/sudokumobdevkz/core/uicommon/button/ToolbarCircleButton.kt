package ru.shprot.sudokumobdevkz.core.uicommon.button

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
internal fun ToolbarCircleButton(
    modifier: Modifier,
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) {

    Box(
        modifier = modifier
            .size(36.dp)
            .border(1.dp, AppTheme.colors.divider, CircleShape),
        contentAlignment = Alignment.Center,
    ) {

        IconButton(
            onClick = onClick,
        ) {

            Icon(
                modifier = Modifier.size(18.dp),
                imageVector = icon,
                contentDescription = contentDescription,
                tint = AppTheme.colors.iconTint,
            )
        }
    }
}