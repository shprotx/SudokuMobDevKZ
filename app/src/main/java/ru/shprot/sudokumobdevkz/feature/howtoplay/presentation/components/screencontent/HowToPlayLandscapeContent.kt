package ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.components.screencontent
import ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.components.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.button.ButtonDefault
import ru.shprot.sudokumobdevkz.core.uicommon.toolbar.ToolbarDefault
import ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.contract.HowToPlayUIEvent
import ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.contract.HowToPlayUIState

@Composable
internal fun HowToPlayLandscapeContent(
    uiState: HowToPlayUIState,
    onEvent: (HowToPlayUIEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .statusBarsPadding(),
    ) {
        ToolbarDefault(
            modifier = Modifier,
            title = stringResource(R.string.how_to_play),
            onLeadIconClick = { onEvent(HowToPlayUIEvent.BackClicked) },
        )

        Row(
            modifier = Modifier
                .weight(1f)
                .navigationBarsPadding()
                .padding(horizontal = AppTheme.paddings.large),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.large),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
            ) {
                Text(
                    modifier = Modifier.padding(top = AppTheme.paddings.large),
                    text = stringResource(R.string.learn_in_3_steps),
                    style = AppTheme.typography.h3,
                    color = AppTheme.colors.text,
                )

                Text(
                    modifier = Modifier.padding(top = AppTheme.paddings.small),
                    text = stringResource(R.string.simple_rules),
                    style = AppTheme.typography.body3,
                    color = AppTheme.colors.textSecondary,
                )

                TutorialStepCardHorizontal(
                    modifier = Modifier.padding(top = AppTheme.paddings.large),
                    stepNumber = 1,
                    title = stringResource(R.string.no_repetitions),
                    description = stringResource(R.string.how_desc_one),
                    imageRes = R.drawable.howone,
                )

                TutorialStepCardHorizontal(
                    modifier = Modifier.padding(top = AppTheme.paddings.default),
                    stepNumber = 2,
                    title = stringResource(R.string.always_unique),
                    description = stringResource(R.string.how_desc_two),
                    imageRes = R.drawable.howtwo,
                )

                TutorialStepCardHorizontal(
                    modifier = Modifier.padding(top = AppTheme.paddings.default),
                    stepNumber = 3,
                    title = stringResource(R.string.use_notes),
                    description = stringResource(R.string.how_desc_three),
                    imageRes = R.drawable.howthree,
                )

                ButtonDefault(
                    modifier = Modifier.padding(
                        top = AppTheme.paddings.xxl,
                        bottom = AppTheme.paddings.large,
                    ),
                    text = stringResource(R.string.got_it_play),
                    onClick = { onEvent(HowToPlayUIEvent.BackClicked) },
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(R.string.useful_tips),
                    style = AppTheme.typography.h3,
                    color = AppTheme.colors.text,
                )

                TipCard(
                    modifier = Modifier.padding(top = AppTheme.paddings.large),
                    icon = Icons.Filled.Lightbulb,
                    title = stringResource(R.string.tip_start_simple),
                    description = stringResource(R.string.tip_start_simple_desc),
                )

                TipCard(
                    modifier = Modifier.padding(top = AppTheme.paddings.default),
                    icon = Icons.Filled.AutoFixHigh,
                    title = stringResource(R.string.tip_elimination),
                    description = stringResource(R.string.tip_elimination_desc),
                )

                TipCard(
                    modifier = Modifier.padding(top = AppTheme.paddings.default),
                    icon = Icons.Filled.Speed,
                    title = stringResource(R.string.tip_no_guessing),
                    description = stringResource(R.string.tip_no_guessing_desc),
                )

                TipCard(
                    modifier = Modifier.padding(top = AppTheme.paddings.default),
                    icon = Icons.Filled.Timer,
                    title = stringResource(R.string.tip_speed),
                    description = stringResource(R.string.tip_speed_desc),
                )
            }
        }
    }
}
