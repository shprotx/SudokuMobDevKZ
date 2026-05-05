package ru.shprot.sudokumobdevkz.core.uicommon.button

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun ButtonText(
    modifier: Modifier,
    text: String,
    textColor: Color = AppTheme.colors.textSecondary,
    onClick: () -> Unit,
) {

    TextButton(
        modifier = modifier,
        onClick = onClick,
    ) {

        Text(
            text = text,
            style = AppTheme.typography.body2,
            color = textColor,
        )
    }
}
