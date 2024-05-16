package com.zhulin.fakedet.data.gateways

import android.util.Log
import com.google.protobuf.StringValue
import com.zhulin.fakedet.business.gateways.PostBridgeGateway
import com.zhulin.fakedet.business.models.ArticlesInfo
import com.zhulin.fakedet.business.models.CheckNewsInfo
import com.zhulin.fakedet.business.models.CreatePostInfo
import com.zhulin.fakedet.business.models.GetPostInfo
import com.zhulin.fakedet.business.models.HistoryInfo
import com.zhulin.fakedet.business.models.SendPostInfo
import com.zhulin.fakedet.business.models.ShortNewsInfo
import com.zhulin.fakedet.data.grpc.clients.PostBridgeGrpcClient
import com.zhulin.fakedet.data.helpers.PostHelpers.getFullInfo
import com.zhulin.fakedet.data.helpers.PostHelpers.getInfo
import com.zhulin.fakedet.data.stores.UserStore
import fake.detection.post.bridge.api.Bridge
import fake.detection.post.bridge.api.Bridge.CheckNewsRequest
import fake.detection.post.bridge.api.Bridge.CreatePostRequest
import fake.detection.post.bridge.api.Bridge.GetAllNewsRequest
import fake.detection.post.bridge.api.Bridge.GetNewsRequest
import fake.detection.post.bridge.api.Bridge.GetPostHistoryRequest
import fake.detection.post.bridge.api.Bridge.GetPostRequest
import fake.detection.post.bridge.api.Bridge.ProcessPostRequest
import fake.detection.post.bridge.contracts.PostOuterClass
import io.grpc.Metadata
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class PostBridgeGatewayImpl(
    private val client: PostBridgeGrpcClient,
    private val userStore: UserStore
) : PostBridgeGateway {
    override suspend fun createPost(
        authorId: String,
        dataSource: PostOuterClass.DataSource,
        externalId: String
    ): CreatePostInfo {
        try {
            val request = CreatePostRequest
                .newBuilder()
                .apply {
                    this.authorId = authorId
                    this.source = dataSource
                    this.externalId = StringValue.of(externalId)
                }
                .build()

            val token = userStore.getToken.first()

            val metaDataKey = Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER)
            val metadata = Metadata()
            metadata.put(metaDataKey, "Bearer $token")

            val response = client.serviceStub.createPost(request, metadata)

            return CreatePostInfo(postId = response.postId)
        } catch (e: Exception) {
            Log.e("FAKEDET-POST", e.message ?: "SOME EXCEPTION")
            throw e
        }
    }

    override suspend fun sendPostItem(chunks: Flow<Bridge.SendPostItemRequest>): SendPostInfo {
        try {
            val token = userStore.getToken.first()

            val metaDataKey = Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER)
            val metadata = Metadata()
            metadata.put(metaDataKey, "Bearer $token")

            val response = client.serviceStub.sendPostItem(chunks, metadata)

            return SendPostInfo(response.result)
        } catch (e: Exception) {
            Log.e("FAKEDET-POST", e.message ?: "SOME EXCEPTION")
            throw e
        }
    }

    override suspend fun processPost(postId: Long) {
        try {
            val request = ProcessPostRequest
                .newBuilder()
                .apply { this.postId = postId; }
                .build()

            val token = userStore.getToken.first()

            val metaDataKey = Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER)
            val metadata = Metadata()
            metadata.put(metaDataKey, "Bearer $token")

            client.serviceStub.processPost(request, metadata)
        } catch (e: Exception) {
            Log.e("FAKEDET-POST", e.message ?: "SOME EXCEPTION")
            throw e
        }
    }

    override suspend fun getPost(postId: Long): GetPostInfo {
        try {
            val request = GetPostRequest
                .newBuilder()
                .apply {
                    this.postId = postId
                }
                .build()

            val token = userStore.getToken.first()

            val metaDataKey = Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER)
            val metadata = Metadata()
            metadata.put(metaDataKey, "Bearer $token")

            val response = client.serviceStub.getPost(request, metadata)

            return GetPostInfo(response.post.getFullInfo())
        } catch (e: Exception) {
            Log.e("FAKEDET-POST", e.message ?: "SOME EXCEPTION")
            throw e
        }
    }

    override suspend fun getPostHistory(
        authorId: String,
        dataSource: PostOuterClass.DataSource
    ): HistoryInfo {
        try {
            val request = GetPostHistoryRequest
                .newBuilder()
                .apply {
                    this.authorId = authorId
                    this.source = dataSource
                }
                .build()

            val token = userStore.getToken.first()

            val metaDataKey = Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER)
            val metadata = Metadata()
            metadata.put(metaDataKey, "Bearer $token")

            val response = client.serviceStub.getPostHistory(request, metadata)

            return HistoryInfo(response.postsList.map { it.getInfo() })
        } catch (e: Exception) {
            Log.e("FAKEDET-POST", e.message ?: "SOME EXCEPTION")
            throw e
        }
    }

    override suspend fun checkNews(site: Bridge.Site, url: String): CheckNewsInfo {
        try {
            val request = CheckNewsRequest
                .newBuilder()
                .apply {
                    this.site = site
                    this.url = url
                }
                .build()

            val token = userStore.getToken.first()

            val metaDataKey = Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER)
            val metadata = Metadata()
            metadata.put(metaDataKey, "Bearer $token")

            val response = client.serviceStub.checkNews(request, metadata)

            return CheckNewsInfo(response.isSuccess, response.postId)
        } catch (e: Exception) {
            Log.e("FAKEDET-POST", e.message ?: "SOME EXCEPTION")
            throw e
        }
    }

    override suspend fun getAllNews(site: Bridge.Site, page: Long): ArticlesInfo {
        try {
            val request = GetAllNewsRequest
                .newBuilder()
                .apply {
                    this.site = site
                    this.page = page
                }
                .build()

            val token = userStore.getToken.first()

            val metaDataKey = Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER)
            val metadata = Metadata()
            metadata.put(metaDataKey, "Bearer $token")

            val response = client.serviceStub.getAllNews(request, metadata)

            return ArticlesInfo(response.newsList.map { ShortNewsInfo(it.url, it.content) })
        } catch (e: Exception) {
            Log.e("FAKEDET-POST", e.message ?: "SOME EXCEPTION")
            throw e
        }
    }

    override suspend fun getNewsHistory(site: Bridge.Site): HistoryInfo {
        try {
            val request = GetNewsRequest
                .newBuilder()
                .apply {
                    this.site = site
                    this.source = PostOuterClass.DataSource.News
                }
                .build()

            val token = userStore.getToken.first()

            val metaDataKey = Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER)
            val metadata = Metadata()
            metadata.put(metaDataKey, "Bearer $token")

            val response = client.serviceStub.getNews(request, metadata)

            return HistoryInfo(response.postsList.map { it.getInfo() })
        } catch (e: Exception) {
            Log.e("FAKEDET-POST", e.message ?: "SOME EXCEPTION")
            throw e
        }    }
}