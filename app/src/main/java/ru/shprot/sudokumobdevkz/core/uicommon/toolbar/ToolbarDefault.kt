package ru.shprot.sudokumobdevkz.core.uicommon.toolbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.button.ToolbarCircleButton

@Composable
fun ToolbarDefault(
    modifier: Modifier,
    title: String,
    leadIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    endIcon: ImageVector = Icons.Filled.Settings,
    onLeadIconClick: () -> Unit,
    onEndIconClick: (() -> Unit)? = null,
) {

    Box(
        modifier = modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .padding(
                horizontal = AppTheme.paddings.small,
                vertical = AppTheme.paddings.medium,
            ),
    ) {

        ToolbarCircleButton(
            modifier = Modifier,
            icon = leadIcon,
            contentDescription = stringResource(R.string.go_back),
            onClick = onLeadIconClick,
        )

        Text(
            modifier = Modifier
                .align(Alignment.Center),
            text = title,
            style = AppTheme.typography.h3,
            color = AppTheme.colors.text,
            textAlign = TextAlign.Center,
        )

        onEndIconClick?.let {
            ToolbarCircleButton(
                modifier = Modifier
                    .align(Alignment.CenterEnd),
                icon = endIcon,
                contentDescription = stringResource(R.string.settings),
                onClick = onEndIconClick,
            )
        }
    }
}