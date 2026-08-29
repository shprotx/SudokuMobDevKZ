package ru.shprot.sudokumobdevkz.core.uicommon.glass

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shape
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

private const val TINT_ALPHA = 0.72f
private const val BORDER_ALPHA = 0.5f

@Composable
internal fun Modifier.glassBar(shape: Shape): Modifier = this
    .shadow(
        elevation = AppTheme.sizes.elevationSmall,
        shape = shape,
    )
    .clip(shape)
    .background(AppTheme.colors.surface.copy(alpha = TINT_ALPHA))
    .border(
        width = AppTheme.sizes.dividerThickness,
        color = AppTheme.colors.divider.copy(alpha = BORDER_ALPHA),
        shape = shape,
    )