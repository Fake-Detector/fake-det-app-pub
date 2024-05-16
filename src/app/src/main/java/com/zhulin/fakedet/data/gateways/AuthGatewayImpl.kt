package com.zhulin.fakedet.data.gateways

import android.util.Log
import com.zhulin.fakedet.business.gateways.AuthGateway
import com.zhulin.fakedet.business.models.AuthInfo
import com.zhulin.fakedet.data.grpc.clients.AuthGrpcClient
import fake.detection.auth.Auth.AuthRequest
import fake.detection.auth.Auth.CreateUserRequest
import fake.detection.auth.Auth.GenerateTokenRequest
import fake.detection.auth.Auth.LoginRequest
import fake.detection.auth.Auth.RestorePasswordRequest
import fake.detection.auth.Auth.UpdateUserRequest
import io.grpc.Metadata


class AuthGatewayImpl(
    private val client: AuthGrpcClient
) : AuthGateway {
    override suspend fun register(login: String, name: String, password: String): AuthInfo {
        return try {
            val request = CreateUserRequest
                .newBuilder()
                .apply { this.login = login; this.name = name; this.password = password }
                .build()

            val response = client.serviceStub.createUser(request)

            AuthInfo(
                result = response.result,
                userName = response.user.name,
                errorStatus = response.errorStatus,
                token = response.user.token
            )
        } catch (e: Exception) {
            Log.e("FAKEDET-AUTH", e.message ?: "SOME EXCEPTION");
            AuthInfo()
        }
    }

    override suspend fun login(login: String, password: String): AuthInfo {
        return try {
            val request = LoginRequest
                .newBuilder()
                .apply { this.login = login; this.password = password }
                .build()

            val response = client.serviceStub.login(request)

            AuthInfo(
                result = response.result,
                userName = response.user.name,
                errorStatus = response.errorStatus,
                token = response.user.token
            )
        } catch (e: Exception) {
            Log.e("FAKEDET-AUTH", e.message ?: "SOME EXCEPTION");
            AuthInfo()
        }
    }

    override suspend fun auth(token: String): AuthInfo {
        return try {
            val request = AuthRequest.newBuilder().build()

            val metaDataKey = Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER)
            val metadata = Metadata()
            metadata.put(metaDataKey, "Bearer $token")
            val response = client.serviceStub.auth(request, metadata)

            AuthInfo(
                result = response.result,
                userName = response.user.name,
                errorStatus = response.errorStatus,
                token = response.user.token
            )
        } catch (e: Exception) {
            Log.e("FAKEDET-AUTH", e.message ?: "SOME EXCEPTION");
            AuthInfo()
        }
    }

    override suspend fun update(name: String, password: String, token: String): AuthInfo {
        return try {
            val request = UpdateUserRequest
                .newBuilder()
                .apply { this.name = name; this.password = password }
                .build()

            val metaDataKey = Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER)
            val metadata = Metadata()
            metadata.put(metaDataKey, "Bearer $token")

            val response = client.serviceStub.updateUser(request, metadata)

            AuthInfo(
                result = response.result,
                userName = response.user.name,
                errorStatus = response.errorStatus,
                token = response.user.token
            )
        } catch (e: Exception) {
            Log.e("FAKEDET-AUTH", e.message ?: "SOME EXCEPTION");
            AuthInfo()
        }
    }

    override suspend fun getLinkToken(token: String): AuthInfo {
        return try {
            val request = GenerateTokenRequest
                .newBuilder()
                .build()

            val metaDataKey = Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER)
            val metadata = Metadata()
            metadata.put(metaDataKey, "Bearer $token")

            val response = client.serviceStub.generateToken(request, metadata)

            AuthInfo(
                result = response.result,
                errorStatus = response.errorStatus,
                token = response.token
            )
        } catch (e: Exception) {
            Log.e("FAKEDET-AUTH", e.message ?: "SOME EXCEPTION");
            AuthInfo()
        }
    }

    override suspend fun restorePassword(login: String, password: String): AuthInfo {
        return try {
            val request = RestorePasswordRequest
                .newBuilder()
                .apply { this.login = login; this.newPassword = password }
                .build()

            val response = client.serviceStub.restorePassword(request)

            AuthInfo(
                result = response.result,
                errorStatus = response.errorStatus,
                userName = response.user.name,
                token = response.user.token
            )
        } catch (e: Exception) {
            Log.e("FAKEDET-AUTH", e.message ?: "SOME EXCEPTION");
            AuthInfo()
        }    }
}