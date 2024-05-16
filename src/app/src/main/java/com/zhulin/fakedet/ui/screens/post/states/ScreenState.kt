package com.zhulin.fakedet.ui.screens.post.states

import com.zhulin.fakedet.business.models.PostFullInfo
import fake.detection.post.bridge.contracts.PostOuterClass.DataSource
import java.time.LocalDateTime

data class ScreenState(
    val postId: Long = 0,
    val dataSource: DataSource = DataSource.Author,
    val isInitialized: Boolean = false,
    val post: PostFullInfo = PostFullInfo(0, "", "", emptyList(), 0, LocalDateTime.now()),
    val itemId: String? = null,
)