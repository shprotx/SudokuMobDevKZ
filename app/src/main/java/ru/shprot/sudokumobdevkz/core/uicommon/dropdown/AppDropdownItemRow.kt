package ru.shprot.sudokumobdevkz.core.uicommon.dropdown

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
internal fun <T : DropdownItem> AppDropdownItemRow(
    modifier: Modifier,
    item: T,
    isSelected: Boolean,
    enabled: Boolean,
    leadingPreview: (@Composable (T) -> Unit)?,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                role = Role.RadioButton
                selected = isSelected
            }
            .clickable(enabled = enabled, onClick = onClick)
            .padding(
                horizontal = AppTheme.paddings.large,
                vertical = AppTheme.paddings.default,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (leadingPreview != null) {
            Box(
                modifier = Modifier.padding(end = AppTheme.paddings.medium),
                contentAlignment = Alignment.Center,
            ) {
                leadingPreview(item)
            }
        }

        Text(
            modifier = Modifier
                .weight(1f),
            text = item.title(),
            style = AppTheme.typography.body1,
            color = when {
                !enabled -> AppTheme.colors.textSecondary
                isSelected -> AppTheme.colors.primary
                else -> AppTheme.colors.text
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        if (isSelected) {
            Icon(
                modifier = Modifier.size(AppTheme.sizes.iconMedium),
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = AppTheme.colors.primary,
            )
        }
    }
}