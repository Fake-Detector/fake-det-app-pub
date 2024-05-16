package com.zhulin.fakedet.ui.screens.news.states

import com.zhulin.fakedet.business.models.ArticlesInfo
import com.zhulin.fakedet.business.models.HistoryInfo
import fake.detection.post.bridge.api.Bridge.Site

data class ScreenState(
    val newsPosts: HistoryInfo = HistoryInfo(emptyList()),
    val newsArticles: ArticlesInfo = ArticlesInfo(emptyList()),
    val site: Site = Site.LentaRu,
    val page: Long = 1,
    val isNextPagePossible: Boolean = false,
    val isPrevPagePossible: Boolean = false,
    val isInitialized: Boolean = false,
)