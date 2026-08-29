package ru.shprot.sudokumobdevkz.core.uicommon.icon

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import ru.shprot.sudokumobdevkz.R

object AppIcons {

    val Back: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_back)

    val Restart: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_restart)

    val Pause: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_pause)

    val Check: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_check)

    val Layout: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_layout)

    val Palette: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_palette)

    val Settings: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_settings)

    val Undo: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_undo)

    val Erase: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_erase)

    val Note: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_note)

    val Hint: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_hint)

    val Heart: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_heart)

    val Clock: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_clock)

    val Close: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_close)

    val Play: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_play)
}