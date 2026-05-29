package ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.data.repository.ISettingsRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.IThemeRepository
import ru.shprot.sudokumobdevkz.core.base.domain.model.CustomTheme
import ru.shprot.sudokumobdevkz.core.base.presentation.viewmodel.BaseViewModel
import ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.contract.ThemeBuilderUIEffect
import ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.contract.ThemeBuilderUIEvent
import ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.contract.ThemeBuilderUIState
import ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.model.ThemeColorKey
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

    private fun handleColorChanged(key: ThemeColorKey, argb: Long) {
        updateState { copy(colors = key.set(colors, argb)) }
    }
}