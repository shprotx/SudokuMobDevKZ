package ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.domain.model.ThemeColors
import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIState
import ru.shprot.sudokumobdevkz.core.theme.BuiltInThemes

data class ThemeBuilderUIState(
    val colors: ThemeColors = BuiltInThemes.Light.colors,
    val themeName: String = "",
    val selectedColorKey: String? = null,
    val showColorPicker: Boolean = false,
    val showSaveDialog: Boolean = false,
    val showNameError: Boolean = false,
    val isEditMode: Boolean = false,
    val editThemeId: String? = null,
) : UIState