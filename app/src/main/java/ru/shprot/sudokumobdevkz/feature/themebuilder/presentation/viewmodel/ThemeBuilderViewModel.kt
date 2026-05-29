package ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.data.repository.ISettingsRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.IThemeRepository
import ru.shprot.sudokumobdevkz.core.base.domain.model.CustomTheme
import ru.shprot.sudokumobdevkz.core.base.domain.model.ThemeColors
import ru.shprot.sudokumobdevkz.core.base.presentation.viewmodel.BaseViewModel
import ru.shprot.sudokumobdevkz.core.theme.BuiltInThemes
import ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.contract.ThemeBuilderUIEffect
import ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.contract.ThemeBuilderUIEvent
import ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.contract.ThemeBuilderUIState
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ThemeBuilderViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val themeRepository: IThemeRepository,
    private val settingsRepository: ISettingsRepository,
) : BaseViewModel<ThemeBuilderUIEvent, ThemeBuilderUIState, ThemeBuilderUIEffect>(
    ThemeBuilderUIState()
) {
    init {
        val themeId = savedStateHandle.get<String?>("themeId")
        if (themeId != null) {
            viewModelScope.launch(exceptionHandler) {
                val theme = themeRepository.getById(themeId)
                if (theme != null) {
                    updateState {
                        copy(
                            colors = theme.colors,
                            themeName = theme.name,
                            isEditMode = true,
                            editThemeId = themeId,
                        )
                    }
                }
            }
        }
    }

    override fun handleUIEvent(event: ThemeBuilderUIEvent) =
        when (event) {
            ThemeBuilderUIEvent.BackClicked ->
                setEffect(ThemeBuilderUIEffect.NavigateBack)

            ThemeBuilderUIEvent.SaveClicked ->
                updateState { copy(showSaveDialog = true, showNameError = false) }

            ThemeBuilderUIEvent.DismissSaveDialog ->
                updateState { copy(showSaveDialog = false) }

            ThemeBuilderUIEvent.DismissColorPicker ->
                updateState { copy(showColorPicker = false, selectedColorKey = null) }

            is ThemeBuilderUIEvent.ConfirmSave ->
                handleConfirmSave(event.name)

            is ThemeBuilderUIEvent.SelectPreset ->
                updateState { copy(colors = event.preset) }

            is ThemeBuilderUIEvent.OpenColorPicker ->
                updateState { copy(selectedColorKey = event.colorKey, showColorPicker = true) }

            is ThemeBuilderUIEvent.ColorChanged ->
                handleColorChanged(event.colorKey, event.argb)
        }

    private fun handleConfirmSave(name: String) {
        if (name.isBlank()) {
            updateState { copy(showNameError = true) }
            return
        }
        updateState { copy(showSaveDialog = false) }
        viewModelScope.launch(exceptionHandler) {
            val id = currentState.editThemeId ?: UUID.randomUUID().toString()
            val theme = CustomTheme(
                id = id,
                name = name.trim(),
                isBuiltIn = false,
                colors = currentState.colors,
                createdAt = System.currentTimeMillis(),
            )
            themeRepository.save(theme)
            settingsRepository.update { copy(themeModeId = id) }
            setEffect(ThemeBuilderUIEffect.ShowMessage(R.string.theme_saved))
            setEffect(ThemeBuilderUIEffect.NavigateBack)
        }
    }

    private fun handleColorChanged(key: String, argb: Long) {
        val updated = applyColorChange(currentState.colors, key, argb)
        updateState { copy(colors = updated) }
    }

    private fun applyColorChange(colors: ThemeColors, key: String, argb: Long): ThemeColors =
        when (key) {
            "primary" -> colors.copy(primary = argb)
            "primaryDark" -> colors.copy(primaryDark = argb)
            "primaryLight" -> colors.copy(primaryLight = argb)
            "secondary" -> colors.copy(secondary = argb)
            "background" -> colors.copy(background = argb)
            "backgroundCard" -> colors.copy(backgroundCard = argb)
            "backgroundCardAccent" -> colors.copy(backgroundCardAccent = argb)
            "surface" -> colors.copy(surface = argb)
            "text" -> colors.copy(text = argb)
            "textSecondary" -> colors.copy(textSecondary = argb)
            "textOnPrimary" -> colors.copy(textOnPrimary = argb)
            "textAccent" -> colors.copy(textAccent = argb)
            "error" -> colors.copy(error = argb)
            "errorLight" -> colors.copy(errorLight = argb)
            "success" -> colors.copy(success = argb)
            "warning" -> colors.copy(warning = argb)
            "gridLine" -> colors.copy(gridLine = argb)
            "gridLineBold" -> colors.copy(gridLineBold = argb)
            "cellSelected" -> colors.copy(cellSelected = argb)
            "cellHighlight" -> colors.copy(cellHighlight = argb)
            "cellSameNumber" -> colors.copy(cellSameNumber = argb)
            "cellError" -> colors.copy(cellError = argb)
            "cellFixed" -> colors.copy(cellFixed = argb)
            "cellEditable" -> colors.copy(cellEditable = argb)
            "draftText" -> colors.copy(draftText = argb)
            "divider" -> colors.copy(divider = argb)
            "iconTint" -> colors.copy(iconTint = argb)
            "bottomNavSelected" -> colors.copy(bottomNavSelected = argb)
            "bottomNavUnselected" -> colors.copy(bottomNavUnselected = argb)
            "chipSelected" -> colors.copy(chipSelected = argb)
            "chipUnselected" -> colors.copy(chipUnselected = argb)
            "chipTextSelected" -> colors.copy(chipTextSelected = argb)
            "chipTextUnselected" -> colors.copy(chipTextUnselected = argb)
            "progressTrack" -> colors.copy(progressTrack = argb)
            "progressIndicator" -> colors.copy(progressIndicator = argb)
            "barChart" -> colors.copy(barChart = argb)
            "barChartLabel" -> colors.copy(barChartLabel = argb)
            else -> colors
        }

    companion object {
        val colorKeys: List<Pair<String, Int>> = listOf(
            "background" to R.string.theme_builder_color_background,
            "surface" to R.string.theme_builder_color_surface,
            "primary" to R.string.theme_builder_color_primary,
            "text" to R.string.theme_builder_color_text,
            "textSecondary" to R.string.theme_builder_color_text_secondary,
            "textAccent" to R.string.theme_builder_color_accent,
            "error" to R.string.theme_builder_color_error,
            "gridLine" to R.string.theme_builder_color_grid_line,
            "cellSelected" to R.string.theme_builder_color_cell_selected,
            "cellHighlight" to R.string.theme_builder_color_cell_highlight,
            "cellFixed" to R.string.theme_builder_color_cell_fixed,
            "cellEditable" to R.string.theme_builder_color_cell_editable,
            "cellError" to R.string.theme_builder_color_cell_error,
            "divider" to R.string.theme_builder_color_divider,
        )
    }
}