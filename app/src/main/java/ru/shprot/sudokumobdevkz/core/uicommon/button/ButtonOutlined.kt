package ru.shprot.sudokumobdevkz.core.uicommon.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun ButtonOutlined(
    modifier: Modifier,
    text: String,
    borderColor: Color = AppTheme.colors.primary,
    textColor: Color = AppTheme.colors.text,
    onClick: () -> Unit,
) {

    OutlinedButton(
        modifier = modifier
            .fillMaxWidth()
            .height(AppTheme.sizes.buttonHeight),
        onClick = onClick,
        shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
        border = BorderStroke(AppTheme.sizes.dividerThickness, borderColor),
    ) {

        Text(
            text = text,
            style = AppTheme.typography.button,
            color = textColor,
        )
    }
}