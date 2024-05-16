package com.zhulin.fakedet.business.helpers

import com.google.protobuf.ByteString
import com.google.protobuf.StringValue
import fake.detection.post.bridge.api.Bridge.ItemChunk
import fake.detection.post.bridge.api.Bridge.MetaData
import fake.detection.post.bridge.api.Bridge.SendPostItemRequest
import fake.detection.post.bridge.contracts.PostOuterClass.ItemType
import kotlinx.coroutines.flow.flow

object ChunkHelpers {
    fun createTextRequest(postId: Long, text: String) = createUploadRequest(
        postId = postId,
        inputBytes = text.toByteArray(),
        itemType = ItemType.Text
    )

    fun createMediaRequest(postId: Long, byteArray: ByteArray, itemType: ItemType, format: String) =
        createUploadRequest(
            postId = postId,
            inputBytes = byteArray,
            itemType = itemType,
            format = format
        )

    private fun createUploadRequest(
        postId: Long,
        inputBytes: ByteArray,
        itemType: ItemType,
        format: String = "",
        chunkSize: Int = 4096
    ) = flow {
        var position = 0

        while (position < inputBytes.size) {
            val end = minOf(inputBytes.size, position + chunkSize)
            val chunk = inputBytes.copyOfRange(position, end)
            position += chunkSize
            val itemChunk = ItemChunk.newBuilder()
                .apply {
                    this.postId = postId
                    this.metaData = MetaData.newBuilder()
                        .apply {
                            this.type = itemType
                            this.format = StringValue.of(format)
                        }
                        .build()
                    this.chunk = ByteString.copyFrom(chunk)
                }

            val request = SendPostItemRequest.newBuilder().setItem(itemChunk).build()
            emit(request)
        }
    }
}