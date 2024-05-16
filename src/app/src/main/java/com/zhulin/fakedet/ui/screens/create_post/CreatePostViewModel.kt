package com.zhulin.fakedet.ui.screens.create_post

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.zhulin.fakedet.R
import com.zhulin.fakedet.business.helpers.TypeHelpers.toItemType
import com.zhulin.fakedet.business.usecases.UseCaseHandler
import com.zhulin.fakedet.ui.screens.AbstractViewModel
import com.zhulin.fakedet.ui.screens.create_post.states.ScreenEvent
import com.zhulin.fakedet.ui.screens.create_post.states.ScreenState
import com.zhulin.fakedet.ui.screens.post.PostViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fake.detection.post.bridge.contracts.PostOuterClass
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val useCaseHandler: UseCaseHandler
) : AbstractViewModel(useCaseHandler) {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _screenState = mutableStateOf(ScreenState())
    val screenState: State<ScreenState> = _screenState

    init {
        viewModelScope.launch {
            if (!auth()) _eventFlow.emit(UiEvent.BackToAuth)
        }
    }

    operator fun invoke(event: ScreenEvent) {
        when (event) {
            is ScreenEvent.BackToMenu -> viewModelScope.launch { _eventFlow.emit(UiEvent.BackToMenu) }
            is ScreenEvent.OnTextChange -> _screenState.value =
                screenState.value.copy(text = event.value, hintVisible = event.value.isBlank())

            is ScreenEvent.OnTextFocusChange -> _screenState.value =
                screenState.value.copy(hintVisible = !event.value.isFocused && _screenState.value.text.isBlank())

            is ScreenEvent.Send -> viewModelScope.launch {
                try {
                    val userId = useCaseHandler.userHandler.getLogin()

                    if (userId.isBlank()) {
                        _eventFlow.emit(UiEvent.ShowSnackbar("Error userId!"))
                        return@launch
                    }

                    val response = useCaseHandler.postHandler.createPost(
                        userId,
                        PostOuterClass.DataSource.Author
                    )

                    if (_screenState.value.text.isNotBlank()) {
                        useCaseHandler.postHandler.sendPostText(
                            response.postId,
                            _screenState.value.text
                        )
                    }

                    _screenState.value.contents.forEach {
                        val bytes = useCaseHandler.contentHandler.getBytes(it.uri)
                        if (bytes != null) {
                            useCaseHandler.postHandler.sendPostMedia(
                                response.postId,
                                bytes,
                                it.type.toItemType(),
                                it.extension
                            )
                        }
                    }

                    useCaseHandler.postHandler.processPost(response.postId)

                    _eventFlow.emit(UiEvent.BackToMenu)
                } catch (ex: Exception) {
                    _eventFlow.emit(UiEvent.ShowSnackbar(message = getStringResource(R.string.error_happend)))
                }
            }

            is ScreenEvent.AddUri -> viewModelScope.launch {
                try {
                    val content = useCaseHandler.contentHandler.getContent(event.value)

                    if (content == null) {
                        _eventFlow.emit(UiEvent.ShowSnackbar(message = getStringResource(R.string.error_incorrect_file_type)))
                        return@launch
                    }

                    val contentInfos = _screenState.value.contents.toMutableList()
                    contentInfos.add(content)

                    _screenState.value = screenState.value.copy(
                        contents = contentInfos.distinctBy { it.uri }
                    )

                } catch (ex: Exception) {
                    _eventFlow.emit(UiEvent.ShowSnackbar(message = getStringResource(R.string.error_happend)))
                }
            }

            is ScreenEvent.RemoveUri -> viewModelScope.launch {
                _screenState.value = screenState.value.copy(
                    contents = screenState.value.contents.filter { it.uri != event.value }
                )
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data object BackToMenu : UiEvent()
        data object BackToAuth : UiEvent()
    }
}