package ru.shprot.sudokumobdevkz.core.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

object AppShapes {

    val cardShape: RoundedCornerShape
        @Composable
        @ReadOnlyComposable
        get() = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge)

    val cardShapeSmall: RoundedCornerShape
        @Composable
        @ReadOnlyComposable
        get() = RoundedCornerShape(AppTheme.sizes.cornerRadiusMedium)

    val buttonShape: RoundedCornerShape
        @Composable
        @ReadOnlyComposable
        get() = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge)

    val chipShape: RoundedCornerShape
        @Composable
        @ReadOnlyComposable
        get() = RoundedCornerShape(AppTheme.sizes.cornerRadiusFull)

    val bottomSheetShape: RoundedCornerShape
        @Composable
        @ReadOnlyComposable
        get() = RoundedCornerShape(
            topStart = AppTheme.sizes.cornerRadiusXL,
            topEnd = AppTheme.sizes.cornerRadiusXL,
        )
}
