package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.icon.AppIcons
import ru.shprot.sudokumobdevkz.core.uicommon.button.ButtonDefault
import ru.shprot.sudokumobdevkz.core.uicommon.button.ButtonOutlined
import ru.shprot.sudokumobdevkz.core.uicommon.button.ButtonText

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
                        modifier = Modifier.size(AppTheme.sizes.iconLarge),
                        imageVector = AppIcons.Pause,
                        contentDescription = null,
                        tint = AppTheme.colors.primary,
                    )
                }

                Text(
                    modifier = Modifier.padding(top = AppTheme.paddings.large),
                    text = stringResource(R.string.pause),
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
                            imageVector = AppIcons.Clock,
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
                            imageVector = AppIcons.Heart,
                            contentDescription = null,
                            tint = AppTheme.colors.error,
                            modifier = Modifier.size(AppTheme.sizes.iconSmall),
                        )

                        Text(
                            modifier = Modifier.padding(start = AppTheme.paddings.small),
                            text = stringResource(R.string.errors_format, errors, maxErrors),
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

                ButtonDefault(
                    modifier = Modifier.padding(top = AppTheme.paddings.extraLarge),
                    text = stringResource(R.string.resume),
                    onClick = onResume,
                )

                ButtonOutlined(
                    modifier = Modifier.padding(top = AppTheme.paddings.default),
                    text = stringResource(R.string.restart),
                    onClick = onRestart,
                )

                ButtonText(
                    modifier = Modifier.padding(top = AppTheme.paddings.medium),
                    text = stringResource(R.string.exit_to_menu),
                    onClick = onExit,
                )
            }
        }
    }
}
