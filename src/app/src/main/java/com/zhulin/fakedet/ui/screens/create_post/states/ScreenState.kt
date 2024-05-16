package com.zhulin.fakedet.ui.screens.create_post.states

import com.zhulin.fakedet.business.models.ContentInfo

data class ScreenState(
    val text: String = "",
    val contents: List<ContentInfo> = emptyList(),
    val hintVisible: Boolean = true,
)