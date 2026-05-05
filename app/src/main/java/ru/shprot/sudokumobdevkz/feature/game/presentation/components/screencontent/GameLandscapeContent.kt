package ru.shprot.sudokumobdevkz.feature.game.presentation.components.screencontent
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.*

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.button.ToolbarCircleButton
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameUIEvent
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameUIState

@Composable
internal fun GameLandscapeContent(
    uiState: GameUIState,
    onEvent: (GameUIEvent) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) {
                onEvent(GameUIEvent.DeselectClicked)
            }
            .padding(
                horizontal = AppTheme.paddings.medium,
                vertical = AppTheme.paddings.small,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(48.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ToolbarCircleButton(
                modifier = Modifier,
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.go_back),
                onClick = { onEvent(GameUIEvent.BackClicked) },
            )

            ToolbarCircleButton(
                modifier = Modifier,
                icon = Icons.Filled.Refresh,
                contentDescription = stringResource(R.string.restart),
                onClick = { onEvent(GameUIEvent.NewGameClicked) },
            )

            ToolbarCircleButton(
                modifier = Modifier,
                icon = Icons.Filled.Pause,
                contentDescription = stringResource(R.string.pause),
                onClick = { onEvent(GameUIEvent.ShowPauseDialog) },
            )

            ToolbarCircleButton(
                modifier = Modifier,
                icon = Icons.Filled.Settings,
                contentDescription = stringResource(R.string.settings),
                onClick = { onEvent(GameUIEvent.SettingsClicked) },
            )

            Spacer(modifier = Modifier.height(AppTheme.paddings.default))

            Text(
                text = stringResource(uiState.difficulty.titleRes),
                style = AppTheme.typography.caption1,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.colors.primary,
            )

            Text(
                text = uiState.timer,
                style = AppTheme.typography.body5,
                color = AppTheme.colors.text,
            )

            Text(
                text = stringResource(R.string.errors_format, uiState.errors, uiState.maxErrors),
                style = AppTheme.typography.caption1,
                color = AppTheme.colors.textSecondary,
                textAlign = TextAlign.Center,
            )
        }

        NumberColumnVertical(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = AppTheme.paddings.small),
            numbers = (1..5).toList(),
            availableNumbers = uiState.availableNumbers,
            isNotesMode = uiState.isNotesEnabled,
            onNumberClick = { onEvent(GameUIEvent.NumberClicked(it)) },
        )

        SudokuGrid(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f),
            cells = uiState.cells,
            selectedRow = uiState.selectedRow,
            selectedCol = uiState.selectedCol,
            isPaused = uiState.isPaused,
            highlightedNumber = uiState.highlightedNumber,
            onCellClick = { row, col ->
                onEvent(GameUIEvent.CellClicked(row, col))
            },
        )

        NumberColumnVertical(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = AppTheme.paddings.small),
            numbers = (6..9).toList(),
            availableNumbers = uiState.availableNumbers,
            isNotesMode = uiState.isNotesEnabled,
            onNumberClick = { onEvent(GameUIEvent.NumberClicked(it)) },
        )

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(64.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ActionButton(
                icon = Icons.AutoMirrored.Filled.Undo,
                label = stringResource(R.string.undo),
                onClick = { onEvent(GameUIEvent.UndoClicked) },
            )

            ActionButton(
                icon = Icons.Outlined.Delete,
                label = stringResource(R.string.erase),
                onClick = { onEvent(GameUIEvent.EraseClicked) },
            )

            ActionButton(
                icon = Icons.Filled.Edit,
                label = stringResource(R.string.note),
                badge = when (uiState.isNotesEnabled) {
                    true -> stringResource(R.string.on_label)
                    false -> stringResource(R.string.off_label)
                },
                isHighlighted = uiState.isNotesEnabled,
                onClick = { onEvent(GameUIEvent.NotesToggled) },
            )

            ActionButton(
                icon = Icons.Filled.Lightbulb,
                label = stringResource(R.string.hint),
                badge = uiState.hintsRemaining.toString(),
                onClick = { onEvent(GameUIEvent.HintClicked) },
            )
        }
    }
}
