package com.zhulin.fakedet.ui.screens.news

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.zhulin.fakedet.R
import com.zhulin.fakedet.business.usecases.UseCaseHandler
import com.zhulin.fakedet.ui.screens.AbstractViewModel
import com.zhulin.fakedet.ui.screens.create_post.CreatePostViewModel
import com.zhulin.fakedet.ui.screens.news.states.ScreenEvent
import com.zhulin.fakedet.ui.screens.news.states.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val useCaseHandler: UseCaseHandler,
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
            is ScreenEvent.GetPost -> viewModelScope.launch { _eventFlow.emit(UiEvent.GetPost(event.value)) }
            is ScreenEvent.ShowBot -> viewModelScope.launch { _eventFlow.emit(UiEvent.ShowBot) }
            is ScreenEvent.GetNextArticles -> viewModelScope.launch(Dispatchers.IO) {
                refresh(_screenState.value.page + 1)
            }

            is ScreenEvent.GetPrevArticles -> viewModelScope.launch(Dispatchers.IO) {
                refresh(_screenState.value.page - 1)
            }

            is ScreenEvent.ChangeSite -> viewModelScope.launch {
                if (event.value == screenState.value.site) return@launch
                _screenState.value = screenState.value.copy(
                    site = event.value,
                    page = 1,
                    isInitialized = false
                )
            }

            is ScreenEvent.Refresh -> viewModelScope.launch(Dispatchers.IO) {
                refresh(_screenState.value.page)
            }

            is ScreenEvent.CheckNews -> viewModelScope.launch(Dispatchers.IO) {
                checkNews(event.value)
            }

            ScreenEvent.Initialize -> viewModelScope.launch(Dispatchers.IO) {
                refresh(_screenState.value.page)

                _screenState.value = screenState.value.copy(
                    isInitialized = true,
                )
            }

            is ScreenEvent.ShowTelegram -> viewModelScope.launch {
                _eventFlow.emit(UiEvent.ShowTelegram)
            }
        }
    }

    private suspend fun refresh(page: Long) {
        getArticles(page)
        getPostHistory()
    }

    private suspend fun getPostHistory() {
        try {
            val newsPosts = useCaseHandler.postHandler.getNewsHistory(
                _screenState.value.site
            )

            _screenState.value = screenState.value.copy(
                newsPosts = newsPosts,
            )
        } catch (ex: Exception) {
            _eventFlow.emit(
                UiEvent.ShowSnackbar(message = getStringResource(R.string.error_happend))
            )
        }
    }

    private suspend fun checkNews(url: String) {
        try {
            val checkInfo = useCaseHandler.postHandler.checkNews(
                _screenState.value.site,
                url
            )

            if (checkInfo.isSuccess)
                _eventFlow.emit(
                    UiEvent.ShowSnackbar(message = getStringResource(R.string.error_happend))
                )

            _eventFlow.emit(
                UiEvent.GetPost(checkInfo.postId)
            )

        } catch (ex: Exception) {
            _eventFlow.emit(
                UiEvent.ShowSnackbar(message = getStringResource(R.string.error_happend))
            )
        }
    }

    private suspend fun getArticles(page: Long) {
        try {
            if (page < 1) return

            val newsArticles = useCaseHandler.postHandler.getAllNews(
                screenState.value.site,
                page
            )

            if (newsArticles.articles.isEmpty() && screenState.value.isInitialized) return

            _screenState.value = screenState.value.copy(
                newsArticles = newsArticles,
                page = page,
                isPrevPagePossible = page != 1L,
                isNextPagePossible = newsArticles.articles.count() >= 20
            )
        } catch (ex: Exception) {
            _eventFlow.emit(
                UiEvent.ShowSnackbar(message = getStringResource(R.string.error_happend))
            )
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data object ShowBot : UiEvent()
        data object ShowTelegram : UiEvent()
        data object BackToAuth : UiEvent()
        data class GetPost(val postId: Long) : UiEvent()
    }
}
