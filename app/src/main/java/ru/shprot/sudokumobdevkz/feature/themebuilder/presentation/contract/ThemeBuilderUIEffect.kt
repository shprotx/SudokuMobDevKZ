package ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.contract

import androidx.annotation.StringRes
import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEffect

sealed interface ThemeBuilderUIEffect : UIEffect {
    data object NavigateBack : ThemeBuilderUIEffect
    data class ShowMessage(@StringRes val messageRes: Int) : ThemeBuilderUIEffect
}