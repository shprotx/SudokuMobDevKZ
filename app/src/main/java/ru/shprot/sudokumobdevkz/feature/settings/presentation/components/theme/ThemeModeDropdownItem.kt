package ru.shprot.sudokumobdevkz.feature.settings.presentation.components.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.domain.model.ThemeMode
import ru.shprot.sudokumobdevkz.core.uicommon.dropdown.DropdownItem

data class ThemeModeDropdownItem(
    val mode: ThemeMode,
) : DropdownItem {
    override val id: String = mode.id

    @Composable
    override fun title(): String = when (mode) {
        ThemeMode.System -> stringResource(R.string.theme_mode_system)
        ThemeMode.Light -> stringResource(R.string.theme_mode_light)
        ThemeMode.Dark -> stringResource(R.string.theme_mode_dark)
        is ThemeMode.Custom -> mode.title
    }
}