package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun GameToolbar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onRestartClick: () -> Unit,
    onPauseClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = AppTheme.paddings.small,
                vertical = AppTheme.paddings.medium,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Назад",
                tint = AppTheme.colors.text,
            )
        }

        Text(
            modifier = Modifier.weight(1f),
            text = "Судоку",
            style = AppTheme.typography.h3,
            color = AppTheme.colors.text,
        )

        IconButton(onClick = onRestartClick) {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = "Рестарт",
                tint = AppTheme.colors.iconTint,
            )
        }

        IconButton(onClick = onPauseClick) {
            Icon(
                imageVector = Icons.Filled.Pause,
                contentDescription = "Пауза",
                tint = AppTheme.colors.iconTint,
            )
        }

        IconButton(onClick = onSettingsClick) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "Настройки",
                tint = AppTheme.colors.iconTint,
            )
        }
    }
}
