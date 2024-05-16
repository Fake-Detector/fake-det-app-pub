package com.zhulin.fakedet.ui.screens.telegram.states

sealed class ScreenEvent
{
    data object ShowBot: ScreenEvent()
    data object ShowNews: ScreenEvent()
}
