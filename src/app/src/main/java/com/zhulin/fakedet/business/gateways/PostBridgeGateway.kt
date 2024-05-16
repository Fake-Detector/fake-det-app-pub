package com.zhulin.fakedet.business.gateways

import com.zhulin.fakedet.business.models.ArticlesInfo
import com.zhulin.fakedet.business.models.CheckNewsInfo
import com.zhulin.fakedet.business.models.CreatePostInfo
import com.zhulin.fakedet.business.models.GetPostInfo
import com.zhulin.fakedet.business.models.HistoryInfo
import com.zhulin.fakedet.business.models.SendPostInfo
import fake.detection.post.bridge.api.Bridge.SendPostItemRequest
import fake.detection.post.bridge.api.Bridge.Site
import fake.detection.post.bridge.contracts.PostOuterClass.DataSource
import kotlinx.coroutines.flow.Flow

interface PostBridgeGateway {
    suspend fun createPost(
        authorId: String,
        dataSource: DataSource,
        externalId: String = ""
    ): CreatePostInfo

    suspend fun sendPostItem(chunks: Flow<SendPostItemRequest>): SendPostInfo
    suspend fun processPost(postId: Long)
    suspend fun getPost(postId: Long): GetPostInfo
    suspend fun getPostHistory(authorId: String, dataSource: DataSource): HistoryInfo
    suspend fun checkNews(site: Site, url: String): CheckNewsInfo
    suspend fun getAllNews(site: Site, page: Long): ArticlesInfo
    suspend fun getNewsHistory(site: Site): HistoryInfo
}