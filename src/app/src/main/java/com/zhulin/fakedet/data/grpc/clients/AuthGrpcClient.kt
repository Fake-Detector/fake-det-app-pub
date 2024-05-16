package com.zhulin.fakedet.data.grpc.clients

import fake.detection.auth.UserServiceGrpcKt
import io.grpc.ManagedChannel
import java.io.Closeable

class AuthGrpcClient(private val channel: ManagedChannel) : Closeable {
    val serviceStub = UserServiceGrpcKt.UserServiceCoroutineStub(channel)

    override fun close() {
        if (!channel.isShutdown) {
            channel.shutdown()
        }
    }
}