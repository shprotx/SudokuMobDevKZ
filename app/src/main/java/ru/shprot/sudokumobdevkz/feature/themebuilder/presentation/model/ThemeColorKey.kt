package ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.model

import androidx.annotation.StringRes
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.domain.model.ThemeColors

enum class ThemeColorKey(
    @StringRes val labelRes: Int,
    val get: (ThemeColors) -> Long,
    val set: (ThemeColors, Long) -> ThemeColors,
) {
    BACKGROUND(R.string.theme_builder_color_background, { it.background }, { c, v -> c.copy(background = v) }),
    SURFACE(R.string.theme_builder_color_surface, { it.surface }, { c, v -> c.copy(surface = v) }),
    PRIMARY(R.string.theme_builder_color_primary, { it.primary }, { c, v -> c.copy(primary = v) }),
    TEXT(R.string.theme_builder_color_text, { it.text }, { c, v -> c.copy(text = v) }),
    TEXT_SECONDARY(R.string.theme_builder_color_text_secondary, { it.textSecondary }, { c, v -> c.copy(textSecondary = v) }),
    ACCENT(R.string.theme_builder_color_accent, { it.textAccent }, { c, v -> c.copy(textAccent = v) }),
    ERROR(R.string.theme_builder_color_error, { it.error }, { c, v -> c.copy(error = v) }),
    GRID_LINE(R.string.theme_builder_color_grid_line, { it.gridLine }, { c, v -> c.copy(gridLine = v) }),
    CELL_SELECTED(R.string.theme_builder_color_cell_selected, { it.cellSelected }, { c, v -> c.copy(cellSelected = v) }),
    CELL_HIGHLIGHT(R.string.theme_builder_color_cell_highlight, { it.cellHighlight }, { c, v -> c.copy(cellHighlight = v) }),
    CELL_FIXED(R.string.theme_builder_color_cell_fixed, { it.cellFixed }, { c, v -> c.copy(cellFixed = v) }),
    CELL_EDITABLE(R.string.theme_builder_color_cell_editable, { it.cellEditable }, { c, v -> c.copy(cellEditable = v) }),
    CELL_ERROR(R.string.theme_builder_color_cell_error, { it.cellError }, { c, v -> c.copy(cellError = v) }),
    DIVIDER(R.string.theme_builder_color_divider, { it.divider }, { c, v -> c.copy(divider = v) }),
}
