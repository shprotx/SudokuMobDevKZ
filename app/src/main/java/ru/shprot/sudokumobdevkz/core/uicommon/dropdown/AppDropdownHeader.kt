package ru.shprot.sudokumobdevkz.core.uicommon.dropdown

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.icon.AppIcons

@Composable
internal fun <T : DropdownItem> AppDropdownHeader(
    modifier: Modifier,
    selected: T?,
    placeholder: String,
    enabled: Boolean,
    arrowRotation: Float,
    leadingPreview: (@Composable (T) -> Unit)?,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .semantics { role = Role.Button }
            .clickable(enabled = enabled, onClick = onClick)
            .padding(
                horizontal = AppTheme.paddings.large,
                vertical = AppTheme.paddings.default,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (selected != null && leadingPreview != null) {
            Box(
                modifier = Modifier.padding(end = AppTheme.paddings.medium),
                contentAlignment = Alignment.Center,
            ) {
                leadingPreview(selected)
            }
        }

        Text(
            modifier = Modifier.weight(1f),
            text = selected?.title() ?: placeholder,
            style = AppTheme.typography.body1,
            color = if (enabled) AppTheme.colors.text else AppTheme.colors.textSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Icon(
            modifier = Modifier
                .size(AppTheme.sizes.iconMedium)
                .rotate(arrowRotation),
            imageVector = AppIcons.ChevronDown,
            contentDescription = null,
            tint = if (enabled) AppTheme.colors.text else AppTheme.colors.textSecondary,
        )
    }
}