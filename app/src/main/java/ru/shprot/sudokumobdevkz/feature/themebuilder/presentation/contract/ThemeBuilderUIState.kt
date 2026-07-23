package ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.domain.model.ThemeColors
import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIState
import ru.shprot.sudokumobdevkz.core.base.util.empty
import ru.shprot.sudokumobdevkz.core.theme.BuiltInTheme
import ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.model.ThemeColorKey

data class ThemeBuilderUIState(
    val colors: ThemeColors = BuiltInTheme.LIGHT.colors,
    val themeName: String = String.empty,
    val selectedColorKey: ThemeColorKey? = null,
    val pickerOriginalColor: Long? = null,
    val showColorPicker: Boolean = false,
    val showSaveDialog: Boolean = false,
    val showNameError: Boolean = false,
    val isEditMode: Boolean = false,
    val editThemeId: String? = null,
) : UIState