package ru.shprot.sudokumobdevkz.feature.statistic.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.shprot.sudokumobdevkz.core.base.data.util.DateTimeUtils
import ru.shprot.sudokumobdevkz.core.base.domain.model.DailyPlaytime
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
internal fun TimeBarChart(
    modifier: Modifier,
    dailyPlaytimes: List<DailyPlaytime>,
) {
    val rawMax = dailyPlaytimes.maxOf { it.totalSeconds }.coerceAtLeast(60)
    val chartMax = DateTimeUtils.chartMaxSeconds(rawMax)
    val chartHeight = 160.dp
    val barWidth = 36.dp
    val timeLabels = DateTimeUtils.generateTimeLabels(rawMax)
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM", Locale.getDefault())
    val listState = rememberLazyListState()

    LaunchedEffect(dailyPlaytimes.size) {
        if (dailyPlaytimes.isNotEmpty()) {
            listState.scrollToItem(dailyPlaytimes.size - 1)
        }
    }

    Row(modifier = modifier) {
        Column(
            modifier = Modifier
                .width(40.dp)
                .height(chartHeight),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            timeLabels.reversed().forEach { label ->
                Text(
                    text = label,
                    style = AppTheme.typography.caption2,
                    color = AppTheme.colors.textSecondary,
                )
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            Box {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(chartHeight),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    timeLabels.reversed().forEach { _ ->
                        HorizontalDivider(
                            color = AppTheme.colors.divider.copy(alpha = 0.5f),
                            thickness = 0.5.dp,
                        )
                    }
                }

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    state = listState,
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.medium),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    items(dailyPlaytimes) { day ->
                        DailyBarColumn(
                            day = day,
                            maxTime = chartMax,
                            barWidth = barWidth,
                            chartHeight = chartHeight,
                            dateFormatter = dateFormatter,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DailyBarColumn(
    day: DailyPlaytime,
    maxTime: Int,
    barWidth: androidx.compose.ui.unit.Dp,
    chartHeight: androidx.compose.ui.unit.Dp,
    dateFormatter: DateTimeFormatter,
) {
    val fraction = (day.totalSeconds.toFloat() / maxTime).coerceIn(0f, 1f)

    Column(
        modifier = Modifier.width(barWidth),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .height(chartHeight)
                .width(barWidth),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = if (day.totalSeconds > 0) DateTimeUtils.formatShortTime(day.totalSeconds) else "",
                    style = AppTheme.typography.caption2,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.colors.textSecondary,
                )

                Box(
                    modifier = Modifier
                        .padding(top = AppTheme.paddings.extraSmall)
                        .width(barWidth)
                        .fillMaxHeight(fraction)
                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                        .background(AppTheme.colors.primary),
                )
            }
        }

        Text(
            modifier = Modifier
                .padding(top = AppTheme.paddings.small)
                .width(barWidth),
            text = day.date.format(dateFormatter),
            style = AppTheme.typography.caption2,
            color = AppTheme.colors.textSecondary,
            textAlign = TextAlign.Center,
        )
    }
}
