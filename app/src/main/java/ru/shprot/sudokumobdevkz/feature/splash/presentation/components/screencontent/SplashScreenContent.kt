package ru.shprot.sudokumobdevkz.feature.splash.presentation.components.screencontent
import ru.shprot.sudokumobdevkz.feature.splash.presentation.components.*

import androidx.compose.foundation.background
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.delay
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.splash.presentation.contract.SplashUIEvent
import ru.shprot.sudokumobdevkz.feature.splash.presentation.contract.SplashUIState

@Composable
fun SplashScreenContent(
    uiState: SplashUIState,
    onEvent: (SplashUIEvent) -> Unit,
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = AppTheme.paddings.large),
        contentAlignment = Alignment.Center,
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(
                text = stringResource(R.string.app_name),
                style = AppTheme.typography.h1,
                color = AppTheme.colors.text,
            )

            Text(
                modifier = Modifier
                    .padding(top = AppTheme.paddings.small),
                text = stringResource(R.string.version_format, ru.shprot.sudokumobdevkz.BuildConfig.VERSION_NAME),
                style = AppTheme.typography.body3,
                color = AppTheme.colors.textSecondary,
            )

            SolvedGridAnimation(
                modifier = Modifier
                    .padding(top = AppTheme.paddings.xxl)
                    .fillMaxWidth(0.7f)
                    .aspectRatio(1f),
                visibleCells = uiState.visibleCells,
                initialFilled = uiState.initialCells,
                solvedGrid = uiState.solvedGrid,
            )
        }
    }
}