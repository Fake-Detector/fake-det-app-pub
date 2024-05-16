package com.zhulin.fakedet.business.gateways

import com.zhulin.fakedet.business.models.AuthInfo

interface AuthGateway {
    suspend fun register(login: String, name: String, password: String): AuthInfo
    suspend fun login(login: String, password: String): AuthInfo
    suspend fun auth(token: String): AuthInfo
    suspend fun update(name: String, password: String, token: String): AuthInfo
    suspend fun getLinkToken(token: String): AuthInfo

    suspend fun restorePassword(login: String, password: String): AuthInfo
}