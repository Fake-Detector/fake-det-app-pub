package com.zhulin.fakedet.ui.screens

import androidx.lifecycle.ViewModel
import com.zhulin.fakedet.business.usecases.UseCaseHandler
import com.zhulin.fakedet.ui.screens.authorization.states.ScreenStateEnum
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

abstract class AbstractViewModel(
    private val useCaseHandler: UseCaseHandler
) : ViewModel() {

    fun getStringResource(id: Int): String {
        var result = ""

        runBlocking {
            result = async { useCaseHandler.getStringResource(id) }.await()
        }

        return result
    }

    suspend fun auth() : Boolean {
        val login = useCaseHandler.userHandler.getLogin()
        val name = useCaseHandler.userHandler.getName()

        return login.isNotBlank() && name.isNotBlank()
    }
}