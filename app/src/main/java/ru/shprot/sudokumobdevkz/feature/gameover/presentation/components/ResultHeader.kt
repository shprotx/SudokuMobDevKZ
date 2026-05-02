package ru.shprot.sudokumobdevkz.feature.gameover.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun ResultHeader(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    subtitle: String,
    isWin: Boolean,
) {
    val accentColor = if (isWin) AppTheme.colors.primary else AppTheme.colors.error

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = accentColor,
            modifier = Modifier.size(64.dp),
        )

        Text(
            modifier = Modifier.padding(top = AppTheme.paddings.large),
            text = title,
            style = AppTheme.typography.h1,
            color = AppTheme.colors.text,
            textAlign = TextAlign.Center,
        )

        Text(
            modifier = Modifier.padding(top = AppTheme.paddings.small),
            text = subtitle,
            style = AppTheme.typography.body1,
            color = AppTheme.colors.textSecondary,
            textAlign = TextAlign.Center,
        )
    }
}
