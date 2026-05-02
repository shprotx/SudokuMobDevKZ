package ru.shprot.sudokumobdevkz.feature.menu.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun NewGameButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusXL),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.primary),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = AppTheme.paddings.xxl),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(AppTheme.sizes.iconXL)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(AppTheme.sizes.iconMedium),
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = AppTheme.paddings.large),
            ) {
                Text(
                    text = "Новая игра",
                    style = AppTheme.typography.h3,
                    color = AppTheme.colors.textOnPrimary,
                )

                Text(
                    text = "Начать новую головоломку",
                    style = AppTheme.typography.body5,
                    color = AppTheme.colors.textOnPrimary.copy(alpha = 0.8f),
                )
            }

            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = AppTheme.colors.textOnPrimary.copy(alpha = 0.7f),
                modifier = Modifier.size(AppTheme.sizes.iconMedium),
            )
        }
    }
}
