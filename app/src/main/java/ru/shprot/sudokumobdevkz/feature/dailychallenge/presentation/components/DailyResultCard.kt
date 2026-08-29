package ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.icon.AppIcons

@Composable
fun DailyResultCard(
    modifier: Modifier,
    title: String,
    timeLabel: String,
    timeValue: String,
    errorsLabel: String,
    errorsValue: String,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.backgroundCard),
        elevation = CardDefaults.cardElevation(defaultElevation = AppTheme.sizes.elevationSmall),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.paddings.default),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier.size(AppTheme.sizes.iconSmall),
                    imageVector = AppIcons.CheckCircle,
                    contentDescription = null,
                    tint = AppTheme.colors.primary,
                )

                Text(
                    modifier = Modifier.padding(start = AppTheme.paddings.small),
                    text = title,
                    style = AppTheme.typography.body1,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.colors.text,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.paddings.medium),
                horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.large),
            ) {
                ResultStat(
                    modifier = Modifier,
                    icon = AppIcons.Stopwatch,
                    label = timeLabel,
                    value = timeValue,
                )

                ResultStat(
                    modifier = Modifier,
                    icon = AppIcons.Heart,
                    label = errorsLabel,
                    value = errorsValue,
                )
            }
        }
    }
}