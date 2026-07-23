package ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEvent
import ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.model.ThemeColorKey

sealed interface ThemeBuilderUIEvent : UIEvent {
    data object BackClicked : ThemeBuilderUIEvent
    data object SaveClicked : ThemeBuilderUIEvent
    data object DismissSaveDialog : ThemeBuilderUIEvent
    data object ApplyColorPicker : ThemeBuilderUIEvent
    data object CancelColorPicker : ThemeBuilderUIEvent

    data class ConfirmSave(val name: String) : ThemeBuilderUIEvent
    data class OpenColorPicker(val colorKey: ThemeColorKey) : ThemeBuilderUIEvent
    data class ColorChanged(val colorKey: ThemeColorKey, val argb: Long) : ThemeBuilderUIEvent
}