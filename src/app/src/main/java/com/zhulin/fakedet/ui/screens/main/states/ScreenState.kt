package com.zhulin.fakedet.ui.screens.main.states

import com.zhulin.fakedet.business.models.HistoryInfo

data class ScreenState(
    val settingsExpended: Boolean = false,
    val userName: String = "",
    val userPosts: HistoryInfo = HistoryInfo(emptyList()),
    val isInitialized: Boolean = false
)