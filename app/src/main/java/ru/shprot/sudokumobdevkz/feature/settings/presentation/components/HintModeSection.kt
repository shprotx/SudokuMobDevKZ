package ru.shprot.sudokumobdevkz.feature.settings.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.domain.model.HintMode
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
