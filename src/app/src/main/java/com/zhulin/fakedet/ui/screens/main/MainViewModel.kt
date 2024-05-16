package com.zhulin.fakedet.ui.screens.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.zhulin.fakedet.R
import com.zhulin.fakedet.business.usecases.UseCaseHandler
import com.zhulin.fakedet.ui.screens.AbstractViewModel
import com.zhulin.fakedet.ui.screens.main.states.ScreenEvent
import com.zhulin.fakedet.ui.screens.main.states.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import fake.detection.post.bridge.contracts.PostOuterClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCaseHandler: UseCaseHandler
) : AbstractViewModel(useCaseHandler) {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _screenState = mutableStateOf(ScreenState())
    val screenState: State<ScreenState> = _screenState

    init {
        viewModelScope.launch {
            if (!auth()) _eventFlow.emit(UiEvent.BackToAuth)

            _screenState.value = screenState.value.copy(
                userName = useCaseHandler.userHandler.getName()
            )
        }
    }

    operator fun invoke(event: ScreenEvent) {
        when (event) {
            is ScreenEvent.ChangeSettingsExpended -> _screenState.value = screenState.value.copy(
                settingsExpended = !screenState.value.settingsExpended
            )

            is ScreenEvent.ChangeLanguage -> viewModelScope.launch {
                useCaseHandler.changeLanguage()

                _screenState.value = screenState.value.copy(
                    settingsExpended = !screenState.value.settingsExpended
                )
            }

            is ScreenEvent.SignOut -> viewModelScope.launch {
                useCaseHandler.userHandler.signOut()

                _screenState.value = screenState.value.copy(
                    settingsExpended = !screenState.value.settingsExpended
                )

                _eventFlow.emit(UiEvent.SignOut)
            }

            is ScreenEvent.CreatePost -> viewModelScope.launch {
                _eventFlow.emit(UiEvent.CreatePost)
            }

            is ScreenEvent.GetPostHistory -> viewModelScope.launch(Dispatchers.IO) {
                getPostHistory()
            }

            is ScreenEvent.GetPost -> viewModelScope.launch { _eventFlow.emit(UiEvent.GetPost(event.value)) }

            is ScreenEvent.ShowNews -> viewModelScope.launch {
                _eventFlow.emit(UiEvent.ShowNews)
            }

            is ScreenEvent.Initialize -> viewModelScope.launch {
                getPostHistory()
                _screenState.value = screenState.value.copy(
                    isInitialized = true
                )
            }

            is ScreenEvent.ShowTelegram -> viewModelScope.launch {
                _eventFlow.emit(UiEvent.ShowTelegram)
            }
        }
    }

    private suspend fun getPostHistory() {
        try {
            val userId = useCaseHandler.userHandler.getLogin()

            if (userId.isBlank()) {
                _eventFlow.emit(UiEvent.ShowSnackbar("Error userId!"))
                return
            }

            val userPosts = useCaseHandler.postHandler.getPostHistory(
                userId,
                PostOuterClass.DataSource.Author
            )

            _screenState.value = screenState.value.copy(
                userPosts = userPosts
            )
        } catch (ex: Exception) {
            _eventFlow.emit(
                UiEvent.ShowSnackbar(message = getStringResource(R.string.error_happend))
            )
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data object SignOut : UiEvent()
        data object CreatePost : UiEvent()
        data object ShowNews : UiEvent()
        data object ShowTelegram : UiEvent()
        data object BackToAuth : UiEvent()
        data class GetPost(val postId: Long) : UiEvent()
    }
}