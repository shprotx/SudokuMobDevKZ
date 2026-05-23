package ru.shprot.sudokumobdevkz.feature.settings.presentation.components.cloud

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

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
        Box(
            modifier = Modifier
                .size(AppTheme.sizes.iconMedium)
                .clip(CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            if (avatarUrl != null) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = null,
                    modifier = Modifier.size(AppTheme.sizes.iconMedium),
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    tint = AppTheme.colors.iconTint,
                    modifier = Modifier.size(AppTheme.sizes.iconMedium),
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = AppTheme.paddings.default),
        ) {
            Text(
                text = displayName.ifBlank { stringResource(R.string.cloud_player_anonymous) },
                style = AppTheme.typography.body1,
                color = AppTheme.colors.text,
            )

            Text(
                text = stringResource(R.string.cloud_signed_in_subtitle),
                style = AppTheme.typography.body3,
                color = AppTheme.colors.textSecondary,
            )
        }

        Icon(
            imageVector = Icons.Filled.Logout,
            contentDescription = stringResource(R.string.cloud_sign_out),
            tint = AppTheme.colors.iconTint,
            modifier = Modifier
                .clickable(onClick = onSignOutClick)
                .padding(AppTheme.paddings.small)
                .size(AppTheme.sizes.iconSmall),
        )
    }
}
