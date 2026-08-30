package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.shprot.sudokumobdevkz.core.base.domain.model.StatusItemId
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun GameStatusBar(
    modifier: Modifier,
    items: List<StatusItemId>,
    difficultyLabel: String,
    errors: Int,
    maxErrors: Int,
    lives: Int,
    timer: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.paddings.large),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items.forEach { itemId ->
            StatusItemContent(
                modifier = Modifier,
                itemId = itemId,
                difficultyLabel = difficultyLabel,
                errors = errors,
                maxErrors = maxErrors,
                lives = lives,
                timer = timer,
            )
        }
    }
}
