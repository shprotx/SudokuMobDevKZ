package ru.shprot.sudokumobdevkz.core.base.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEffect
import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEvent
import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<Event : UIEvent, State : UIState, Effect : UIEffect>(
    initialState: State,
) : ViewModel() {

    val currentState: State
        get() = uiState.value

    private val _uiState: MutableStateFlow<State> = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    private val event: MutableSharedFlow<Event> = MutableSharedFlow()

    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    protected val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError(throwable)
    }

    protected open fun onError(throwable: Throwable) = Unit

    init {
        subscribeEvents()
    }

    private fun subscribeEvents() {
        viewModelScope.launch {
            event.collect(::handleUIEvent)
        }
    }

    fun setEvent(newEvent: Event) {
        viewModelScope.launch {
            event.emit(newEvent)
        }
    }

    protected fun setState(newState: State) {
        _uiState.value = newState
    }

    protected fun updateState(update: State.() -> State) {
        _uiState.value = currentState.update()
    }

    protected fun setEffect(effect: Effect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    protected abstract fun handleUIEvent(event: Event)
}
