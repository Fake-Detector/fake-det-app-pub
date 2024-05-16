package com.zhulin.fakedet.ui.screens.authorization.states

sealed class ScreenEvent {
    data class UpdateInLogin(val value: String) : ScreenEvent()
    data class UpdateInPassword(val value: String) : ScreenEvent()
    data class UpdateUpName(val value: String) : ScreenEvent()
    data class UpdateUpLogin(val value: String) : ScreenEvent()
    data class UpdateUpPassword(val value: String) : ScreenEvent()
    data class UpdateScreenState(val value: ScreenStateEnum) : ScreenEvent()
    data class UpdateRestoreLogin(val value: String) : ScreenEvent()
    data class UpdateRestorePassword(val value: String) : ScreenEvent()
    data object Auth: ScreenEvent()
    data object SignIn : ScreenEvent()
    data object Restore : ScreenEvent()
    data object SignUp : ScreenEvent()
}