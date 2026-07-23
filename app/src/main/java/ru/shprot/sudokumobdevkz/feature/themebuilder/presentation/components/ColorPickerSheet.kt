package ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.domain.model.ThemeColors
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.button.ButtonDefault

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ColorPickerSheet(
    title: String,
    colors: ThemeColors,
    color: Long,
    swatches: List<Long>,
    onColorChanged: (Long) -> Unit,
    onApply: () -> Unit,
    onCancel: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val alpha = remember { argbAlpha(color) }
    var hsv by remember { mutableStateOf(argbToHsv(color)) }

    ModalBottomSheet(
        onDismissRequest = onCancel,
        sheetState = sheetState,
        containerColor = AppTheme.colors.backgroundCard,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = AppTheme.paddings.large)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(AppTheme.paddings.default),
        ) {
            PickerThemePreview(colors = colors)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = title,
                    style = AppTheme.typography.h3,
                    color = AppTheme.colors.text,
                )

                Box(
                    modifier = Modifier
                        .size(AppTheme.sizes.colorSwatch)
                        .clip(RoundedCornerShape(AppTheme.sizes.cornerRadiusSmall))
                        .background(Color(color.toInt()))
                        .border(
                            width = AppTheme.sizes.dividerThickness,
                            color = AppTheme.colors.divider,
                            shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusSmall),
                        ),
                    content = {},
                )
            }

            swatches.chunked(SWATCHES_PER_ROW).forEach { rowSwatches ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.small),
                ) {
                    rowSwatches.forEach { swatch ->
                        val isSelected = (swatch and 0xFFFFFF) == (color and 0xFFFFFF)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(AppTheme.sizes.cornerRadiusSmall))
                                .background(Color(swatch.toInt()))
                                .border(
                                    width = if (isSelected) AppTheme.sizes.elevationSmall else AppTheme.sizes.dividerThickness,
                                    color = if (isSelected) AppTheme.colors.primary else AppTheme.colors.divider,
                                    shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusSmall),
                                )
                                .clickable {
                                    hsv = argbToHsv(swatch)
                                    onColorChanged(hsv.toArgb(alpha))
                                },
                            content = {},
                        )
                    }
                }
            }

            ColorSlider(
                value = hsv.hue / MAX_HUE,
                gradient = hueGradient,
                onValueChange = { fraction ->
                    hsv = hsv.copy(hue = fraction * MAX_HUE)
                    onColorChanged(hsv.toArgb(alpha))
                },
            )

            ColorSlider(
                value = hsv.saturation,
                gradient = listOf(
                    Color.hsv(hsv.hue, 0f, hsv.value),
                    Color.hsv(hsv.hue, 1f, hsv.value),
                ),
                onValueChange = { fraction ->
                    hsv = hsv.copy(saturation = fraction)
                    onColorChanged(hsv.toArgb(alpha))
                },
            )

            ColorSlider(
                value = hsv.value,
                gradient = listOf(
                    Color.hsv(hsv.hue, hsv.saturation, 0f),
                    Color.hsv(hsv.hue, hsv.saturation, 1f),
                ),
                onValueChange = { fraction ->
                    hsv = hsv.copy(value = fraction)
                    onColorChanged(hsv.toArgb(alpha))
                },
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.paddings.small, bottom = AppTheme.paddings.large),
                horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.default),
            ) {
                ButtonDefault(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.cancel),
                    containerColor = AppTheme.colors.chipUnselected,
                    textColor = AppTheme.colors.text,
                    onClick = onCancel,
                )

                ButtonDefault(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.theme_builder_save),
                    onClick = onApply,
                )
            }
        }
    }
}

private const val SWATCHES_PER_ROW = 7
private const val MAX_HUE = 360f

private val hueGradient: List<Color> = listOf(
    Color.hsv(0f, 1f, 1f),
    Color.hsv(60f, 1f, 1f),
    Color.hsv(120f, 1f, 1f),
    Color.hsv(180f, 1f, 1f),
    Color.hsv(240f, 1f, 1f),
    Color.hsv(300f, 1f, 1f),
    Color.hsv(360f, 1f, 1f),
)