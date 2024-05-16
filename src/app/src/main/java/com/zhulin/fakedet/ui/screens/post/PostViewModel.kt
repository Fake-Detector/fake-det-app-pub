package com.zhulin.fakedet.ui.screens.post

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.zhulin.fakedet.business.models.ItemInfo
import com.zhulin.fakedet.business.usecases.UseCaseHandler
import com.zhulin.fakedet.ui.navigation.models.Screen
import com.zhulin.fakedet.ui.screens.AbstractViewModel
import com.zhulin.fakedet.ui.screens.create_post.CreatePostViewModel
import com.zhulin.fakedet.ui.screens.post.states.ScreenEvent
import com.zhulin.fakedet.ui.screens.post.states.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import fake.detection.post.bridge.contracts.PostOuterClass.DataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val useCaseHandler: UseCaseHandler,
    savedStateHandle: SavedStateHandle
) : AbstractViewModel(useCaseHandler) {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _screenState = mutableStateOf(ScreenState())
    val screenState: State<ScreenState> = _screenState

    init {
        viewModelScope.launch {
            if (!auth()) _eventFlow.emit(UiEvent.BackToAuth)

            savedStateHandle.get<Long>("postId")?.let {
                _screenState.value = screenState.value.copy(postId = it)
            }

            savedStateHandle.get<String>("dataSource")?.let {
                _screenState.value = screenState.value.copy(dataSource = DataSource.valueOf(it))
            }
        }

    }

    operator fun invoke(event: ScreenEvent) {
        when (event) {
            is ScreenEvent.BackToMenu -> viewModelScope.launch {
                backToMenu()
            }

            is ScreenEvent.SetItem -> _screenState.value = screenState.value.copy(
                itemId = event.value
            )

            is ScreenEvent.Refresh -> viewModelScope.launch(Dispatchers.IO) {
                refresh()
            }

            is ScreenEvent.Initialize -> viewModelScope.launch(Dispatchers.IO) {
                refresh()
            }
        }
    }

    fun getItem(): ItemInfo? {
        if (_screenState.value.itemId == null) return null

        return _screenState.value.post.items.firstOrNull { it.id == _screenState.value.itemId }
    }

    private suspend fun refresh() {
        try {
            val result = useCaseHandler.postHandler.getPost(_screenState.value.postId)

            if (result.post.id != _screenState.value.postId)
                backToMenu()

            _screenState.value = screenState.value.copy(
                post = result.post,
                isInitialized = true,
                itemId = result.post.items.firstOrNull()?.id
            )
        } catch (e: Exception) {
            backToMenu()
        }
    }

    private suspend fun backToMenu() = when (screenState.value.dataSource) {
        DataSource.Author -> _eventFlow.emit(UiEvent.BackToMenu(Screen.MainScreen.route))
        DataSource.TgChannel -> _eventFlow.emit(UiEvent.BackToMenu(Screen.MainScreen.route))
        DataSource.News -> _eventFlow.emit(UiEvent.BackToMenu(Screen.NewsScreen.route))
        else -> _eventFlow.emit(UiEvent.BackToMenu(Screen.MainScreen.route))
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data class BackToMenu(val route: String) : UiEvent()
        data object BackToAuth : UiEvent()
    }
}