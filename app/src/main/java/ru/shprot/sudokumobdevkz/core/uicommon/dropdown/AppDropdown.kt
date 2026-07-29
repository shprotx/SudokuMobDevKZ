package ru.shprot.sudokumobdevkz.core.uicommon.dropdown

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import kotlinx.collections.immutable.ImmutableList
import ru.shprot.sudokumobdevkz.core.base.util.empty
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun <T : DropdownItem> AppDropdown(
    modifier: Modifier,
    items: ImmutableList<T>,
    selected: T?,
    onSelect: (T) -> Unit,
    leadingPreview: (@Composable (T) -> Unit)? = null,
    trailingContent: (@Composable (T) -> Unit)? = null,
    placeholder: String = String.empty,
    enabled: Boolean = true,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(selected?.id) {
        expanded = false
    }

    val arrowRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "dropdown-arrow-rotation",
    )

    val borderColor = if (expanded && enabled) AppTheme.colors.primary else AppTheme.colors.divider

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusMedium),
        color = AppTheme.colors.backgroundCard,
        border = BorderStroke(
            width = AppTheme.sizes.dividerThickness,
            brush = SolidColor(borderColor),
        ),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            AppDropdownHeader(
                modifier = Modifier,
                selected = selected,
                placeholder = placeholder,
                enabled = enabled,
                arrowRotation = arrowRotation,
                leadingPreview = leadingPreview,
                onClick = { expanded = !expanded },
            )

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut(),
            ) {
                AppDropdownItemsList(
                    modifier = Modifier,
                    items = items,
                    trailingContent = trailingContent,
                    selected = selected,
                    enabled = enabled,
                    leadingPreview = leadingPreview,
                    onSelect = { item ->
                        onSelect(item)
                        expanded = false
                    },
                )
            }
        }
    }
}