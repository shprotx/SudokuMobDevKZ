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
import ru.shprot.sudokumobdevkz.core.base.domain.model.ThemeMode
import ru.shprot.sudokumobdevkz.core.base.presentation.viewmodel.BaseViewModel
import ru.shprot.sudokumobdevkz.core.theme.BuiltInTheme
import ru.shprot.sudokumobdevkz.core.theme.ThemePalettes
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
        viewModelScope.launch(exceptionHandler) {
            if (themeId != null) {
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
            } else {
                val initialColors = resolveInitialColors()
                updateState { copy(colors = initialColors) }
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

            ThemeBuilderUIEvent.ApplyColorPicker ->
                updateState {
                    copy(showColorPicker = false, selectedColorKey = null, pickerOriginalColor = null)
                }

            ThemeBuilderUIEvent.CancelColorPicker ->
                handleCancelColorPicker()

            is ThemeBuilderUIEvent.ConfirmSave ->
                handleConfirmSave(event.name)

            is ThemeBuilderUIEvent.OpenColorPicker ->
                updateState {
                    copy(
                        selectedColorKey = event.colorKey,
                        pickerOriginalColor = event.colorKey.get(colors),
                        showColorPicker = true,
                    )
                }

            is ThemeBuilderUIEvent.ColorChanged ->
                updateState { copy(colors = event.colorKey.set(colors, event.argb)) }
        }

    private suspend fun resolveInitialColors(): ThemeColors {
        val themeModeId = settingsRepository.currentSettings.themeModeId
        return when (themeModeId) {
            ThemeMode.Dark.id -> BuiltInTheme.DARK.colors
            ThemeMode.Light.id, ThemeMode.System.id -> BuiltInTheme.LIGHT.colors
            else -> ThemePalettes.byId(themeModeId)?.colors
                ?: themeRepository.getById(themeModeId)?.colors
                ?: BuiltInTheme.LIGHT.colors
        }
    }

    private fun handleCancelColorPicker() {
        val key = currentState.selectedColorKey
        val original = currentState.pickerOriginalColor
        updateState {
            copy(
                colors = if (key != null && original != null) key.set(colors, original) else colors,
                showColorPicker = false,
                selectedColorKey = null,
                pickerOriginalColor = null,
            )
        }
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
}