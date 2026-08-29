package ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.components.screencontent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.domain.model.ThemeColors
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.button.ButtonDefault
import ru.shprot.sudokumobdevkz.core.uicommon.icon.AppIcons
import ru.shprot.sudokumobdevkz.core.uicommon.toolbar.ToolbarDefault
import ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.components.ColorCategoryList
import ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.components.ColorPickerSheet
import ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.components.quickColorSwatches
import ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.components.SaveThemeDialog
import ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.contract.ThemeBuilderUIEvent
import ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.contract.ThemeBuilderUIState

@Composable
fun ThemeBuilderScreenContent(
    uiState: ThemeBuilderUIState,
    onEvent: (ThemeBuilderUIEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .verticalScroll(rememberScrollState()),
    ) {
        ToolbarDefault(
            modifier = Modifier,
            title = stringResource(R.string.theme_builder_title),
            onLeadIconClick = { onEvent(ThemeBuilderUIEvent.BackClicked) },
        )

        if (hasLowContrast(uiState.colors)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.paddings.large, vertical = AppTheme.paddings.small),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier.size(AppTheme.sizes.iconSmall),
                    imageVector = AppIcons.Warning,
                    contentDescription = null,
                    tint = AppTheme.colors.warning,
                )
                Text(
                    modifier = Modifier.padding(start = AppTheme.paddings.small),
                    text = stringResource(R.string.theme_builder_low_contrast_warning),
                    style = AppTheme.typography.body3,
                    color = AppTheme.colors.warning,
                )
            }
        }

        ColorCategoryList(
            colors = uiState.colors,
            onColorKeyClick = { key -> onEvent(ThemeBuilderUIEvent.OpenColorPicker(key)) },
            modifier = Modifier.padding(horizontal = AppTheme.paddings.large),
        )

        ButtonDefault(
            modifier = Modifier
                .padding(horizontal = AppTheme.paddings.large)
                .padding(top = AppTheme.paddings.xxl, bottom = AppTheme.paddings.xxl)
                .navigationBarsPadding(),
            text = stringResource(R.string.theme_builder_save),
            onClick = { onEvent(ThemeBuilderUIEvent.SaveClicked) },
        )
    }

    if (uiState.showSaveDialog) {
        SaveThemeDialog(
            initialName = uiState.themeName,
            showNameError = uiState.showNameError,
            onConfirm = { name -> onEvent(ThemeBuilderUIEvent.ConfirmSave(name)) },
            onDismiss = { onEvent(ThemeBuilderUIEvent.DismissSaveDialog) },
        )
    }

    val selectedKey = uiState.selectedColorKey
    if (uiState.showColorPicker && selectedKey != null) {
        ColorPickerSheet(
            title = stringResource(selectedKey.labelRes),
            colors = uiState.colors,
            color = selectedKey.get(uiState.colors),
            swatches = quickColorSwatches,
            onColorChanged = { argb -> onEvent(ThemeBuilderUIEvent.ColorChanged(selectedKey, argb)) },
            onApply = { onEvent(ThemeBuilderUIEvent.ApplyColorPicker) },
            onCancel = { onEvent(ThemeBuilderUIEvent.CancelColorPicker) },
        )
    }
}

private fun hasLowContrast(colors: ThemeColors): Boolean {
    val bg = Color(colors.background)
    val text = Color(colors.text)
    val bgLum = 0.2126f * bg.red + 0.7152f * bg.green + 0.0722f * bg.blue
    val textLum = 0.2126f * text.red + 0.7152f * text.green + 0.0722f * text.blue
    val lighter = maxOf(bgLum, textLum) + 0.05f
    val darker = minOf(bgLum, textLum) + 0.05f
    return (lighter / darker) < 3.0f
}