package com.zhulin.fakedet.ui.screens.news.states

import fake.detection.post.bridge.api.Bridge.Site

sealed class ScreenEvent {
    data object ShowBot : ScreenEvent()
    data object ShowTelegram : ScreenEvent()
    data object GetNextArticles : ScreenEvent()
    data object GetPrevArticles : ScreenEvent()
    data object Refresh : ScreenEvent()
    data object Initialize : ScreenEvent()
    data class GetPost(val value: Long) : ScreenEvent()
    data class ChangeSite(val value: Site) : ScreenEvent()
    data class CheckNews(val value: String) : ScreenEvent()
}