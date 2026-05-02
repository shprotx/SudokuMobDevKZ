package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun PauseDialog(
    timer: String,
    errors: Int,
    maxErrors: Int,
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onExit: () -> Unit,
) {
    Dialog(onDismissRequest = onResume) {
        Card(
            shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusXL),
            colors = CardDefaults.cardColors(containerColor = AppTheme.colors.backgroundCard),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.paddings.xxl),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(AppTheme.colors.primaryLight),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Pause,
                        contentDescription = null,
                        tint = AppTheme.colors.primary,
                        modifier = Modifier.size(AppTheme.sizes.iconLarge),
                    )
                }

                Text(
                    modifier = Modifier.padding(top = AppTheme.paddings.large),
                    text = "Пауза",
                    style = AppTheme.typography.h2,
                    color = AppTheme.colors.text,
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppTheme.paddings.extraLarge),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Timer,
                            contentDescription = null,
                            tint = AppTheme.colors.textSecondary,
                            modifier = Modifier.size(AppTheme.sizes.iconSmall),
                        )

                        Text(
                            modifier = Modifier.padding(start = AppTheme.paddings.small),
                            text = timer,
                            style = AppTheme.typography.body2,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.colors.text,
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = null,
                            tint = AppTheme.colors.error,
                            modifier = Modifier.size(AppTheme.sizes.iconSmall),
                        )

                        Text(
                            modifier = Modifier.padding(start = AppTheme.paddings.small),
                            text = "$errors/$maxErrors",
                            style = AppTheme.typography.body2,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.colors.text,
                        )
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(top = AppTheme.paddings.extraLarge),
                    color = AppTheme.colors.divider,
                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppTheme.paddings.extraLarge)
                        .height(AppTheme.sizes.buttonHeight),
                    onClick = onResume,
                    shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.primary),
                ) {
                    Text(
                        text = "Продолжить",
                        style = AppTheme.typography.button,
                        color = AppTheme.colors.textOnPrimary,
                    )
                }

                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppTheme.paddings.default)
                        .height(AppTheme.sizes.buttonHeight),
                    onClick = onRestart,
                    shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
                ) {
                    Text(
                        text = "Начать заново",
                        style = AppTheme.typography.button,
                        color = AppTheme.colors.text,
                    )
                }

                TextButton(
                    modifier = Modifier.padding(top = AppTheme.paddings.medium),
                    onClick = onExit,
                ) {
                    Text(
                        text = "Выйти в меню",
                        style = AppTheme.typography.body2,
                        color = AppTheme.colors.textSecondary,
                    )
                }
            }
        }
    }
}
