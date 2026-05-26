package ru.shprot.sudokumobdevkz.core.uicommon.dropdown

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.ImmutableList
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
internal fun <T : DropdownItem> AppDropdownItemsList(
    modifier: Modifier,
    items: ImmutableList<T>,
    selected: T?,
    enabled: Boolean,
    leadingPreview: (@Composable (T) -> Unit)?,
    onSelect: (T) -> Unit,
) {
    if (items.size > DROPDOWN_LAZY_THRESHOLD) {
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(max = AppTheme.sizes.dropdownMaxHeight),
        ) {
            items(
                items = items,
                key = { it.id },
            ) { item ->
                AppDropdownItemRow(
                    modifier = Modifier,
                    item = item,
                    isSelected = item.id == selected?.id,
                    enabled = enabled,
                    leadingPreview = leadingPreview,
                    onClick = { onSelect(item) },
                )
            }
        }
    } else {
        Column(modifier = modifier.fillMaxWidth()) {
            items.forEach { item ->
                AppDropdownItemRow(
                    modifier = Modifier,
                    item = item,
                    isSelected = item.id == selected?.id,
                    enabled = enabled,
                    leadingPreview = leadingPreview,
                    onClick = { onSelect(item) },
                )
            }
        }
    }
}

private const val DROPDOWN_LAZY_THRESHOLD = 7