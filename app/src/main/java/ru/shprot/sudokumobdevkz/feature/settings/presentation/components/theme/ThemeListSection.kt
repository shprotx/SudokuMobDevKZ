package ru.shprot.sudokumobdevkz.feature.settings.presentation.components.theme

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kotlinx.collections.immutable.ImmutableList
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.domain.model.CustomTheme
import ru.shprot.sudokumobdevkz.core.uicommon.button.ButtonText
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.settings.SettingsCard
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.settings.SettingsDivider

@Composable
internal fun ThemeListSection(
    themes: ImmutableList<CustomTheme>,
    selectedThemeId: String,
    onThemeSelected: (String) -> Unit,
    onEditTheme: (String) -> Unit,
    onDeleteTheme: (String) -> Unit,
    onAddTheme: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SettingsCard(modifier = modifier) {
        themes.forEachIndexed { index, theme ->
            ThemeListItem(
                theme = theme,
                isSelected = theme.id == selectedThemeId,
                onSelected = { onThemeSelected(theme.id) },
                onEdit = { onEditTheme(theme.id) },
                onDelete = { onDeleteTheme(theme.id) },
            )
            if (index < themes.lastIndex) {
                SettingsDivider(modifier = Modifier)
            }
        }

        SettingsDivider(modifier = Modifier)

        ButtonText(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.theme_list_add_theme),
            onClick = onAddTheme,
        )
    }
}
