package ru.shprot.sudokumobdevkz.feature.splash.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.shprot.sudokumobdevkz.core.base.data.repository.SudokuRepository
import ru.shprot.sudokumobdevkz.core.base.presentation.viewmodel.BaseViewModel
import ru.shprot.sudokumobdevkz.feature.splash.domain.model.GridPoint
import ru.shprot.sudokumobdevkz.feature.splash.presentation.contract.SplashUIEffect
import ru.shprot.sudokumobdevkz.feature.splash.presentation.contract.SplashUIEvent
import ru.shprot.sudokumobdevkz.feature.splash.presentation.contract.SplashUIState
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: SudokuRepository,
) : BaseViewModel<SplashUIEvent, SplashUIState, SplashUIEffect>(SplashUIState()) {

    init {
        syncStatistic()
        startAnimation()
    }

    override fun handleUIEvent(event: SplashUIEvent) = Unit

    private fun startAnimation() {
        val emptyCells = (0 until 9).flatMap { row ->
            (0 until 9).map { col -> GridPoint(row, col) }
        }.filter { it !in INITIAL_FILLED }.shuffled()

        setState(currentState.copy(initialCells = INITIAL_FILLED))

        viewModelScope.launch {
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

    private fun syncStatistic() {
        viewModelScope.launch { repository.syncStatisticsFromFirebase() }
    }
}

private val INITIAL_FILLED = setOf(
    GridPoint(0, 0), GridPoint(0, 1), GridPoint(0, 4), GridPoint(0, 8),
    GridPoint(1, 2), GridPoint(1, 5), GridPoint(1, 7),
    GridPoint(2, 0), GridPoint(2, 3), GridPoint(2, 6),
    GridPoint(3, 1), GridPoint(3, 4), GridPoint(3, 8),
    GridPoint(4, 0), GridPoint(4, 3), GridPoint(4, 5), GridPoint(4, 8),
    GridPoint(5, 0), GridPoint(5, 4), GridPoint(5, 7),
    GridPoint(6, 2), GridPoint(6, 5), GridPoint(6, 8),
    GridPoint(7, 1), GridPoint(7, 3), GridPoint(7, 6),
    GridPoint(8, 0), GridPoint(8, 4), GridPoint(8, 7), GridPoint(8, 8),
)
