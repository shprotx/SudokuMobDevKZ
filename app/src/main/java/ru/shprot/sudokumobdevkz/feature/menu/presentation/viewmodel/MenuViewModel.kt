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

    override fun handleUIEvent(event: MenuUIEvent) {
        when (event) {
            is MenuUIEvent.NewGameClicked ->
                setEffect(MenuUIEffect.NavigateToGame(event.difficulty))
            is MenuUIEvent.ContinueGameClicked ->
                setEffect(MenuUIEffect.NavigateToContinueGame)
            is MenuUIEvent.DifficultySelected ->
                updateState { copy(selectedDifficulty = event.difficulty) }
            is MenuUIEvent.NavigateToStatistic ->
                setEffect(MenuUIEffect.NavigateToStatistic)
            is MenuUIEvent.NavigateToSettings ->
                setEffect(MenuUIEffect.NavigateToSettings)
            is MenuUIEvent.NavigateToHowToPlay ->
                setEffect(MenuUIEffect.NavigateToHowToPlay)
            is MenuUIEvent.ScreenResumed -> checkSavedGame()
        }
    }

    private fun checkSavedGame() {
        viewModelScope.launch(exceptionHandler) {
            val hasSaved = repository.hasSavedGame()
            updateState { copy(hasSavedGame = hasSaved) }
        }
    }
}
