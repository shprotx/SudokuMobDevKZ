package ru.shprot.sudokumobdevkz.feature.gameover.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import ru.shprot.sudokumobdevkz.core.base.presentation.viewmodel.BaseViewModel
import ru.shprot.sudokumobdevkz.feature.gameover.presentation.contract.GameOverUIEffect
import ru.shprot.sudokumobdevkz.feature.gameover.presentation.contract.GameOverUIEvent
import ru.shprot.sudokumobdevkz.feature.gameover.presentation.contract.GameOverUIState
import ru.shprot.sudokumobdevkz.feature.gameover.presentation.navigation.GameOverRoutes
import javax.inject.Inject

@HiltViewModel
class GameOverViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<GameOverUIEvent, GameOverUIState, GameOverUIEffect>(GameOverUIState()) {

    private val route = savedStateHandle.toRoute<GameOverRoutes.GameOverScreen>()
    private val difficulty: Difficulty = Difficulty.fromOrdinal(route.difficultyOrdinal)

    init {
        setState(
            GameOverUIState(
                isWin = route.isWin,
                time = route.time,
                errors = route.errors,
                difficulty = difficulty,
                isDailyChallenge = route.isDailyChallenge,
                newStreak = route.newStreak,
            )
        )
    }

    override fun handleUIEvent(event: GameOverUIEvent) =
        when (event) {
            GameOverUIEvent.PlayAgainClicked ->
                handlePlayAgainClicked()

            GameOverUIEvent.BackToMenuClicked ->
                setEffect(GameOverUIEffect.NavigateToMenu)
        }

    private fun handlePlayAgainClicked() {
        if (currentState.isDailyChallenge) {
            setEffect(GameOverUIEffect.NavigateToMenu)
        } else {
            setEffect(GameOverUIEffect.NavigateToNewGame(currentState.difficulty.ordinal))
        }
    }
}