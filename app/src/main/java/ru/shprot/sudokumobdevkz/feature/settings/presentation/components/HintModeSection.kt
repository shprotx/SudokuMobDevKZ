package ru.shprot.sudokumobdevkz.feature.settings.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.domain.model.HintMode
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.settings.SettingsCard
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.settings.SettingsDivider

@Composable
fun HintModeSection(
    modifier: Modifier,
    selectedMode: HintMode,
    onModeSelected: (HintMode) -> Unit,
) {
    SettingsCard(modifier = modifier) {
        HintModeOption(
            title = stringResource(R.string.hint_mode_single_shot_title),
            subtitle = stringResource(R.string.hint_mode_single_shot_subtitle),
            selected = selectedMode == HintMode.SINGLE_SHOT,
            onClick = { onModeSelected(HintMode.SINGLE_SHOT) },
        )
        SettingsDivider(modifier = Modifier)
        HintModeOption(
            title = stringResource(R.string.hint_mode_toggle_title),
            subtitle = stringResource(R.string.hint_mode_toggle_subtitle),
            selected = selectedMode == HintMode.TOGGLE,
            onClick = { onModeSelected(HintMode.TOGGLE) },
        )
    }
}

@Composable
internal fun HintModeOption(
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = AppTheme.paddings.small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = AppTheme.colors.primary),
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = AppTheme.paddings.small),
        ) {
            Text(
                text = title,
                style = AppTheme.typography.body1,
                color = AppTheme.colors.text,
            )
            Text(
                text = subtitle,
                style = AppTheme.typography.body3,
                color = AppTheme.colors.textSecondary,
            )
        }
    }
}