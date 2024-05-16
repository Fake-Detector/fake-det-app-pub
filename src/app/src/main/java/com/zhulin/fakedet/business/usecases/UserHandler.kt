package com.zhulin.fakedet.business.usecases

import com.zhulin.fakedet.data.stores.UserStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserHandler @Inject constructor(
    private val userStore: UserStore
) {
    suspend fun signOut() = userStore.update("", "")

    suspend fun getName() = userStore.getName.first()
    suspend fun getLogin() = userStore.getLogin.first()
}