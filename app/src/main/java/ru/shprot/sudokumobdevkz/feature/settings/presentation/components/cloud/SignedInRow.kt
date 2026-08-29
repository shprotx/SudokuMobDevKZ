package ru.shprot.sudokumobdevkz.feature.settings.presentation.components.cloud

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.avatar.PgsAvatar
import ru.shprot.sudokumobdevkz.core.uicommon.icon.AppIcons

@Composable
fun SignedInRow(
    modifier: Modifier,
    displayName: String,
    avatarUrl: String?,
    onSignOutClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = AppTheme.paddings.default),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PgsAvatar(
            modifier = Modifier,
            size = AppTheme.sizes.iconMedium,
            avatarUrl = avatarUrl,
        )

        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = AppTheme.paddings.default),
            text = displayName.ifBlank { stringResource(R.string.cloud_player_anonymous) },
            style = AppTheme.typography.body1,
            color = AppTheme.colors.text,
        )

        Row(
            modifier = Modifier
                .clickable(onClick = onSignOutClick)
                .padding(
                    horizontal = AppTheme.paddings.default,
                    vertical = AppTheme.paddings.small,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.cloud_sign_out),
                style = AppTheme.typography.body3,
                color = AppTheme.colors.textSecondary,
            )

            Icon(
                imageVector = AppIcons.Logout,
                contentDescription = null,
                tint = AppTheme.colors.textSecondary,
                modifier = Modifier
                    .padding(start = AppTheme.paddings.small)
                    .size(AppTheme.sizes.iconSmall),
            )
        }
    }
}
