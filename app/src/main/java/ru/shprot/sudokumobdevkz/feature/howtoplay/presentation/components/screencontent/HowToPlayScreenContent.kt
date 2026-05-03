package ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.components.screencontent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.components.TipCard
import ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.components.TutorialStepCard

@Composable
fun HowToPlayScreenContent(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
) {
    Scaffold(containerColor = AppTheme.colors.background) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = AppTheme.paddings.small,
                        vertical = AppTheme.paddings.medium,
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.go_back),
                        tint = AppTheme.colors.text,
                    )
                }

                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.how_to_play),
                    style = AppTheme.typography.h3,
                    color = AppTheme.colors.text,
                    textAlign = TextAlign.Center,
                )

                IconButton(onClick = { }, enabled = false) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = AppTheme.colors.background,
                        modifier = Modifier.size(AppTheme.sizes.iconMedium),
                    )
                }
            }

            Column(modifier = Modifier.padding(horizontal = AppTheme.paddings.large)) {

                Text(
                    modifier = Modifier.padding(top = AppTheme.paddings.large),
                    text = stringResource(R.string.learn_in_3_steps),
                    style = AppTheme.typography.h2,
                    color = AppTheme.colors.text,
                )

                Text(
                    modifier = Modifier.padding(top = AppTheme.paddings.small),
                    text = stringResource(R.string.simple_rules),
                    style = AppTheme.typography.body3,
                    color = AppTheme.colors.textSecondary,
                )

                TutorialStepCard(
                    modifier = Modifier.padding(top = AppTheme.paddings.xxl),
                    stepNumber = 1,
                    title = stringResource(R.string.no_repetitions),
                    description = stringResource(R.string.how_desc_one),
                    imageRes = R.drawable.howone,
                )

                TutorialStepCard(
                    modifier = Modifier.padding(top = AppTheme.paddings.large),
                    stepNumber = 2,
                    title = stringResource(R.string.always_unique),
                    description = stringResource(R.string.how_desc_two),
                    imageRes = R.drawable.howtwo,
                )

                TutorialStepCard(
                    modifier = Modifier.padding(top = AppTheme.paddings.large),
                    stepNumber = 3,
                    title = stringResource(R.string.use_notes),
                    description = stringResource(R.string.how_desc_three),
                    imageRes = R.drawable.howthree,
                )

                Text(
                    modifier = Modifier.padding(top = AppTheme.paddings.xxxl),
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

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = AppTheme.paddings.xxxl,
                            bottom = AppTheme.paddings.xxxl,
                        ),
                    onClick = onNavigateBack,
                    shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.primary),
                ) {
                    Text(
                        text = stringResource(R.string.got_it_play),
                        style = AppTheme.typography.button,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.colors.textOnPrimary,
                    )
                }
            }
        }
    }
}
