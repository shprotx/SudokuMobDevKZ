package ru.shprot.sudokumobdevkz.feature.menu.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.shprot.sudokumobdevkz.core.base.data.repository.SudokuRepository
import ru.shprot.sudokumobdevkz.core.base.presentation.viewmodel.BaseViewModel
import ru.shprot.sudokumobdevkz.feature.menu.presentation.contract.MenuUIEffect
import ru.shprot.sudokumobdevkz.feature.menu.presentation.contract.MenuUIEvent
import ru.shprot.sudokumobdevkz.feature.menu.presentation.contract.MenuUIState
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val repository: SudokuRepository,
) : BaseViewModel<MenuUIEvent, MenuUIState, MenuUIEffect>(MenuUIState()) {

    init {
        checkSavedGame()
    }

    override fun handleUIEvent(event: MenuUIEvent) =
        when (event) {
            MenuUIEvent.ContinueGameClicked ->
                setEffect(MenuUIEffect.NavigateToContinueGame)

            MenuUIEvent.NavigateToStatistic ->
                setEffect(MenuUIEffect.NavigateToStatistic)

            MenuUIEvent.NavigateToSettings ->
                setEffect(MenuUIEffect.NavigateToSettings)

            MenuUIEvent.NavigateToHowToPlay ->
                setEffect(MenuUIEffect.NavigateToHowToPlay)

            MenuUIEvent.ScreenResumed ->
                checkSavedGame()

            is MenuUIEvent.NewGameClicked ->
                setEffect(MenuUIEffect.NavigateToGame(event.difficultyOrdinal))

            is MenuUIEvent.DifficultySelected ->
                updateState { copy(selectedDifficulty = event.difficultyOrdinal) }
        }

    private fun checkSavedGame() {
        viewModelScope.launch(exceptionHandler) {
            val hasSaved = repository.hasSavedGame()
            updateState { copy(hasSavedGame = hasSaved) }
        }
    }
}
