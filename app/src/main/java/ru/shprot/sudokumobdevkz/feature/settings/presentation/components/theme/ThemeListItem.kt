package ru.shprot.sudokumobdevkz.feature.settings.presentation.components.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.domain.model.CustomTheme
import ru.shprot.sudokumobdevkz.core.base.domain.model.toAppColors
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
internal fun ThemeListItem(
    theme: CustomTheme,
    isSelected: Boolean,
    onSelected: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    var showMenu by remember { mutableStateOf(false) }
    val appColors = remember(theme.colors) { theme.colors.toAppColors() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelected)
            .padding(vertical = AppTheme.paddings.small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onSelected,
            colors = RadioButtonDefaults.colors(selectedColor = AppTheme.colors.primary),
        )

        Text(
            modifier = Modifier.weight(1f),
            text = theme.name,
            style = AppTheme.typography.body1,
            color = AppTheme.colors.text,
        )

        Spacer(modifier = Modifier.width(AppTheme.paddings.small))

        Row {
            listOf(
                appColors.background,
                appColors.primary,
                appColors.text,
                appColors.cellEditable,
            ).forEach { color ->
                Box(
                    modifier = Modifier
                        .size(AppTheme.sizes.themePreviewDot)
                        .background(color, CircleShape),
                )
            }
        }

        if (!theme.isBuiltIn) {
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = null,
                        tint = AppTheme.colors.iconTint,
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.theme_list_edit)) },
                        onClick = {
                            showMenu = false
                            onEdit()
                        },
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(R.string.theme_list_delete),
                                color = AppTheme.colors.error,
                            )
                        },
                        onClick = {
                            showMenu = false
                            onDelete()
                        },
                    )
                }
            }
        }
    }
}
