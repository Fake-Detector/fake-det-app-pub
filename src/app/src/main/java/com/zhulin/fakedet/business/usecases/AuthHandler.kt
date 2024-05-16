package com.zhulin.fakedet.business.usecases

import com.zhulin.fakedet.business.gateways.AuthGateway
import com.zhulin.fakedet.business.models.AuthInfo
import com.zhulin.fakedet.data.stores.UserStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AuthHandler @Inject constructor(
    private val gateway: AuthGateway,
    private val userStore: UserStore
) {
    suspend fun register(login: String, name: String, password: String) =
        gateway.register(login, name, password)

    suspend fun login(login: String, password: String) = gateway.login(login, password)
    suspend fun auth() = gateway.auth(userStore.getToken.first())
    suspend fun update(name: String, password: String): AuthInfo =
        gateway.update(name, password, userStore.getToken.first())

    suspend fun save(name: String, token: String) = userStore.update(name, token)

    suspend fun getToken() = gateway.getLinkToken(userStore.getToken.first())

    suspend fun restorePassword(login: String, password: String) =
        gateway.restorePassword(login, password)
}