package com.zhulin.fakedet.ui.screens.main.states

sealed class ScreenEvent {
    data object ChangeSettingsExpended : ScreenEvent()
    data object ChangeLanguage : ScreenEvent()
    data object SignOut : ScreenEvent()
    data object Initialize : ScreenEvent()
    data object CreatePost : ScreenEvent()
    data object ShowNews : ScreenEvent()
    data object ShowTelegram : ScreenEvent()
    data object GetPostHistory : ScreenEvent()
    data class GetPost(val value: Long) : ScreenEvent()
}