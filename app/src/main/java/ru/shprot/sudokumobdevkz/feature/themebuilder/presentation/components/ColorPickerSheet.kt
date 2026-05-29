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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val alpha = argbAlpha(color)
    val red = argbRed(color)
    val green = argbGreen(color)
    val blue = argbBlue(color)

    fun channel(red: Int, green: Int, blue: Int) = Color(red / 255f, green / 255f, blue / 255f)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
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
            MiniThemePreview(colors = colors)

            Text(
                text = title,
                style = AppTheme.typography.h3,
                color = AppTheme.colors.text,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.small),
            ) {
                swatches.forEach { swatch ->
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
                                onColorChanged(composeArgb(alpha, argbRed(swatch), argbGreen(swatch), argbBlue(swatch)))
                            },
                    )
                }
            }

            ColorChannelSlider(
                label = stringResource(R.string.color_channel_red),
                value = red,
                gradient = listOf(channel(0, green, blue), channel(255, green, blue)),
                onValueChange = { onColorChanged(composeArgb(alpha, it, green, blue)) },
            )

            ColorChannelSlider(
                label = stringResource(R.string.color_channel_green),
                value = green,
                gradient = listOf(channel(red, 0, blue), channel(red, 255, blue)),
                onValueChange = { onColorChanged(composeArgb(alpha, red, it, blue)) },
            )

            ColorChannelSlider(
                label = stringResource(R.string.color_channel_blue),
                value = blue,
                gradient = listOf(channel(red, green, 0), channel(red, green, 255)),
                onValueChange = { onColorChanged(composeArgb(alpha, red, green, it)) },
            )

            ButtonDefault(
                modifier = Modifier.padding(
                    top = AppTheme.paddings.small,
                    bottom = AppTheme.paddings.large,
                ),
                text = stringResource(R.string.done),
                onClick = onDismiss,
            )
        }
    }
}
