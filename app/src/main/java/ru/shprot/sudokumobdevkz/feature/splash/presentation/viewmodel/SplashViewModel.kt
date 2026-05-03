package ru.shprot.sudokumobdevkz.feature.splash.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.shprot.sudokumobdevkz.core.base.data.repository.SudokuRepository
import ru.shprot.sudokumobdevkz.core.base.presentation.viewmodel.BaseViewModel
import ru.shprot.sudokumobdevkz.feature.splash.presentation.contract.SplashUIEffect
import ru.shprot.sudokumobdevkz.feature.splash.presentation.contract.SplashUIEvent
import ru.shprot.sudokumobdevkz.feature.splash.presentation.contract.SplashUIState
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: SudokuRepository,
) : BaseViewModel<SplashUIEvent, SplashUIState, SplashUIEffect>(SplashUIState()) {

    init {
        viewModelScope.launch { repository.syncStatisticsFromFirebase() }
    }

    override fun handleUIEvent(event: SplashUIEvent) =
        when (event) {
            SplashUIEvent.AnimationCompleted ->
                setEffect(SplashUIEffect.NavigateToMenu)
        }
}
