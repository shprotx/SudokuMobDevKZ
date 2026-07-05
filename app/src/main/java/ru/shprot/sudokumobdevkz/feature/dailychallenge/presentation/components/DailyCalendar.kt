package ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.contract.DayCellUiModel

@Composable
internal fun DailyCalendar(
    modifier: Modifier = Modifier,
    visibleMonthLabel: String,
    weekDayLabels: List<String>,
    firstDayOffset: Int,
    calendarDays: List<DayCellUiModel>,
    monthCompletedCount: Int,
    monthTotalDays: Int,
    canGoPrev: Boolean,
    canGoNext: Boolean,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onDayClicked: (String) -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusXL),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.backgroundCard),
        elevation = CardDefaults.cardElevation(defaultElevation = AppTheme.sizes.elevationSmall),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.paddings.default),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = onPrevMonth,
                    enabled = canGoPrev,
                ) {
                    Icon(
                        modifier = Modifier.size(AppTheme.sizes.iconMedium),
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = stringResource(R.string.daily_calendar_prev_month),
                        tint = if (canGoPrev) AppTheme.colors.iconTint else AppTheme.colors.textSecondary,
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = visibleMonthLabel,
                        style = AppTheme.typography.body2,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.colors.text,
                        textAlign = TextAlign.Center,
                    )

                    Text(
                        text = "$monthCompletedCount/$monthTotalDays",
                        style = AppTheme.typography.caption1,
                        color = AppTheme.colors.textSecondary,
                        textAlign = TextAlign.Center,
                    )
                }

                IconButton(
                    onClick = onNextMonth,
                    enabled = canGoNext,
                ) {
                    Icon(
                        modifier = Modifier.size(AppTheme.sizes.iconMedium),
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = stringResource(R.string.daily_calendar_next_month),
                        tint = if (canGoNext) AppTheme.colors.iconTint else AppTheme.colors.textSecondary,
                    )
                }
            }

            if (weekDayLabels.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppTheme.paddings.small),
                ) {
                    weekDayLabels.forEach { label ->
                        Text(
                            modifier = Modifier.weight(1f),
                            text = label,
                            style = AppTheme.typography.caption1,
                            color = AppTheme.colors.textSecondary,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }

            val totalCells = firstDayOffset + calendarDays.size
            val rowCount = (totalCells + 6) / 7

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.paddings.extraSmall),
            ) {
                repeat(rowCount) { row ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        repeat(7) { col ->
                            val index = row * 7 + col - firstDayOffset
                            if (index < 0 || index >= calendarDays.size) {
                                Box(modifier = Modifier.weight(1f))
                            } else {
                                val dayModel = calendarDays[index]
                                CalendarDayCell(
                                    modifier = Modifier.weight(1f),
                                    model = dayModel,
                                    onClicked = { onDayClicked(dayModel.dateKey) },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}