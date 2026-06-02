package ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.domain.model.dotColor
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.contract.DayCellState
import ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.contract.DayCellUiModel

@Composable
internal fun CalendarDayCell(
    modifier: Modifier = Modifier,
    model: DayCellUiModel,
    onClicked: () -> Unit,
) {
    val state = model.state
    val isClickable = state !is DayCellState.Future

    Box(
        modifier = modifier.aspectRatio(1f),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(AppTheme.paddings.extraSmall)
                .clip(CircleShape)
                .then(
                    when (state) {
                        is DayCellState.Completed ->
                            Modifier.background(state.difficulty.dotColor())
                        is DayCellState.Today ->
                            Modifier.border(
                                width = AppTheme.sizes.dividerThickness,
                                color = AppTheme.colors.primary,
                                shape = CircleShape,
                            )
                        is DayCellState.Missed -> Modifier
                        is DayCellState.Future -> Modifier
                    }
                )
                .then(
                    if (isClickable) Modifier.clickable(onClick = onClicked) else Modifier
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (state is DayCellState.Completed) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = stringResource(R.string.daily_calendar_completed),
                    tint = AppTheme.colors.textOnPrimary,
                    modifier = Modifier.fillMaxSize(0.5f),
                )
            } else {
                Text(
                    text = model.dayNumber.toString(),
                    style = AppTheme.typography.body5,
                    fontWeight = when (state) {
                        is DayCellState.Today -> FontWeight.SemiBold
                        else -> FontWeight.Normal
                    },
                    color = when (state) {
                        is DayCellState.Today -> AppTheme.colors.primary
                        is DayCellState.Missed -> AppTheme.colors.text
                        else -> AppTheme.colors.textSecondary
                    },
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}