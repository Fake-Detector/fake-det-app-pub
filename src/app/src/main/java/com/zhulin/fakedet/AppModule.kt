package com.zhulin.fakedet

import android.app.Application
import android.net.Uri
import com.zhulin.fakedet.business.gateways.AuthGateway
import com.zhulin.fakedet.business.gateways.PostBridgeGateway
import com.zhulin.fakedet.business.repositories.StringResourceRepository
import com.zhulin.fakedet.business.usecases.ContentHandler
import com.zhulin.fakedet.data.gateways.AuthGatewayImpl
import com.zhulin.fakedet.data.gateways.PostBridgeGatewayImpl
import com.zhulin.fakedet.data.grpc.clients.AuthGrpcClient
import com.zhulin.fakedet.data.grpc.clients.PostBridgeGrpcClient
import com.zhulin.fakedet.data.helpers.ChannelHelpers.createGrpcChannel
import com.zhulin.fakedet.data.repositories.StringResourceRepositoryImpl
import com.zhulin.fakedet.data.stores.SettingsStore
import com.zhulin.fakedet.data.stores.UserStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideStringResourceRepository(application: Application): StringResourceRepository {
        return StringResourceRepositoryImpl(application)
    }


    @Provides
    @Singleton
    fun provideSettingsStore(application: Application): SettingsStore {
        return SettingsStore(application)
    }

    @Provides
    @Singleton
    fun provideUserStore(application: Application): UserStore {
        return UserStore(application)
    }


    @Provides
    @Singleton
    fun provideAuthGrpcClient(application: Application): AuthGrpcClient {
        val uri = Uri.parse("https://192.168.0.105:7146")

        return AuthGrpcClient(
            createGrpcChannel(application, uri.host, uri.port)
        )
    }


    @Provides
    @Singleton
    fun provideAuthGateway(client: AuthGrpcClient): AuthGateway {
        return AuthGatewayImpl(client)
    }


    @Provides
    @Singleton
    fun providePostBridgeGrpcClient(application: Application): PostBridgeGrpcClient {
        val uri = Uri.parse("https://192.168.0.105:7136")

        return PostBridgeGrpcClient(
            createGrpcChannel(application, uri.host, uri.port)
        )
    }

    @Provides
    @Singleton
    fun provideContentHandler(application: Application): ContentHandler {
        return ContentHandler(application)
    }

    @Provides
    @Singleton
    fun providePostBridgeGateway(client: PostBridgeGrpcClient, userStore: UserStore): PostBridgeGateway {
        return PostBridgeGatewayImpl(client, userStore)
    }
}