package ru.shprot.sudokumobdevkz.core.uicommon.button

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun ButtonDefault(
    modifier: Modifier,
    text: String,
    containerColor: Color = AppTheme.colors.primary,
    textColor: Color = AppTheme.colors.textOnPrimary,
    icon: ImageVector? = null,
    onClick: () -> Unit,
) {

    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(AppTheme.sizes.buttonHeight),
        onClick = onClick,
        shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
    ) {

        if (icon != null) {
            Icon(
                modifier = Modifier
                    .padding(end = AppTheme.paddings.medium)
                    .size(AppTheme.sizes.iconMedium),
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
            )
        }

        Text(
            text = text,
            style = AppTheme.typography.button,
            color = textColor,
        )
    }
}
