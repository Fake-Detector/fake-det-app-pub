package com.zhulin.fakedet.ui.screens.authorization.states

enum class ScreenStateEnum {
    SignIn, SignUp, Loading, Restore
}

data class ScreenState(
    val state: ScreenStateEnum = ScreenStateEnum.Loading,
)