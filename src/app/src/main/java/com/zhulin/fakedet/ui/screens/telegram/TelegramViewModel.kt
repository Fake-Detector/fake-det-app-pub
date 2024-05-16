package com.zhulin.fakedet.ui.screens.telegram

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.zhulin.fakedet.business.usecases.UseCaseHandler
import com.zhulin.fakedet.ui.screens.AbstractViewModel
import com.zhulin.fakedet.ui.screens.telegram.states.ScreenEvent
import com.zhulin.fakedet.ui.screens.telegram.states.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TelegramViewModel @Inject constructor(
    private val useCaseHandler: UseCaseHandler,
) : AbstractViewModel(useCaseHandler) {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _screenState = mutableStateOf(ScreenState())
    val screenState: State<ScreenState> = _screenState

    init {
        viewModelScope.launch {
            if (!auth()) _eventFlow.emit(UiEvent.BackToAuth)
            _screenState.value = screenState.value.copy(
                token = useCaseHandler.auth.getToken().token ?: ""
            )
        }
    }

    operator fun invoke(event: ScreenEvent) {
        when (event) {
            ScreenEvent.ShowBot -> viewModelScope.launch { _eventFlow.emit(UiEvent.ShowBot) }
            ScreenEvent.ShowNews -> viewModelScope.launch { _eventFlow.emit(UiEvent.ShowNews) }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data object BackToAuth : UiEvent()
        data object ShowBot : UiEvent()
        data object ShowNews : UiEvent()
    }
}