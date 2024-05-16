package com.zhulin.fakedet.data.grpc.clients

import fake.detection.post.bridge.api.PostBridgeServiceGrpcKt
import io.grpc.ManagedChannel
import java.io.Closeable

class PostBridgeGrpcClient(private val channel: ManagedChannel) : Closeable {
    val serviceStub = PostBridgeServiceGrpcKt.PostBridgeServiceCoroutineStub(channel)

    override fun close() {
        if (!channel.isShutdown) {
            channel.shutdown()
        }
    }
}