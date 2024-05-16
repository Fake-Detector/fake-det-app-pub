package com.zhulin.fakedet.ui.screens.post.states

sealed class ScreenEvent {
    data object BackToMenu : ScreenEvent()
    data object Refresh : ScreenEvent()
    data object Initialize : ScreenEvent()
    data class SetItem(val value: String) : ScreenEvent()
}