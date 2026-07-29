package ru.shprot.sudokumobdevkz.core.uicommon.badge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
internal fun SquareIconBadge(
    modifier: Modifier,
    icon: ImageVector,
    backgroundColor: Color,
    iconTint: Color,
    contentDescription: String?,
) {
    Box(
        modifier = modifier
            .size(AppTheme.sizes.iconMedium)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusSmall),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = Modifier.size(AppTheme.sizes.iconSmall),
            imageVector = icon,
            contentDescription = contentDescription,
            tint = iconTint,
        )
    }
}
