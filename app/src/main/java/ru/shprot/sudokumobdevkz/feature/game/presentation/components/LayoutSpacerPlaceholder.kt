package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
internal fun LayoutSpacerPlaceholder(
    modifier: Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(AppTheme.sizes.buttonHeightSmall),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.layout_block_spacer),
            style = AppTheme.typography.body2,
            color = AppTheme.colors.textSecondary,
        )
    }
}
