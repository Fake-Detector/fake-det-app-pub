package com.zhulin.fakedet.business.usecases

import com.zhulin.fakedet.business.gateways.PostBridgeGateway
import com.zhulin.fakedet.business.helpers.ChunkHelpers
import fake.detection.post.bridge.api.Bridge
import fake.detection.post.bridge.contracts.PostOuterClass.DataSource
import fake.detection.post.bridge.contracts.PostOuterClass.ItemType
import javax.inject.Inject

class PostHandler @Inject constructor(
    private val postBridgeGateway: PostBridgeGateway
) {
    suspend fun createPost(authorId: String, dataSource: DataSource) =
        postBridgeGateway.createPost(authorId, dataSource)

    suspend fun sendPostText(postId: Long, text: String) =
        postBridgeGateway.sendPostItem(ChunkHelpers.createTextRequest(postId, text))

    suspend fun sendPostMedia(postId: Long, bytes: ByteArray, type: ItemType, format: String) =
        postBridgeGateway.sendPostItem(ChunkHelpers.createMediaRequest(postId, bytes, type, format))

    suspend fun processPost(postId: Long) =
        postBridgeGateway.processPost(postId)

    suspend fun getPost(postId: Long) =
        postBridgeGateway.getPost(postId)

    suspend fun getPostHistory(authorId: String, dataSource: DataSource) =
        postBridgeGateway.getPostHistory(authorId, dataSource)

    suspend fun checkNews(site: Bridge.Site, url: String) = postBridgeGateway.checkNews(site, url)
    suspend fun getAllNews(site: Bridge.Site, page: Long) = postBridgeGateway.getAllNews(site, page)
    suspend fun getNewsHistory(site: Bridge.Site) = postBridgeGateway.getNewsHistory(site)
}