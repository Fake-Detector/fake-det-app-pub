package com.zhulin.fakedet.ui.screens.create_post.states

import android.net.Uri
import androidx.compose.ui.focus.FocusState

sealed class ScreenEvent {
    data class OnTextChange(val value: String) : ScreenEvent()
    data class OnTextFocusChange(val value: FocusState) : ScreenEvent()
    data class AddUri(val value: Uri) : ScreenEvent()
    data class RemoveUri(val value: Uri) : ScreenEvent()

    data object BackToMenu : ScreenEvent()

    data object Send : ScreenEvent()
}