package ru.shprot.sudokumobdevkz.feature.splash.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.shprot.sudokumobdevkz.core.base.data.StatisticSync
import ru.shprot.sudokumobdevkz.core.base.presentation.viewmodel.BaseViewModel
import ru.shprot.sudokumobdevkz.core.uicommon.sudokuanim.GridPoint
import ru.shprot.sudokumobdevkz.core.uicommon.sudokuanim.SolvedPuzzleData.INITIAL_FILLED
import ru.shprot.sudokumobdevkz.feature.splash.presentation.contract.SplashUIEffect
import ru.shprot.sudokumobdevkz.feature.splash.presentation.contract.SplashUIEvent
import ru.shprot.sudokumobdevkz.feature.splash.presentation.contract.SplashUIState
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val statisticSync: StatisticSync,
) : BaseViewModel<SplashUIEvent, SplashUIState, SplashUIEffect>(SplashUIState()) {

    init {
        statisticSync.ensureStarted()
        startAnimation()
    }

    override fun handleUIEvent(event: SplashUIEvent) = Unit

    private fun startAnimation() {
        viewModelScope.launch {
            val emptyCells = (0 until 9).flatMap { row ->
                (0 until 9).map { col -> GridPoint(row, col) }
            }.filter { it !in INITIAL_FILLED }.shuffled()

            delay(400)

            for (index in emptyCells.indices) {
                setState(currentState.copy(
                    visibleCells = INITIAL_FILLED + emptyCells.take(index + 1).toSet(),
                ))
                delay(25)
            }

            delay(300)

            setEffect(SplashUIEffect.NavigateToMenu)
        }
    }
}
