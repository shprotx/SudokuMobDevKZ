package ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ColorPickerSheet(
    initialColor: Long,
    onColorChanged: (Long) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    val controller = rememberColorPickerController()

    LaunchedEffect(initialColor) {
        controller.selectByColor(Color(initialColor), fromUser = false)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = AppTheme.colors.backgroundCard,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.paddings.large)
                .navigationBarsPadding(),
        ) {
            HsvColorPicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                controller = controller,
                onColorChanged = { envelope ->
                    if (envelope.fromUser) {
                        onColorChanged(envelope.color.toArgb().toLong() and 0xFFFFFFFFL)
                    }
                },
            )

            Spacer(modifier = Modifier.height(AppTheme.paddings.medium))

            BrightnessSlider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AppTheme.sizes.buttonHeightSmall),
                controller = controller,
            )

            Spacer(modifier = Modifier.height(AppTheme.paddings.xxl))
        }
    }
}